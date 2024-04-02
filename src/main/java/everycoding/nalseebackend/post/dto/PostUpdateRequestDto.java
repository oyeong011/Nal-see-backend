package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.user.domain.UserDetail;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String content;
    private UserDetail userDetail;
}
