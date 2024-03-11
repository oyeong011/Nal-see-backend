package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<PostResponseDto> getPosts(int lastPostId, int size);

    List<PostResponseDto> getPostsInLocation(double latitude, double longitude);

    List<PostResponseDto> searchPosts(String keyword);

    void post(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException;

    void likePost(Long userId, Long postId);
}
