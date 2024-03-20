package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.aws.S3Service;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.Constitution;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.Gender;
import everycoding.nalseebackend.user.domain.User;
import everycoding.nalseebackend.weather.dto.WeatherResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private final RestTemplate restTemplate;
    private final PostSpecification postSpecification;

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
                        .address(post.getAddress())
                        .weather(post.getWeather())
                        .temperature(post.getTemperature())
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
                        .address(post.getAddress())
                        .weather(post.getWeather())
                        .temperature(post.getTemperature())
                        .userId(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .userImage(post.getUser().getPicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> searchPosts(List<String> weathers, Double minTemperature, Double maxTemperature, Double minHeight, Double maxHeight,
                                             Double minWeight, Double maxWeight, String constitution, List<String> styles, String gender) {
        Specification<Post> spec = Specification.where(null);

        if (weathers != null) {
            spec = spec.and(postSpecification.hasWeatherIn(weathers));
        }
        spec = spec.and(postSpecification.isTemperatureBetween(minTemperature, maxTemperature));
        spec = spec.and(postSpecification.isHeightBetween(minHeight, maxHeight));
        spec = spec.and(postSpecification.isWeightBetween(minWeight, maxWeight));
        if (constitution != null) {
            spec = spec.and(postSpecification.hasConstitution(constitution));
        }
        if (styles != null) {
            spec = spec.and(postSpecification.hasStyleIn(styles));
        }
        if (gender != null) {
            spec = spec.and(postSpecification.hasGender(gender));
        }

        return postRepository.findAll(spec)
                .stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .pictureList(post.getPictureList())
                        .content(post.getContent())
                        .likeCnt(post.getLikeCNT())
                        .address(post.getAddress())
                        .weather(post.getWeather())
                        .temperature(post.getTemperature())
                        .userId(post.getUser().getId())
                        .username(post.getUser().getUsername())
                        .userImage(post.getUser().getPicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void post(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartHttpServletRequest.getFiles("photos");

        User user = userRepository.findById(postRequestDto.getUserId()).orElseThrow(() -> new BaseException("wrong userId"));

        List<String> photos = s3Service.uploadS3(files);

        WeatherResponseDto weatherResponseDto = getWeather(postRequestDto.getLatitude(), postRequestDto.getLongitude());

        postRepository.save(
                Post.builder()
                        .pictureList(photos)
                        .content(postRequestDto.getContent())
                        .user(user)
                        .weather(weatherResponseDto.getWeather().get(0).getMain())
                        .temperature(Math.ceil((weatherResponseDto.getMain().getTemp()- 273.15)*10)/10.0)
                        .address(postRequestDto.getAddress())
                        .latitude(postRequestDto.getLatitude())
                        .longitude(postRequestDto.getLongitude())
                        .height(postRequestDto.getHeight())
                        .weight(postRequestDto.getWeight())
                        .bodyShape(postRequestDto.getBodyShape())
                        .constitution(Constitution.valueOf(postRequestDto.getConstitution()))
                        .style(FashionStyle.valueOf(postRequestDto.getStyle()))
                        .gender(Gender.valueOf(postRequestDto.getGender()))
                        .build()
        );
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

    private WeatherResponseDto getWeather(double latitude, double longitude) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=7b9d8977d2c3d10d5ae6e4b4b4907c10";
        return restTemplate.getForObject(url, WeatherResponseDto.class);
    }
}
