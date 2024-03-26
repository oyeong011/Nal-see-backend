package everycoding.nalseebackend.post;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.aws.S3Service;
import everycoding.nalseebackend.comment.CommentService;
import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.post.dto.PostForDetailResponseDto;
import everycoding.nalseebackend.post.dto.PostForUserFeedResponseDto;
import everycoding.nalseebackend.post.dto.PostRequestDto;
import everycoding.nalseebackend.post.dto.PostResponseDto;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.*;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
import everycoding.nalseebackend.weather.Weather;
import everycoding.nalseebackend.weather.dto.WeatherResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final CommentService commentService;
    private final RestTemplate restTemplate;
    private final PostSpecification postSpecification;

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts(Long userId, Long lastPostId, Double nowLatitude, Double nowLongitude) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        return postRepository.findByIdLessThan(lastPostId!=-1 ? lastPostId : Long.MAX_VALUE, pageable)
                .stream()
                .map(post -> PostResponseDto.createPostResponseDto(post, isLiked(userId, post.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsInLocation(
            Long userId,
            double bottomLeftLat, double bottomLeftLong,
            double topRightLat, double topRightLong
    ) {
        List<Post> posts = postRepository.findByLocationWithin(bottomLeftLat, bottomLeftLong, topRightLat, topRightLong);

        return posts.stream()
                .map(post -> PostResponseDto.createPostResponseDto(post, isLiked(userId, post.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostForDetailResponseDto getPost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));
        return new PostForDetailResponseDto(
                PostResponseDto.createPostResponseDto(post, isLiked(userId, post.getId())),
                commentService.getComments(postId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> searchPosts(Long userId, List<String> weathers, Double minTemperature, Double maxTemperature, Double minHeight, Double maxHeight,
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
                .map(post -> PostResponseDto.createPostResponseDto(post, isLiked(userId, post.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostForUserFeedResponseDto> getPostsForUserFeed(Long userId, Long lastPostId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));

        Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());

        return postRepository.findByUserAndIdLessThan(user, lastPostId!=-1 ? lastPostId : Long.MAX_VALUE, pageable)
                .stream()
                .map(post -> PostForUserFeedResponseDto.builder()
                        .postId(post.getId())
                        .postPicture(post.getPictureList().get(0))
                        .isMany(post.getPictureList().size()>1)
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
        UserInfo userInfo = UserInfo.builder()
                .height(postRequestDto.getUserInfo().getHeight())
                .weight(postRequestDto.getUserInfo().getWeight())
                .constitution(postRequestDto.getUserInfo().getConstitution())
                .style(postRequestDto.getUserInfo().getStyle())
                .gender(postRequestDto.getUserInfo().getGender())
                .build();

        postRepository.save(
                Post.builder()
                        .pictureList(photos)
                        .content(postRequestDto.getContent())
                        .user(user)
                        .weather(Weather.valueOf(weatherResponseDto.getWeather().get(0).getMain()))
                        .temperature(Math.ceil((weatherResponseDto.getMain().getTemp() - 273.15) * 10) / 10.0)
                        .address(postRequestDto.getAddress())
                        .latitude(postRequestDto.getLatitude())
                        .longitude(postRequestDto.getLongitude())
                        .userInfo(userInfo)
                        .build()
        );
    }

    @Override
    public void updatePost(Long userId, Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));

        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException("수정할 수 있는 권한이 없습니다.");
        }

        if (postRequestDto.getContent() != null) {
            post.setContent(postRequestDto.getContent());
        }
        if (postRequestDto.getAddress() != null) {
            post.setAddress(postRequestDto.getAddress());
        }
        if (postRequestDto.getLatitude() != null) {
            post.setLatitude(postRequestDto.getLatitude());
            post.setLongitude(postRequestDto.getLongitude());
        }

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException("wrong postId"));
        if (!post.getUser().getId().equals(userId)) {
            throw new BaseException("삭제할 수 있는 권한이 없습니다.");
        }
        post.getPictureList().forEach(s3Service::deleteS3);
        postRepository.delete(post);
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

    private boolean isLiked(long userId, long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        return user.getPostLikeList().contains(postId);
    }
}
