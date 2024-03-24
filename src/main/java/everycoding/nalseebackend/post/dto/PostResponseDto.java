package everycoding.nalseebackend.post.dto;

import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.weather.Weather;
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

    private Weather weather;
    private double temperature;

    private Long userId;
    private String username;
    private String userImage;

    @Builder
    public PostResponseDto(Long id, List<String> pictureList, String content, int likeCnt, boolean isLiked, LocalDateTime createDate, String address, Weather weather, double temperature, Long userId, String username, String userImage) {
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

    static public PostResponseDto createPostResponseDto(Post post, boolean isLiked) {
        return PostResponseDto.builder()
                .id(post.getId())
                .pictureList(post.getPictureList())
                .content(post.getContent())
                .likeCnt(post.getLikeCNT())
                .isLiked(isLiked)
                .address(post.getAddress())
                .weather(post.getWeather())
                .temperature(post.getTemperature())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .userImage(post.getUser().getPicture())
                .build();
    }
}
