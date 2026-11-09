package co.edu.usco.parce2.web;

import co.edu.usco.parce2.model.*;
import co.edu.usco.parce2.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class LoginController {
    private final AppUserRepository users;
    private final DoctorRepository doctors;
    private final AppointmentRepository appointments;

    public LoginController(AppUserRepository users, DoctorRepository doctors, AppointmentRepository appointments) {
        this.users = users; this.doctors = doctors; this.appointments = appointments;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/acceso-denegado")
    public String denied() { return "denied"; }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        AppUser user = users.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("appointments", user.getRole() == Role.MEDICO
                ? appointments.findByDoctorId(user.getDoctor().getId())
                : user.getRole() == Role.PACIENTE
                ? appointments.findByPatientUsername(user.getUsername()) : appointments.findAll());
        if (user.getRole() == Role.ADMINISTRADOR) {
            model.addAttribute("doctors", doctors.findAll());
            model.addAttribute("patients", users.findAll().stream()
                    .filter(candidate -> candidate.getRole() == Role.PACIENTE).toList());
        }
        return "dashboard";
    }
}
