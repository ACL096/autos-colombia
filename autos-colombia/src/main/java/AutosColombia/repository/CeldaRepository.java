package AutosColombia.repository;

import AutosColombia.model.Celda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, String> {
}

