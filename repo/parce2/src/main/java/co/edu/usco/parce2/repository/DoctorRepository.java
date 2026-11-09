package co.edu.usco.parce2.repository;

import co.edu.usco.parce2.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
