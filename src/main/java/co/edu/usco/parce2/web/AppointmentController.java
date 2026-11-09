package co.edu.usco.parce2.web;

import co.edu.usco.parce2.model.*;
import co.edu.usco.parce2.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/consultas")
public class AppointmentController {
    private final AppointmentRepository appointments;
    private final DoctorRepository doctors;
    private final AppUserRepository users;

    public AppointmentController(AppointmentRepository appointments, DoctorRepository doctors, AppUserRepository users) {
        this.appointments = appointments; this.doctors = doctors; this.users = users;
    }

    @Operation(summary = "Crea una consulta y asigna médico y paciente")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Consulta creada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es administrador")})
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment create(@Valid @RequestBody AppointmentRequest request) {
        return appointments.save(request.toEntity(doctors, users));
    }

    @Operation(summary = "Consulta las citas propias según el rol")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consultas obtenidas"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Rol no autorizado")})
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','MEDICO','PACIENTE')")
    public List<Appointment> list(Authentication authentication) {
        AppUser user = users.findByUsername(authentication.getName()).orElseThrow();
        if (user.getRole() == Role.MEDICO) {
            return appointments.findByDoctorId(user.getDoctor().getId());
        }
        if (user.getRole() == Role.PACIENTE) {
            return appointments.findByPatientUsername(user.getUsername());
        }
        return appointments.findAll();
    }

    @Operation(summary = "Actualiza únicamente el horario de una cita propia")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Horario actualizado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es paciente autorizado")})
    @PatchMapping("/{id}/horario")
    @PreAuthorize("hasRole('PACIENTE')")
    public Appointment updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request,
                                      Authentication authentication) {
        Appointment appointment = appointments.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!appointment.getPatient().getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cita no pertenece al paciente");
        }
        if (!request.startTime().isBefore(request.endTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El inicio debe ser anterior al final");
        }
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        return appointments.save(appointment);
    }

    public record ScheduleRequest(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {}
    public record AppointmentRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max = 40)
                                     String patientUsername, @jakarta.validation.constraints.NotBlank
                                     @jakarta.validation.constraints.Size(max = 40) String patientName,
                                     @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max = 100)
                                     String reason, @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive
                                     Integer officeNumber, @jakarta.validation.constraints.NotNull
                                     java.time.LocalDateTime startTime, @jakarta.validation.constraints.NotNull
                                     java.time.LocalDateTime endTime, @jakarta.validation.constraints.NotNull Long doctorId) {
        Appointment toEntity(DoctorRepository doctors, AppUserRepository users) {
            if (!startTime.isBefore(endTime)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El inicio debe ser anterior al final");
            Doctor doctor = doctors.findById(doctorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            AppUser patient = users.findByUsername(patientUsername)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            return new Appointment(patientName, reason, officeNumber, startTime, endTime, doctor, patient);
        }
    }
}
