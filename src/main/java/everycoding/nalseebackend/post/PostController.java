package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.post.dto.*;
import everycoding.nalseebackend.user.UserService;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
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
    private final UserService userService;

    // 기본 조회
    @GetMapping("/api/posts")
    public ApiResponse<List<PostScoreDto>> getPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Long lastPostId,
            @RequestParam Double nowLatitude,
            @RequestParam Double nowLongitude
    ) {
        return ApiResponse.ok(postService.getPosts(customUserDetails.getId(), lastPostId, nowLatitude, nowLongitude));
    }

    // 상세 페이지 조회
    @GetMapping("/api/posts/{postId}")
    public ApiResponse<PostForDetailResponseDto> getPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        return ApiResponse.ok(postService.getPost(customUserDetails.getId(), postId));
    }

    // 검색
    @GetMapping("/api/posts/search")
    public ApiResponse<List<PostResponseDto>> searchPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
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
        return ApiResponse.ok(postService.searchPosts(customUserDetails.getId(), weathers, minTemperature, maxTemperature, minHeight, maxHeight, minWeight, maxWeight, constitution, styles, gender));
    }

    // 개인 피드의 게시물 리스트
    @GetMapping("/api/posts/users/{userId}")
    public ApiResponse<List<PostForUserFeedResponseDto>> getPostsForUserFeed(
            @PathVariable Long userId,
            @RequestParam Long lastPostId
    ) {
        return ApiResponse.ok(postService.getPostsForUserFeed(userId, lastPostId));
    }

    // 게시물 등록
    @PostMapping(value = "/api/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Void> post(@RequestPart PostRequestDto requestDto, HttpServletRequest request) throws IOException {
        postService.post(requestDto, request);
        return ApiResponse.ok();
    }

    // 게시물 수정
    @PatchMapping("/api/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto requestDto
    ) {
        postService.updatePost(customUserDetails.getId(), postId, requestDto);
        return ApiResponse.ok();
    }

    //게시물 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId
    ) {
        postService.deletePost(customUserDetails.getId(), postId);
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
