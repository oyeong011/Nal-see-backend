package everycoding.nalseebackend.post;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import everycoding.nalseebackend.api.exception.BaseException;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;

    private final String bucket = "nalsee-post-photos";

    @Override
    public List<PostResponseDto> getPosts(int lastPostId, int size) {
        Pageable pageable = PageRequest.of(lastPostId, size, Sort.by("id").descending());
        return postRepository.findAll(pageable)
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
    public List<PostResponseDto> getPostsInLocation(double latitude, double longitude) {
        return null;
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

        List<String> photos = uploadS3(files);

        postRepository.save(Post.builder()
                .pictureList(photos)
                .content(postRequestDto.getContent())
                .user(user)
                .longitude(postRequestDto.getLongitude())
                .latitude(postRequestDto.getLatitude())
                .height(postRequestDto.getHeight())
                .weight(postRequestDto.getWeight())
                .bodyShape(postRequestDto.getBodyShape())
                .constitution(postRequestDto.getConstitution())
                .style(FashionStyle.valueOf(postRequestDto.getStyle()))
                .gender(Gender.valueOf(postRequestDto.getGender()))
                .build());
    }

    @Override
    public void likePost(Long userId, Long postId) {

    }

    private List<String> uploadS3(List<MultipartFile> files) throws IOException {
        List<String> photos = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            File file = convertMultipartFileToFile(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> file convert fail"));

            String key = "post-photos/" + UUID.randomUUID();

            try {
                amazonS3.putObject(new PutObjectRequest(bucket, key, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (Exception e) {
                throw new BaseException("사진 업로드 실패");
            } finally {
                file.delete();
            }

            photos.add(getS3(key));
        }
        return photos;
    }

    private String getS3(String key) {
        return amazonS3.getUrl(bucket, key).toString();
    }

    public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)){
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }
}
