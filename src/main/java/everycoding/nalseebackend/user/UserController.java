package everycoding.nalseebackend.user;

import everycoding.nalseebackend.api.ApiResponse;
import everycoding.nalseebackend.auth.customUser.CustomUserDetails;
import everycoding.nalseebackend.user.dto.UserInfoRequestDto;
import everycoding.nalseebackend.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users/{userId}/follow")
    public ApiResponse<Void> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.followUser(userId, customUserDetails.getId());
        return ApiResponse.ok();
    }

    @PostMapping("/api/users/{userId}/unfollow")
    public ApiResponse<Void> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        userService.unfollowUser(userId, customUserDetails.getId());
        return ApiResponse.ok();
    }

    @GetMapping("/api/users/userInfo")
    public ApiResponse<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.ok(userService.getUserInfo(customUserDetails.getId()));
    }

    @PostMapping("/api/users/userInfo")
    public ApiResponse<Void> setUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody UserInfoRequestDto requestDto
            ) {
        userService.setUserInfo(customUserDetails.getId(), requestDto);
        return ApiResponse.ok();
    }
}
