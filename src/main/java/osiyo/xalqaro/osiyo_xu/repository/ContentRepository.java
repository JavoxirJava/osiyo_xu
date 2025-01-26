package osiyo.xalqaro.osiyo_xu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import osiyo.xalqaro.osiyo_xu.entity.Content;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    //    @Modifying
//    @Transactional
//    @Query("DELETE FROM Content c WHERE c.subject.id = :subjectId")
//    void deleteBySubjectId(@Param("subjectId") Long subjectId);
//
    List<Content> findByDirectionAndScienceAndSemester(String direction, String science, String semester);
    Optional<Content> findByDirectionAndScienceAndSemesterAndSubject(String direction, String science, String semester, String subject);
}
