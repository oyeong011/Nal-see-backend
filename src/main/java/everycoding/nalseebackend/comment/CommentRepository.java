package everycoding.nalseebackend.comment;

import everycoding.nalseebackend.comment.domain.Comment;
import everycoding.nalseebackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
}
