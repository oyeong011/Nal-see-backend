package everycoding.nalseebackend.weather.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "weather")
public class Weather extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
