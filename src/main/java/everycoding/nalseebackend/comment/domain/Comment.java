package everycoding.nalseebackend.comment.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor
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

    @Builder
    public Comment(String content, User user, Post post) {
        this.content = content;
        likeCNT = 0;
        this.user = user;
        this.post = post;
    }
}
