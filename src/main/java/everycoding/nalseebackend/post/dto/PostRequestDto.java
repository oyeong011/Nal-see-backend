package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.user.domain.UserDetail;
import lombok.Getter;

@Getter
public class PostRequestDto {

    private Long userId;

    private String content;

    private String address;
    private Double latitude;
    private Double longitude;

    private UserDetail userDetail;
}
