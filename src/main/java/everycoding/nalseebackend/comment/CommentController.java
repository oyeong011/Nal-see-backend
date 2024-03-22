package everycoding.nalseebackend.comment;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.comment.dto.CommentRequestDto;
import everycoding.nalseebackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/api/posts/{postId}/comments")
    public ApiResponse<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ApiResponse.ok(commentService.getComments(postId));
    }

    // 댓글 작성
    @PostMapping("/api/posts/{postId}/comments")
    public ApiResponse<Void> writeComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto
    ) {
        commentService.writeComment(postId, requestDto);
        return ApiResponse.ok();
    }

    // 댓글 수정
    @PatchMapping("/api/posts/{postId}/comments/{commentId}")
    public ApiResponse<Void> updateComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto
    ) {
        commentService.updateComment(customUserDetails.getId(), postId, commentId, requestDto);
        return ApiResponse.ok();
    }

    // 댓글 삭제
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ApiResponse<Void> deleteComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(customUserDetails.getId(), postId, commentId);
        return ApiResponse.ok();
    }

    // 댓글 좋아요
    @PostMapping("/api/posts/{postId}/comment/{commentId}/likes")
    public ApiResponse<Void> likeComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId
            ) {
        commentService.likeComment(customUserDetails.getId(), postId, commentId);
        return ApiResponse.ok();
    }

    // 댓글 좋아요 취소
    @PostMapping("/api/posts/{postId}/comment/{commentId}/likes/cancel")
    public ApiResponse<Void> cancelLikeComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.cancelLikeComment(customUserDetails.getId(), postId, commentId);
        return ApiResponse.ok();
    }
}
