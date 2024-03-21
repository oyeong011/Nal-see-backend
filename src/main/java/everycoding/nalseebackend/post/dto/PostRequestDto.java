package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.user.domain.Constitution;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import lombok.Getter;

@Getter
public class PostRequestDto {

    private Long userId;

    private String content;

    private String address;
    private Double latitude;
    private Double longitude;

    private Double height;
    private Double weight;
    private String bodyShape;
    private Constitution constitution;
    private FashionStyle style;
    private Gender gender;
}
