package everycoding.nalseebackend.post.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import everycoding.nalseebackend.user.domain.User;
import everycoding.nalseebackend.weather.domain.Weather;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> pictureList;

    private String content;

    private Integer likeCNT;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Weather weather;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

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

    private String link;
    private String brand;
    private String productName;

    @Builder
    public Post(List<String> pictureList, String content, User user, Weather weather, Double longitude, Double latitude, Integer height, Integer weight, String bodyShape, String constitution, FashionStyle style, Gender gender) {
        this.pictureList = pictureList;
        this.content = content;
        likeCNT = 0;
        this.user = user;
        this.weather = weather;
        comments = new ArrayList<>();
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
        this.weight = weight;
        this.bodyShape = bodyShape;
        this.constitution = constitution;
        this.style = style;
        this.gender = gender;
    }
}
