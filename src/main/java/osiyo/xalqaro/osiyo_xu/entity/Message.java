package osiyo.xalqaro.osiyo_xu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    String id;

    @Column(nullable = false)
    MessageType type;

    @ManyToOne
    Content content;
}
