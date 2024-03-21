package everycoding.nalseebackend.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class UserInfo {

    private double height;
    private double weight;
    @Enumerated(EnumType.STRING)
    private Constitution constitution;
    @Enumerated(EnumType.STRING)
    private FashionStyle style;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder
    public UserInfo(double height, double weight, Constitution constitution, FashionStyle style, Gender gender) {
        this.height = height;
        this.weight = weight;
        this.constitution = constitution;
        this.style = style;
        this.gender = gender;
    }
}
