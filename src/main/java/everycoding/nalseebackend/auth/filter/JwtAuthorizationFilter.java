package everycoding.nalseebackend.auth.filter;


import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.auth.jwt.JwtProperties;
import everycoding.nalseebackend.auth.jwt.JwtTokenProvider;
import everycoding.nalseebackend.user.UserRepository;
import everycoding.nalseebackend.user.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //
    //    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        String accessToken = null;
//        String refreshToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("RefreshToken".equals(cookie.getName())) {
//                    refreshToken = cookie.getValue();
//                    break;
//                }
//                else if ("AccessToken".equals(cookie.getName())) {
//                    accessToken = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        if (accessToken != null) {
//            String tokenValidationResult = jwtTokenProvider.validateToken(accessToken.replace("Bearer ", ""));
//            if ("token expired".equals(tokenValidationResult)) {
//                log.info("AccessToken is expired");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            } else if (!"Success".equals(tokenValidationResult)) {
//                Authentication authentication = getUsernamePasswordAuthentication(request);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                log.info("is not validate token");
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                return;
//            }
//
//            // 토큰이 유효한 경우 사용자 인증 처리
//            Authentication authentication = getUsernamePasswordAuthentication(request);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } else if (refreshToken != null) {
//            // 리프레시 토큰으로 새 엑세스 토큰 발급
//            log.info("Use Refresh Token And Make New Access Token");
//            String tokenValidationResult = jwtTokenProvider.validateToken(refreshToken.replace("Bearer ", ""));
//            if ("token expired".equals(tokenValidationResult)) {
//                log.info("AccessToken is expired");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//            String AccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
//            Cookie AccessTokenCookie = new Cookie("AccessToken", AccessToken);
//            AccessTokenCookie.setHttpOnly(true);
//            AccessTokenCookie.setPath("/");
//            AccessTokenCookie.setMaxAge(60*2);
//            response.addCookie(AccessTokenCookie);
//            log.info("newAccessToken in Cookie={}", AccessToken);
//
////            response.setHeader("NewAccessToken","Bearer " + newAccessToken); // 새 토큰을 헤더에 추가
////            response.setHeader("expTime", "60");
//
//            //공통 에러 클래스 필요한 이유
//            // 프론트가 정해진 형식을 받아야 된다 (우리만의 오류코드 오류 디스크립션, 다른 메타데이터 등이 들어가야 프론트에서 특정에러를 받았을때 정형화된 형식으로 response를 파싱하고 적절 조치를 취할수 있음)
//            // 리프레시토큰 만료같은 경우는 -> 우리가 내려주는 리스폰스를 우리만의 공통에러 클래스로 지정해서 그 케이스를 별도로 처리 가능
//            // 에러 명세를 만드는 것 ex(6000~7000 기능에러~, 이런식으로)
//        }
//        chain.doFilter(request, response);
//    }
//
//    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
//        String accessToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("AccessToken".equals(cookie.getName())) {
//                    accessToken = cookie.getValue().replace("Bearer ", "");
//                    break;
//                }
//            }
//        }
//
//        Claims claims = jwtTokenProvider.getClaims(accessToken);
//        String email = claims.getSubject();
//
//        log.info(email);
//
//        if(email != null) {
//            Optional<User> oUser = userRepository.findByEmail(email);
//            User user = oUser.get();
//            CustomUserDetails customUserDetails = CustomUserDetails.create(user);
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(), null, customUserDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에 넣기
//            return authentication;
//        }
//        return null;
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                } else if ("AccessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
            }
        }

        boolean tokenProcessed = false;

        if (accessToken != null) {
            String tokenValidationResult = jwtTokenProvider.validateToken(accessToken.replace("Bearer ", ""));
            if ("Success".equals(tokenValidationResult)) {
                // 토큰이 유효한 경우 사용자 인증 처리
                Authentication authentication = getUsernamePasswordAuthentication(accessToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    tokenProcessed = true;
                }
            } else if ("token expired".equals(tokenValidationResult)) {
                // 액세스 토큰이 만료된 경우
                log.info("AccessToken expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "AccessToken expired");
                return;
                }
        }

        if (!tokenProcessed && refreshToken != null) {
            // 액세스 토큰이 처리되지 않았고, 유효한 리프레시 토큰이 있는 경우 새로운 액세스 토큰 발급
            String tokenValidationResult = jwtTokenProvider.validateToken(refreshToken.replace("Bearer ", ""));
            if ("Success".equals(tokenValidationResult)) {
                String newAccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
                Cookie newAccessTokenCookie = new Cookie("AccessToken", newAccessToken);
                newAccessTokenCookie.setHttpOnly(true);
                newAccessTokenCookie.setPath("/");
                // 적절한 만료 시간 설정
                newAccessTokenCookie.setMaxAge((int) JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME / 1000);
                response.addCookie(newAccessTokenCookie);
                log.info("액세스 토큰이 처리되지 않았고, 유효한 리프레시 토큰이 있는 경우 새로운 액세스 토큰 발급");

                // 새로 발급된 액세스 토큰으로 사용자 인증 처리
                Authentication authentication = getUsernamePasswordAuthentication(newAccessToken.replace("Bearer ", ""));
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("새로 발급된 액세스 토큰으로 사용자 인증 처리");
                }
            }

        }

        chain.doFilter(request, response);

    }

    private Authentication getUsernamePasswordAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String email = claims.getSubject();

        if (email != null) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                CustomUserDetails customUserDetails = CustomUserDetails.create(user);
                return new UsernamePasswordAuthenticationToken(customUserDetails.getUsername(), null, customUserDetails.getAuthorities());
            }
        }
        return null;
    }

}