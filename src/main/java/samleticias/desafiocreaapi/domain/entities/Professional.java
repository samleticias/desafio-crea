package samleticias.desafiocreaapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import samleticias.desafiocreaapi.domain.entities.enums.ProfessionalType;
import samleticias.desafiocreaapi.domain.entities.enums.RegistrationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "professional")
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true)
    private String uniqueCode;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "registration_status")
    private RegistrationStatus registrationStatus;

    @Column(name = "professional_type")
    private ProfessionalType professionalType;

    @Column(name = "visa_date")
    private LocalDate visaDate;

    public void setUniqueCode() {
        this.uniqueCode = id.toString() + "PI";
    }
}
