package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;

import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts();

    List<PostResponseDto> getPostsInLocation(double latitude, double longitude);

    List<PostResponseDto> searchPosts(String keyword);

    void post(PostRequestDto postRequestDto);

    void likePost(Long userId, Long postId);
}
