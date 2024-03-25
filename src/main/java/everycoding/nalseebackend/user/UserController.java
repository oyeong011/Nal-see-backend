package everycoding.nalseebackend.user;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.user.dto.UserFeedResponseDto;
import everycoding.nalseebackend.user.dto.UserInfoRequestDto;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 팔로우
    @PostMapping("/api/users/{userId}/follow")
    public ApiResponse<Void> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.followUser(userId, customUserDetails.getId());
        return ApiResponse.ok();
    }

    // 언팔로우
    @PostMapping("/api/users/{userId}/unfollow")
    public ApiResponse<Void> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.unfollowUser(userId, customUserDetails.getId());
        return ApiResponse.ok();
    }

    // 유저 개인정보 조회
    @GetMapping("/api/users/userInfo")
    public ApiResponse<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.ok(userService.getUserInfo(customUserDetails.getId()));
    }

    // 유저 개인정보 등록
    @PostMapping("/api/users/userInfo")
    public ApiResponse<Void> setUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody UserInfoRequestDto requestDto
    ) {
        userService.setUserInfo(customUserDetails.getId(), requestDto);
        return ApiResponse.ok();
    }

    // 개인 피드 페이지
    @GetMapping("/api/users/{userId}/feed")
    public ApiResponse<UserFeedResponseDto> getFeed(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long userId
    ) {
        return ApiResponse.ok(userService.getFeed(customUserDetails.getId(), userId));
    }
}
