package everycoding.nalseebackend.user.dto;

import everycoding.nalseebackend.user.domain.Constitution;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfoRequestDto {
    private double height;
    private double weight;
    private Constitution constitution;
    private List<FashionStyle> style;
    private Gender gender;
}
