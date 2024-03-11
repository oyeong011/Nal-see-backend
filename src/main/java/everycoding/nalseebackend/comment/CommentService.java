package everycoding.nalseebackend.comment;

import everycoding.nalseebackend.comment.dto.CommentRequestDto;
import everycoding.nalseebackend.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getComments(Long postId);

    void writeComment(Long postId, CommentRequestDto requestDto);

    void likeComment(Long postId, Long commentId, Long userId);
}
