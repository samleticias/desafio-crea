package samleticias.desafiocreaapi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import samleticias.desafiocreaapi.domain.entities.enums.ProfessionalType;
import samleticias.desafiocreaapi.domain.entities.enums.RegistrationStatus;
import samleticias.desafiocreaapi.rest.dto.ProfessionalDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professional_title",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "title_id")
    )
    private List<Title> titles;

    public Professional(ProfessionalDTO dto) {
        this.id = dto.id();
        this.email = dto.email();
        this.name = dto.name();
        this.password = dto.password();
        this.birthDate = dto.birthDate();
        this.registrationDate = dto.registrationDate();
        this.phone = dto.phone();
        this.professionalType = dto.professionalType();
    }

    public void setUniqueCode() {
        this.uniqueCode = id.toString() + "PI";
    }
}
