package everycoding.nalseebackend.comment;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.comment.dto.CommentRequestDto;
import everycoding.nalseebackend.comment.dto.CommentResponseDto;
import everycoding.nalseebackend.post.PostRepository;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentResponseDto> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));
        return commentRepository.findAllByPost(post)
                .stream()
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .likeCNT(comment.getLikeCNT())
                        .userId(comment.getUser().getId())
                        .userPicture(comment.getUser().getPicture())
                        .username(comment.getUser().getUsername())
                        .postId(comment.getPost().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void writeComment(Long postId, CommentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new BaseException("wrong userId"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));

        commentRepository.save(
                Comment.builder()
                        .content(requestDto.getContent())
                        .user(user)
                        .post(post)
                        .build());
    }

    @Override
    public void likeComment(Long postId, Long commentId, Long userId) {

    }
}
