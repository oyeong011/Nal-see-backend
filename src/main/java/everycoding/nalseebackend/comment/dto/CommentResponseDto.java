package everycoding.nalseebackend.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private int likeCNT;

    private Long userId;
    private String userPicture;
    private String username;

    private Long postId;

    @Builder
    public CommentResponseDto(Long id, String content, int likeCNT, Long userId, String userPicture, String username, Long postId) {
        this.id = id;
        this.content = content;
        this.likeCNT = likeCNT;
        this.userId = userId;
        this.userPicture = userPicture;
        this.username = username;
        this.postId = postId;
    }
}
