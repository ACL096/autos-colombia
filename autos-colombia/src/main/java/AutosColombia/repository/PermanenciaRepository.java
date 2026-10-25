package AutosColombia.repository;

import AutosColombia.model.Permanencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermanenciaRepository
        extends JpaRepository<Permanencia, Long> {
}
