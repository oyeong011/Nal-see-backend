package everycoding.nalseebackend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostForMapResponseDto {
    private PostResponseDto postResponseDto;
    private Double latitude;
    private Double longitude;
}
