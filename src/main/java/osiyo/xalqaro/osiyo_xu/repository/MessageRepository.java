package osiyo.xalqaro.osiyo_xu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import osiyo.xalqaro.osiyo_xu.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
}
