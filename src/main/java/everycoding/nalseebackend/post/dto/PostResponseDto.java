package everycoding.nalseebackend.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private List<String> pictureList;
    private String content;
    private int likeCnt;

    private Long userId;
    private String username;
    private String picture;

    @Builder
    public PostResponseDto(java.lang.Long id, List<String> pictureList, String content, int likeCnt, java.lang.Long userId, String username, String picture) {
        this.id = id;
        this.pictureList = pictureList;
        this.content = content;
        this.likeCnt = likeCnt;
        this.userId = userId;
        this.username = username;
        this.picture = picture;
    }
}
