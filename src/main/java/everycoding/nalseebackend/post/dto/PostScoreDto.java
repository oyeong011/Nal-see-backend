package everycoding.nalseebackend.post.dto;

import lombok.Getter;

@Getter
public class PostScoreDto {

    private PostResponseDto postResponseDto;
    private double score;

    public PostScoreDto(PostResponseDto postResponseDto, double score) {
        this.postResponseDto = postResponseDto;
        this.score = score;
    }
}
