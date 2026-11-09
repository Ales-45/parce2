package co.edu.usco.parce2.web;

import co.edu.usco.parce2.model.*;
import co.edu.usco.parce2.repository.*;
import java.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final DoctorRepository doctors;
    private final AppointmentRepository appointments;
    private final AppUserRepository users;

    public DashboardController(DoctorRepository doctors, AppointmentRepository appointments, AppUserRepository users) {
        this.doctors = doctors; this.appointments = appointments; this.users = users;
    }

    @PostMapping("/medicos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String createDoctor(@RequestParam String name, @RequestParam String specialty,
                               @RequestParam Integer officeNumber, RedirectAttributes redirect) {
        doctors.save(new Doctor(name, specialty, officeNumber));
        redirect.addFlashAttribute("message", "Médico creado correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/medicos/{id}/editar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String editDoctor(@PathVariable Long id, @RequestParam String name,
                             @RequestParam String specialty, @RequestParam Integer officeNumber,
                             RedirectAttributes redirect) {
        Doctor doctor = doctors.findById(id).orElseThrow();
        doctor.setName(name); doctor.setSpecialty(specialty); doctor.setOfficeNumber(officeNumber);
        doctors.save(doctor);
        redirect.addFlashAttribute("message", "Médico actualizado correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/medicos/{id}/eliminar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirect) {
        doctors.deleteById(id);
        redirect.addFlashAttribute("message", "Médico eliminado correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/consultas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String createAppointment(@RequestParam String patientUsername, @RequestParam String patientName,
                                    @RequestParam String reason, @RequestParam Integer officeNumber,
                                    @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime,
                                    @RequestParam Long doctorId, RedirectAttributes redirect) {
        if (!startTime.isBefore(endTime)) {
            redirect.addFlashAttribute("error", "La hora inicial debe ser anterior a la hora final.");
            return "redirect:/dashboard";
        }
        appointments.save(new Appointment(patientName, reason, officeNumber, startTime, endTime,
                doctors.findById(doctorId).orElseThrow(), users.findByUsername(patientUsername).orElseThrow()));
        redirect.addFlashAttribute("message", "Consulta asignada correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/consultas/{id}/horario")
    @PreAuthorize("hasRole('PACIENTE')")
    public String updateSchedule(@PathVariable Long id, @RequestParam LocalDateTime startTime,
                                 @RequestParam LocalDateTime endTime, org.springframework.security.core.Authentication auth,
                                 RedirectAttributes redirect) {
        Appointment appointment = appointments.findById(id).orElseThrow();
        if (!appointment.getPatient().getUsername().equals(auth.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("La consulta no pertenece al paciente.");
        }
        if (!startTime.isBefore(endTime)) {
            redirect.addFlashAttribute("error", "La hora inicial debe ser anterior a la hora final.");
            return "redirect:/dashboard";
        }
        appointment.setStartTime(startTime); appointment.setEndTime(endTime);
        appointments.save(appointment);
        redirect.addFlashAttribute("message", "Horario actualizado correctamente.");
        return "redirect:/dashboard";
    }
}
