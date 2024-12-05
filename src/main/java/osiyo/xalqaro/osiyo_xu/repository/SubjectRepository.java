package osiyo.xalqaro.osiyo_xu.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import osiyo.xalqaro.osiyo_xu.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);

    @Modifying
    @Transactional
    @Query("SELECT s.name FROM Subject s WHERE s.science.id = ?1")
    List<String> findByScience(Long scienceId);

    @Modifying
    @Transactional
    @Query("SELECT s.name FROM Subject s WHERE s.science.name = ?1")
    List<String> findByScience(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM content WHERE subject_id = (SELECT id FROM subject WHERE name = :name); " +
            "DELETE FROM subject WHERE name = :name", nativeQuery = true)
    void deleteSubjectAndContents(@Param("name") String name);
}
