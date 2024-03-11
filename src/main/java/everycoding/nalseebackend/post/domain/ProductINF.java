package everycoding.nalseebackend.post.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "productINF")
public class ProductINF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;
    private String brand;
    private String productName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
