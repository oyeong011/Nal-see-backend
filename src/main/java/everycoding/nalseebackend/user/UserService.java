package everycoding.nalseebackend.user;

import everycoding.nalseebackend.api.exception.BaseException;
import everycoding.nalseebackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void followUser(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        User me = userRepository.findById(myId).orElseThrow(() -> new BaseException("wrong userId"));

        me.follow(user);
    }

    public void unfollowUser(Long userId, Long myId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("wrong userId"));
        User me = userRepository.findById(myId).orElseThrow(() -> new BaseException("wrong userId"));

        me.unfollow(user);
    }
}
