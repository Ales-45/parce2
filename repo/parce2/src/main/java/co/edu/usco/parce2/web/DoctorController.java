package co.edu.usco.parce2.web;

import co.edu.usco.parce2.model.Doctor;
import co.edu.usco.parce2.repository.DoctorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/medicos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class DoctorController {
    private final DoctorRepository doctors;
    public DoctorController(DoctorRepository doctors) { this.doctors = doctors; }

    @Operation(summary = "Lista médicos para asignar consultas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista obtenida"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es administrador")})
    @GetMapping
    public List<Doctor> all() { return doctors.findAll(); }

    @Operation(summary = "Crea un médico")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Médico creado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es administrador")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@Valid @RequestBody Doctor doctor) { return doctors.save(doctor); }

    @Operation(summary = "Edita un médico")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Médico actualizado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es administrador")})
    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @Valid @RequestBody Doctor input) {
        Doctor doctor = doctors.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        doctor.setName(input.getName());
        doctor.setSpecialty(input.getSpecialty());
        doctor.setOfficeNumber(input.getOfficeNumber());
        return doctors.save(doctor);
    }

    @Operation(summary = "Elimina un médico")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Médico eliminado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No es administrador")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { doctors.deleteById(id); }
}
