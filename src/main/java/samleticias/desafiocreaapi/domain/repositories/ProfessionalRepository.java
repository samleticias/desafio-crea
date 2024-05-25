package samleticias.desafiocreaapi.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samleticias.desafiocreaapi.domain.entities.Professional;
import samleticias.desafiocreaapi.rest.dto.ProfessionalDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Integer> {
    public Optional<Professional> findProfessionalByEmail(String email);
    public Optional<Professional> findProfessionalByUniqueCode(String uniqueCode);

    List<Professional> findByTitles_Id(Integer titleId);
}
