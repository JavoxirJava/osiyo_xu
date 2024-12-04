package osiyo.xalqaro.osiyo_xu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import osiyo.xalqaro.osiyo_xu.entity.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
}
