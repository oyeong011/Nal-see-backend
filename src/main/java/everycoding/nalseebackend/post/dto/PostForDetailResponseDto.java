package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.comment.dto.CommentResponseDto;
import everycoding.nalseebackend.user.domain.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostForDetailResponseDto {
    private PostResponseDto postResponseDto;
    private UserDetail userDetail;
    private List<CommentResponseDto> comments;
}
