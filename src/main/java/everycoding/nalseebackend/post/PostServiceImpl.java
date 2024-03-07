package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{


    @Override
    public List<PostResponseDto> getPosts() {
        return null;
    }

    @Override
    public List<PostResponseDto> getPostsInLocation(double latitude, double longitude) {
        return null;
    }

    @Override
    public List<PostResponseDto> searchPosts(String keyword) {
        return null;
    }

    @Override
    public void post(PostRequestDto postRequestDto) {

    }

    @Override
    public void likePost(Long userId, Long postId) {

    }
}
