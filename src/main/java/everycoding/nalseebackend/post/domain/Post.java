package everycoding.nalseebackend.post.domain;

import everycoding.nalseebackend.BaseEntity;
import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.user.domain.Constitution;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import everycoding.nalseebackend.user.domain.User;
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

    private int likeCNT;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    private String address;
    private double longitude;
    private double latitude;

    private String weather;
    private double temperature;

    private Integer height;
    private Integer weight;
    private String bodyShape;

    @Enumerated(EnumType.STRING)
    private Constitution constitution;

    @Enumerated(EnumType.STRING)
    private FashionStyle style;

    @Enumerated(EnumType.STRING)
    private Gender gender;

//    private String link;
//    private String brand;
//    private String productName;

    @Builder
    public Post(List<String> pictureList, String content, User user, String weather, double temperature, String address, double longitude, double latitude, int height, int weight, String bodyShape, Constitution constitution, FashionStyle style, Gender gender) {
        this.pictureList = pictureList;
        this.content = content;
        likeCNT = 0;
        this.user = user;
        comments = new ArrayList<>();
        this.weather = weather;
        this.temperature = temperature;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
        this.weight = weight;
        this.bodyShape = bodyShape;
        this.constitution = constitution;
        this.style = style;
        this.gender = gender;
    }

    public void increaseLikeCNT() {
        likeCNT++;
    }

    public void decreaseLikeCNT() {
        likeCNT--;
    }
}
