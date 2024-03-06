package everycoding.nalseebackend.user.domain;

import everycoding.nalseebackend.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "userINF")
public class UserINF extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer height;
    private Integer weight;
    private String bodyShape;
    private String constitution;
    @Enumerated(EnumType.STRING)
    private FashionStyle style;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
