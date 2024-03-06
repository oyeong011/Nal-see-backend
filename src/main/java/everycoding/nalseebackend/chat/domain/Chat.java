package everycoding.nalseebackend.chat.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "chats")
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
