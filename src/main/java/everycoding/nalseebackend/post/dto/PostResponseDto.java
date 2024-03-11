package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private List<String> pictureList;
    private int likeCnt;

    private Long userId;
    private String username;
    private String picture;

    @Builder
    public PostResponseDto(java.lang.Long id, List<String> pictureList, int likeCnt, java.lang.Long userId, String username, String picture) {
        this.id = id;
        this.pictureList = pictureList;
        this.likeCnt = likeCnt;
        this.userId = userId;
        this.username = username;
        this.picture = picture;
    }
}
