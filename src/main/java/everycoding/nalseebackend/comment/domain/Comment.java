package everycoding.nalseebackend.comment.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Integer likeCNT;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
