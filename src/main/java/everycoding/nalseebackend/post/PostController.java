package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 기본 조회
    @GetMapping("/api/posts")
    public ApiResponse<List<PostResponseDto>> getPosts() {
        return ApiResponse.ok(postService.getPosts());
    }

    // 지도 기준 조회
    @GetMapping("/api/posts/location")
    public ApiResponse<List<PostResponseDto>> getPostsInLocation(@RequestParam double latitude, @RequestParam double longitude) {
        return ApiResponse.ok(postService.getPostsInLocation(latitude, longitude));
    }

    // 검색
    @GetMapping("/api/posts/search")
    public ApiResponse<List<PostResponseDto>> searchPosts(@RequestParam String keyword) {
        return ApiResponse.ok(postService.searchPosts(keyword));
    }

    // 게시물 등록
    @PostMapping("/api/posts")
    public ApiResponse<Void> post(PostRequestDto requestDto) {
        postService.post(requestDto);
        return ApiResponse.ok();
    }

    // 게시물 좋아요
    @GetMapping("/api/posts/{postId}/likes")
    public ApiResponse<Void> likePost(@PathVariable Long postId) {
        postService.likePost(1L, postId);
        return ApiResponse.ok();
    }
}
