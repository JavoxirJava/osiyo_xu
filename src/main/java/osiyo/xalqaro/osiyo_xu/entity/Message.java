package osiyo.xalqaro.osiyo_xu.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String caption;
    String message;
    String fileId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    MessageType type;

    @ManyToOne
    Content content;
}
