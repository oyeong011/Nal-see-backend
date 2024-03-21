package everycoding.nalseebackend.user;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.post.PostRepository;
import everycoding.nalseebackend.user.domain.UserInfo;
import everycoding.nalseebackend.user.dto.UserFeedResponseDto;
import everycoding.nalseebackend.user.dto.UserInfoRequestDto;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;

import everycoding.nalseebackend.auth.dto.request.SignupRequestDto;
import everycoding.nalseebackend.auth.exception.EmailAlreadyUsedException;
import everycoding.nalseebackend.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void followUser(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        User me = userRepository.findById(myId).orElseThrow(() -> new BaseException("wrong userId"));

        me.follow(user);

        userRepository.save(me);
    }

    public void unfollowUser(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        User me = userRepository.findById(myId).orElseThrow(() -> new BaseException("wrong userId"));

        me.unfollow(user);

        userRepository.save(me);
    }

    public UserInfoResponseDto getUserInfo(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        return UserInfoResponseDto.builder()
                .height(user.getUserInfo().getHeight())
                .weight(user.getUserInfo().getWeight())
                .constitution(user.getUserInfo().getConstitution())
                .style(user.getUserInfo().getStyle())
                .gender(user.getUserInfo().getGender())
                .build();
    }

    public void setUserInfo(long userId, UserInfoRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        user.setUserInfo(
                UserInfo.builder()
                .height(requestDto.getHeight())
                .weight(requestDto.getWeight())
                .constitution(requestDto.getConstitution())
                .style(requestDto.getStyle())
                .gender(requestDto.getGender())
                .build()
        );
        userRepository.save(user);
    }

    public UserFeedResponseDto getMyFeed(long userId, long lastPostId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());

        UserFeedResponseDto responseDto = UserFeedResponseDto.builder()
                .feedCount(user.getPosts().size())
                .followingCount(user.getFollowings().size())
                .followerCount(user.getFollowers().size())
                .userId(user.getId())
                .userImage(user.getPicture())
                .username(user.getUsername())
                .build();

        responseDto.setPostList(
                postRepository.findByUserAndIdLessThan(user, lastPostId, pageable)
                        .stream()
                        .map(UserFeedResponseDto.Post::fromEntity)
                        .collect(Collectors.toList())
        );

        return responseDto;
    }

    public UserFeedResponseDto getFeed(long myId, long userId, long lastPostId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        User me = userRepository.findById(myId).orElseThrow(() -> new BaseException("wrong userId"));
        Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());

        UserFeedResponseDto responseDto = UserFeedResponseDto.builder()
                .feedCount(user.getPosts().size())
                .followingCount(user.getFollowings().size())
                .followerCount(user.getFollowers().size())
                .userId(user.getId())
                .userImage(user.getPicture())
                .username(user.getUsername())
                .isFollowed(user.getFollowers().contains(me))
                .build();

        responseDto.setPostList(
                postRepository.findByUserAndIdLessThan(user, lastPostId, pageable)
                        .stream()
                        .map(UserFeedResponseDto.Post::fromEntity)
                        .collect(Collectors.toList())
        );

        return responseDto;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void signUpUser(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);

        log.info("회원가입 완료");
    }
}
