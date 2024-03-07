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
    private String Long;
    private int likeCnt;

    private Long userId;
    private String username;
    private String picture;

    private List<Comment> comments;

    @Builder
    public PostResponseDto(java.lang.Long id, List<String> pictureList, String aLong, int likeCnt, java.lang.Long userId, String username, String picture, List<Comment> comments) {
        this.id = id;
        this.pictureList = pictureList;
        Long = aLong;
        this.likeCnt = likeCnt;
        this.userId = userId;
        this.username = username;
        this.picture = picture;
        this.comments = comments;
    }
}
