package everycoding.nalseebackend.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private List<String> pictureList;
    private String content;
    private int likeCnt;
    private boolean isLiked;
    private LocalDateTime createDate;

    private Long userId;
    private String username;
    private String userImage;

    //TODO 장소정보, 날씨정보

    @Builder
    public PostResponseDto(java.lang.Long id, List<String> pictureList, String content, int likeCnt, boolean isLiked, LocalDateTime createDate, java.lang.Long userId, String username, String userImage) {
        this.id = id;
        this.pictureList = pictureList;
        this.content = content;
        this.likeCnt = likeCnt;
        this.isLiked = isLiked;
        this.createDate = createDate;
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
    }
}
