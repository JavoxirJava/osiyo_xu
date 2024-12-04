package osiyo.xalqaro.osiyo_xu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import osiyo.xalqaro.osiyo_xu.entity.Science;

import java.util.List;
import java.util.Optional;

public interface ScienceRepository extends JpaRepository<Science, Long> {
    Optional<Science> findByName(String name);

    @Query("SELECT s.name FROM Science s")
    List<String> findAllNames();
}
