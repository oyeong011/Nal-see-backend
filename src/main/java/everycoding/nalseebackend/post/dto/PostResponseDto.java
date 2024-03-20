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

    private String address;

    private String weather;
    private double temperature;

    private Long userId;
    private String username;
    private String userImage;

    @Builder
    public PostResponseDto(Long id, List<String> pictureList, String content, int likeCnt, boolean isLiked, LocalDateTime createDate, String address, String weather, double temperature, Long userId, String username, String userImage) {
        this.id = id;
        this.pictureList = pictureList;
        this.content = content;
        this.likeCnt = likeCnt;
        this.isLiked = isLiked;
        this.createDate = createDate;
        this.address = address;
        this.weather = weather;
        this.temperature = temperature;
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
    }
}
