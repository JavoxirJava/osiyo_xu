package osiyo.xalqaro.osiyo_xu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;

import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "content_photos", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "photo")
    private List<String> photos;
    private String fileId;
    private Integer messageId;

    @ManyToOne(optional = false)
    private Subject subject;
}
