package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 기본 조회
    @GetMapping("/api/posts")
    public ApiResponse<List<PostResponseDto>> getPosts(@RequestParam Long lastPostId, @RequestParam int size) {
        return ApiResponse.ok(postService.getPosts(lastPostId, size));
    }

    // 지도 기준 조회
    @GetMapping("/api/posts/location")
    public ApiResponse<List<PostResponseDto>> getPostsInLocation(
            @RequestParam double bottomLeftLat, @RequestParam double bottomLeftLong,
            @RequestParam double topRightLat, @RequestParam double topRightLong) {
        return ApiResponse.ok(postService.getPostsInLocation(bottomLeftLat, bottomLeftLong, topRightLat, topRightLong));
    }

    // 검색
    @GetMapping("/api/posts/search")
    public ApiResponse<List<PostResponseDto>> searchPosts(
            @RequestParam(required = false) List<String> weathers,
            @RequestParam(required = false) Double minTemperature,
            @RequestParam(required = false) Double maxTemperature,
            @RequestParam(required = false) Double minHeight,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @RequestParam(required = false) String constitution,
            @RequestParam(required = false) List<String> styles,
            @RequestParam(required = false) String gender
    ) {
        return ApiResponse.ok(postService.searchPosts(weathers, minTemperature, maxTemperature, minHeight, maxHeight, minWeight, maxWeight, constitution, styles, gender));
    }

    // 게시물 등록
    @PostMapping(value = "/api/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Void> post(@RequestPart PostRequestDto requestDto, HttpServletRequest request) throws IOException {
        postService.post(requestDto, request);
        return ApiResponse.ok();
    }

    // 게시물 좋아요
    @PostMapping("/api/posts/{postId}/likes")
    public ApiResponse<Void> likePost(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long postId) {
        postService.likePost(customUserDetails.getId(), postId);
        return ApiResponse.ok();
    }

    // 게시물 좋아요 취소
    @PostMapping("/api/posts/{postId}/likes/cancel")
    public ApiResponse<Void> cancelLikePost(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long postId) {
        postService.cancelLikePost(customUserDetails.getId(), postId);
        return ApiResponse.ok();
    }
}
