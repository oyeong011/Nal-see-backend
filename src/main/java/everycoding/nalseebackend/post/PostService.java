package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts(Long lastPostId, int size);

    List<PostResponseDto> getPostsInLocation(double bottomLeftLat, double bottomLeftLong,
                                             double topRightLat, double topRightLong);

    List<PostResponseDto> searchPosts(List<String> weathers, Double minTemperature, Double maxTemperature, Double minHeight, Double maxHeight,
                                      Double minWeight, Double maxWeight, String constitution, List<String> styles, String gender);

    void post(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException;

    void likePost(Long userId, Long postId);

    void cancelLikePost(Long userId, Long postId);
}
