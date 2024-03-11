package everycoding.nalseebackend.comment;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.comment.dto.CommentRequestDto;
import everycoding.nalseebackend.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/posts/{postId}/comments")
    public ApiResponse<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ApiResponse.ok(commentService.getComments(postId));
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ApiResponse<Void> writeComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto
    ) {
        commentService.writeComment(postId, requestDto);
        return ApiResponse.ok();
    }

    @GetMapping("/api/posts/{postId}/comment/{commentId}/likes")
    public ApiResponse<Void> likeComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.likeComment(postId, commentId, 1L);
        return ApiResponse.ok();
    }
}
