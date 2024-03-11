package everycoding.nalseebackend.post.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.user.domain.User;
import everycoding.nalseebackend.weather.domain.Weather;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> picture_list;

    private Long content;

    private Integer likeCNT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post")
    private Weather weather;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToOne(mappedBy = "post")
    private PostINF postINF;

    @OneToOne(mappedBy = "post")
    private ProductINF productINF;

}
