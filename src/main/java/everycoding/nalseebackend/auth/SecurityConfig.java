package everycoding.nalseebackend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import everycoding.nalseebackend.auth.jwt.JwtTokenProvider;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder pwEnc() {return new BCryptPasswordEncoder();}


    public void AthenScs(HttpServletRequest req, HttpServletResponse res, Authentication athen) throws IOException {
        log.info("OAuth Login Success :)");
        // 토큰 발급 시작
        String accTk = jwtTokenProvider.createAccessToken(athen);
        String refTk = jwtTokenProvider.createRefreshToken(athen);
        log.info("test={}", athen.getName());
        log.info("test={}", athen.getAuthorities());
        log.info("test={}", athen.getDetails());
        log.info("test={}", athen.getClass());
        log.info("test={}", athen.getPrincipal());

        log.info(accTk);
        log.info(refTk);
        ObjectMapper om = new ObjectMapper(); // JSON 처리를 위한 데이터 바인딩 기능

        res.addHeader("Authorization", "Bearer " + accTk);
        log.info("AccessToken in Header={}", accTk);
        log.info("header={}", res.getHeader("Authorization"));

        Cookie refTkCookie = new Cookie("RefreshToken", refTk);
        refTkCookie.setHttpOnly(true);
        refTkCookie.setPath("/");
        refTkCookie.setMaxAge(60 * 60 * 24 * 7); // 리프레시 토큰 유효기간
        res.addCookie(refTkCookie);
        log.info("RefreshToken in ={}", refTk);

        String role = athen.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findAny().orElse(""); // role 변수에 권한 부여되면 그 중 첫번째 문자열로 저장, 권한 없으면 빈문자열 출력
        String usreml = "";
        if(role.equals("ROLE_USER")){
            User user = (User) athen.getPrincipal();
            usreml = user.getEmail();
        }

    }


    public void onAthenFail (HttpServletRequest req, HttpServletResponse res, AuthenticationException excp) throws IOException {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Bean
    public CorsConfigurationSource crsCfgSource() {
        CorsConfiguration crsCfg = new CorsConfiguration();

        crsCfg.addAllowedHeader("*");
        crsCfg.addExposedHeader("*");
        crsCfg.setAllowCredentials(true);

        crsCfg.setAllowedOrigins(List.of("https://ec2-43-203-106-91.ap-northeast-2.compute.amazonaws.com:8080"));

        crsCfg.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", crsCfg);
        return source;
    }

}
