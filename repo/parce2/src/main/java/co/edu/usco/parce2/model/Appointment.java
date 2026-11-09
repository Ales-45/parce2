package co.edu.usco.parce2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 40)
    @Pattern(regexp = "^[\\p{L}\\p{M}0-9 .'-]+$")
    @Column(nullable = false, length = 40)
    private String patientName;
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String reason;
    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer officeNumber;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endTime;
    @ManyToOne(optional = false)
    private Doctor doctor;
    @ManyToOne(optional = false)
    private AppUser patient;

    protected Appointment() {}

    public Appointment(String patientName, String reason, Integer officeNumber,
                       LocalDateTime startTime, LocalDateTime endTime,
                       Doctor doctor, AppUser patient) {
        this.patientName = patientName;
        this.reason = reason;
        this.officeNumber = officeNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getReason() { return reason; }
    public Integer getOfficeNumber() { return officeNumber; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Doctor getDoctor() { return doctor; }
    public AppUser getPatient() { return patient; }
    public void setReason(String reason) { this.reason = reason; }
    public void setOfficeNumber(Integer officeNumber) { this.officeNumber = officeNumber; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
}
