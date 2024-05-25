package samleticias.desafiocreaapi.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samleticias.desafiocreaapi.domain.entities.Title;

@Repository
public interface TitleRepository extends JpaRepository<Title, Integer> {
}
