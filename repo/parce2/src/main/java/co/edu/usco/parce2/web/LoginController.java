package co.edu.usco.parce2.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/acceso-denegado")
    public String denied() { return "denied"; }

    @GetMapping("/dashboard")
    public String dashboard() { return "dashboard"; }
}
