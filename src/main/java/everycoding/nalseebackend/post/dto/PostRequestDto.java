package everycoding.nalseebackend.post.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {

    private Long userId;

    private Long content;
    private int likeCNT;

    private Double latitude;
    private Double longitude;

    private int height;
    private int weight;
    private String bodyShape;
    private String constitution;
    private String style;
    private String gender;
}
