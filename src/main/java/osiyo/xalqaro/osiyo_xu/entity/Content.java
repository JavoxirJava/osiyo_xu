package osiyo.xalqaro.osiyo_xu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String fileId;
    private Integer messageId;

    @ManyToOne(optional = false)
    private Subject subject;
}
