package everycoding.nalseebackend.webclient;

import everycoding.nalseebackend.auth.jwt.JwtTokenProvider;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.User;
import everycoding.nalseebackend.webclient.dto.UserInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @GetMapping("/user/info")
    public UserInfo getUser(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.info("token={}", authorization);
        Claims claims = jwtTokenProvider.getClaims(authorization);
        String subject = claims.getSubject();
        Optional<User> byEmail = userRepository.findByEmail(subject);
        User user = byEmail.orElseThrow();

        return UserInfo.builder()
                .userName(user.getUsername())
                .userId(user.getId())
                .userImg(user.getPicture())
                .build();
    }

    @GetMapping("/user/exist")
    public UserInfo checkUserExistence(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return UserInfo.builder()
                .userName(user.getUsername())
                .userId(user.getId())
                .userImg(user.getPicture())
                .build();
    }
}
