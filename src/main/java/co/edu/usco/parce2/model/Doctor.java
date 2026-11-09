package co.edu.usco.parce2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 80)
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String specialty;
    @Column(nullable = false)
    private Integer officeNumber;

    protected Doctor() {}

    public Doctor(String name, String specialty, Integer officeNumber) {
        this.name = name;
        this.specialty = specialty;
        this.officeNumber = officeNumber;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public Integer getOfficeNumber() { return officeNumber; }
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setOfficeNumber(Integer officeNumber) { this.officeNumber = officeNumber; }
}
