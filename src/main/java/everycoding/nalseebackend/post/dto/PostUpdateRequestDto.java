package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.user.domain.UserInfo;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String content;
    private UserInfo userInfo;
}
