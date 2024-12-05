package osiyo.xalqaro.osiyo_xu.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import osiyo.xalqaro.osiyo_xu.entity.Content;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Content c WHERE c.subject.id = :subjectId")
    void deleteBySubjectId(@Param("subjectId") Long subjectId);

    @Modifying
    @Transactional
    @Query("SELECT c FROM Content c WHERE c.subject.name = :subjectName")
    List<Content> findBySubjectName(@Param("subjectName") String subjectName);
}
