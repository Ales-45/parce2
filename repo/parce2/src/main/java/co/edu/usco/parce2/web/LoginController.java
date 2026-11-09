package co.edu.usco.parce2.web;

import co.edu.usco.parce2.model.Role;
import co.edu.usco.parce2.repository.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final AppUserRepository users;

    public LoginController(AppUserRepository users) {
        this.users = users;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/acceso-denegado")
    public String denied() { return "denied"; }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        Role role = users.findByUsername(authentication.getName()).orElseThrow().getRole();
        return switch (role) {
            case ADMINISTRADOR -> "admin-dashboard";
            case MEDICO -> "medico-dashboard";
            case PACIENTE -> "paciente-dashboard";
        };
    }
}
