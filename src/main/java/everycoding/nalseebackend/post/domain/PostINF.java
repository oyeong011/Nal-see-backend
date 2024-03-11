//package everycoding.nalseebackend.post.domain;
//
//import everycoding.nalseebackend.BaseEntity;
//import everycoding.nalseebackend.user.domain.FashionStyle;
//import everycoding.nalseebackend.user.domain.Gender;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "postINF")
//@NoArgsConstructor
//public class PostINF extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Double longitude;
//    private Double latitude;
//
//    private Integer height;
//    private Integer weight;
//    private String bodyShape;
//    private String constitution;
//
//    @Enumerated(EnumType.STRING)
//    private FashionStyle style;
//
//    @Enumerated(EnumType.STRING)
//    private Gender gender;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post")
//    private Post post;
//
//    @Builder
//    public PostINF(Double longitude, Double latitude, Integer height, Integer weight, String bodyShape, String constitution, FashionStyle style, Gender gender) {
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.height = height;
//        this.weight = weight;
//        this.bodyShape = bodyShape;
//        this.constitution = constitution;
//        this.style = style;
//        this.gender = gender;
//    }
//}
