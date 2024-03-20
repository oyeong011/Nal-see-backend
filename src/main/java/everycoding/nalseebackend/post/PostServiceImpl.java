package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.aws.S3Service;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import everycoding.nalseebackend.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                        .content(post.getContent())
                        .likeCnt(post.getLikeCNT())
                        .createDate(post.getCreateDate())
                        .userId(post.getUser().getId())
                        .userImage(post.getUser().getPicture())
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
                        .content(post.getContent())
                        .likeCnt(post.getLikeCNT())
                        .userId(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .userImage(post.getUser().getPicture())
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

        User user = userRepository.findById(postRequestDto.getUserId()).orElseThrow(() -> new BaseException("wrong userId"));

        List<String> photos = s3Service.uploadS3(files);

        Post post = postRepository.save(
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

//        getWeather(post.getCreateDate(), post.getLatitude(), post.getLongitude());
    }

    @Override
    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));
        System.out.println("userId:" + user.getId());
        System.out.println("postId:" + post.getId());

        user.addPostLike(postId);
        post.increaseLikeCNT();

        userRepository.save(user);
        postRepository.save(post);
    }

    @Override
    public void cancelLikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));

        user.cancelPostLike(postId);
        post.decreaseLikeCNT();

        userRepository.save(user);
        postRepository.save(post);
    }

    private void getWeather(LocalDateTime localDateTime, double latitude, double longitude) {
        try {
            long epoch = localDateTime.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond();

            URL url = new URL("https://apihub.kma.go.kr/api/typ01/url/kma_sfctm2.php?tm=" + epoch + "&stn=1&authKey=M0At3_T7SteALd_0--rXPw");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            System.out.println("11");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "EUC-KR"));
            String inputLine;
            StringBuffer response = new StringBuffer();
            System.out.println("22");

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                System.out.println(inputLine);
            }

            br.close();

            System.out.println(response.toString());
        } catch (IOException e) {
            throw new BaseException("날씨를 불러오지 못하였습니다.");
        }

    }


}
