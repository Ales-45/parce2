package co.edu.usco.parce2.config;

import co.edu.usco.parce2.model.*;
import co.edu.usco.parce2.repository.*;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(AppUserRepository users, DoctorRepository doctors,
                           AppointmentRepository appointments, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;
            Doctor doctor = doctors.save(new Doctor("Dra. Ana Gómez", "Medicina general", 101));
            AppUser admin = users.save(new AppUser("admin", encoder.encode("admin123"), Role.ADMINISTRADOR));
            AppUser physician = users.save(new AppUser("medico", encoder.encode("medico123"), Role.MEDICO));
            physician.setDoctor(doctor);
            users.save(physician);
            AppUser patient = users.save(new AppUser("paciente", encoder.encode("paciente123"), Role.PACIENTE));
            appointments.save(new Appointment("María Pérez", "Consulta de control", 101,
                    LocalDateTime.now().plusDays(1).withHour(9).withMinute(0),
                    LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), doctor, patient));
            users.save(admin);
        };
    }
}
