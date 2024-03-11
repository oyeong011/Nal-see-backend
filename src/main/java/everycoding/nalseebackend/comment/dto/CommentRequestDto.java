package everycoding.nalseebackend.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long userId;
}
