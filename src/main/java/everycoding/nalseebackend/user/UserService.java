package everycoding.nalseebackend.user;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.user.domain.UserInfo;
import everycoding.nalseebackend.user.dto.UserInfoRequestDto;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;

import everycoding.nalseebackend.auth.dto.request.SignupRequestDto;
import everycoding.nalseebackend.auth.exception.EmailAlreadyUsedException;
import everycoding.nalseebackend.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
