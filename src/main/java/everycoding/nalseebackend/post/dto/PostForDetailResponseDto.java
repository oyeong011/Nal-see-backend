package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.comment.dto.CommentResponseDto;
import everycoding.nalseebackend.user.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostForDetailResponseDto {
    private PostResponseDto postResponseDto;
    private UserInfo userInfo;
    private List<CommentResponseDto> comments;
}
