//package everycoding.nalseebackend.post;
//
//import everycoding.nalseebackend.user.domain.Gender;
//import everycoding.nalseebackend.post.PostAlgorithmService;
//import everycoding.nalseebackend.post.domain.Post;
//import everycoding.nalseebackend.user.domain.User;
//import everycoding.nalseebackend.user.domain.UserInfo;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class PostAlgorithmServiceTest {
//
//    PostAlgorithmService service = new PostAlgorithmService();
//    @Test
//    void genderScoreTest() {
//
//        User user = new User();
//        UserInfo userInfo = new UserInfo();
//        userInfo.setGender(Gender.M); // 가정: Gender 열거형을 사용
//        user.setUserInfo(userInfo);
//
//        Post post = new Post();
//        post.setUserInfo(userInfo); // 동일한 성별의 UserInfo 설정
//
//        double score = service.genderScore(post, user);
//        assertEquals(1, score, "성별이 동일할 경우 점수는 1이어야 합니다.");
//    }
//
//    @Test
//    void whenTimezoneDifferenceIsWithinThreeHours_thenScoreShouldBeFive() {
//
//        Post postMock = mock(Post.class);
//        when(postMock.getCreateDate()).thenReturn(LocalDateTime.of(2022, 3, 25, 12, 0));
//
//        // 현재 시간 (동일 시간대 내)
//        LocalDateTime currentTime = LocalDateTime.of(2022, 3, 25, 14, 0); // 2시간 차이
//
//        double score = service.timezoneScore(postMock, currentTime);
//
//        assertEquals(5, score, "시간대 차이가 3시간 이내일 때 점수는 5점이어야 합니다.");
//    }
//
//    @Test
//    void whenUserFollowsPostAuthor_thenScoreShouldBeTwo() {
//        // 모킹된 User 객체와 Post 객체 생성
//        User user = mock(User.class);
//        User author = mock(User.class); // 게시물 작성자
//        Post post = mock(Post.class);
//
//        // 팔로우 관계 설정
//        Set<User> followings = new HashSet<>();
//        followings.add(author);
//        when(user.getFollowings()).thenReturn(followings);
//        when(post.getUser()).thenReturn(author);
//
//        // 테스트 실행
//        double score = service.followingScore(post, user);
//
//        // 검증
//        assertEquals(2, score, "사용자가 게시물 작성자를 팔로우하는 경우 점수는 2점이어야 합니다.");
//    }
//
//}
