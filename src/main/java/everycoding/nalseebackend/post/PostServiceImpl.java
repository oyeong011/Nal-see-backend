package everycoding.nalseebackend.post;

import everycoding.nalseebackend.aws.S3Service;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import everycoding.nalseebackend.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    public List<PostResponseDto> getPosts(Long lastPostId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").descending());
        return postRepository.findByIdLessThan(lastPostId, pageable)
                .stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .pictureList(post.getPictureList())
                        .likeCnt(post.getLikeCNT())
                        .userId(post.getUser().getId())
                        .picture(post.getUser().getPicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getPostsInLocation(
            double bottomLeftLat, double bottomLeftLong,
            double topRightLat, double topRightLong
    ) {
        List<Post> posts = postRepository.findByLocationWithin(bottomLeftLat, bottomLeftLong, topRightLat, topRightLong);

        return posts.stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .pictureList(post.getPictureList())
                        .likeCnt(post.getLikeCNT())
                        .userId(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .picture(post.getUser().getPicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> searchPosts(String keyword) {
        return null;
    }

    @Override
    public void post(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("photos");

        User user = userRepository.findById(postRequestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("없는 유저입니다."));

        List<String> photos = s3Service.uploadS3(files);

        postRepository.save(
                Post.builder()
                        .pictureList(photos)
                        .content(postRequestDto.getContent())
                        .user(user)
                        .latitude(postRequestDto.getLatitude())
                        .longitude(postRequestDto.getLongitude())
                        .height(postRequestDto.getHeight())
                        .weight(postRequestDto.getWeight())
                        .bodyShape(postRequestDto.getBodyShape())
                        .constitution(postRequestDto.getConstitution())
                        .style(FashionStyle.valueOf(postRequestDto.getStyle()))
                        .gender(Gender.valueOf(postRequestDto.getGender()))
                        .build()
        );
    }

    @Override
    public void likePost(Long userId, Long postId) {

    }


}
