package everycoding.nalseebackend.user.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.chat.domain.Chat;
import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.post.domain.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카카오에서 받아올 username, email, picture
    private String username;
    private String email;
    private String picture;

    private String role;

    @Setter
    private String refreshToken;

    private String provider;
//    private String providerId;

    @ElementCollection
    private List<String> postLikeList;

    @ElementCollection
    private List<String> commentLikeList;

    @OneToOne(fetch = FetchType.LAZY)
    private UserINF userINF;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Chat> chats;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
