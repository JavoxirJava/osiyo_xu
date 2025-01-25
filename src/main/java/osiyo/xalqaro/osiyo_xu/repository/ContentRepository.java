package osiyo.xalqaro.osiyo_xu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import osiyo.xalqaro.osiyo_xu.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Content c WHERE c.subject.id = :subjectId")
//    void deleteBySubjectId(@Param("subjectId") Long subjectId);
//
//    @Modifying
//    @Transactional
//    @Query("SELECT c FROM Content c WHERE c.subject.name = :subjectName")
//    List<Content> findBySubjectName(@Param("subjectName") String subjectName);
}
