package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts(Long userId, Long lastPostId, int size);

    List<PostResponseDto> getPostsInLocation(Long userId, double bottomLeftLat, double bottomLeftLong,
                                             double topRightLat, double topRightLong);

    PostResponseDto getPost(Long userId, Long postId);

    List<PostResponseDto> searchPosts(Long userId, List<String> weathers, Double minTemperature, Double maxTemperature, Double minHeight, Double maxHeight,
                                      Double minWeight, Double maxWeight, String constitution, List<String> styles, String gender);

    void post(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException;

    void likePost(Long userId, Long postId);

    void cancelLikePost(Long userId, Long postId);
}
