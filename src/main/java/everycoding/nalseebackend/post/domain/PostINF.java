package everycoding.nalseebackend.post.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "postINF")
public class PostINF extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;
    private Double latitude;

    private Integer height;
    private Integer weight;
    private String bodyShape;
    private String constitution;

    @Enumerated(EnumType.STRING)
    private FashionStyle style;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;


}
