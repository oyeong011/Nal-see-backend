package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.domain.Post;
import everycoding.nalseebackend.user.domain.FashionStyle;
import everycoding.nalseebackend.user.domain.User;
import everycoding.nalseebackend.user.domain.UserInfo;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class PostAlgorithmService {
    private PostRepository postRepository;

    //1차 필터링
    //날짜와 거리로 우선 정렬. 메인 페이지에서 위치 가져와서 위치 기준으로 정렬
//    public List<Post> showPostsFilteredByData(){
//        List<Post> postsNearbyToday = getFilteredPostsBetweenDates()
//                .sorted(Comparator.comparingDouble(post -> ))
//    }

    //1년전 오늘 +- 한달 과 오늘 게시물 반환
    public Stream<Post> getFilteredPostsBetweenDates() {
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1); // 오늘 -1년
        LocalDate oneYearAgoAndWeekAgo = oneYearAgo.minusDays(7); //오늘 - 1년 - 일주일
        LocalDate oneYearAgoAndWeekAfter = oneYearAgo.plusDays(7); // 오늘 - 1년 + 일주일

        LocalDateTime startDateTime = oneYearAgoAndWeekAgo.atStartOfDay();
        LocalDateTime endDateTime = oneYearAgoAndWeekAfter.atTime(23,59,59);

        List<Post> postsYearAgo = postRepository.findByCreateDateBetween(startDateTime, endDateTime);

        LocalDate twoWeeksAgo = today.minusDays(14); //2주 전

        startDateTime = twoWeeksAgo.atStartOfDay();
        endDateTime = LocalDateTime.now();

        List<Post> postsLastTwoWeeks = postRepository.findByCreateDateBetween(startDateTime, endDateTime);

        return Stream.concat(postsYearAgo.stream(), postsLastTwoWeeks.stream());

    }

    //거리 구하는 메서드
//    public double getDistanceBetweenUer(Post post) {
//        double nowLatitude
//    }

    // 2차 정렬
//    public List<Post> sortByAllParams(User user, List<Post> postList) {
//        return postList.stream()
//                .sorted(Comparator.comparingDouble(post -> ))
//    }

    //가산점 점수
//    private double totalScore(Post post, User user, LocalDateTime localDateTime) {
//        double genderScore = genderScore(post, user);
//        double timezoneScore = timezoneScore(post, localDateTime);
//
//    }

    //거리 점수 distanceScore

    //성별 점수
    private double genderScore(Post post, User user) {
        return post.getUserInfo().getGender() == user.getGender() ? 1 : 0;
    }

    //동시간대 점수
    private double timezoneScore(Post post, LocalDateTime localDateTime) {
        int timezoneDifference = Math.abs(post.getCreateDate().getHour() - localDateTime.getHour());
        return timezoneDifference <= 3 ? 5 : 0 ;
    }


    //키 점수
    private double heightScore(Post post, UserInfo userInfo) {
        double heightDifference = Math.abs(post.getUserInfo().getHeight() - userInfo.getHeight());
        return heightDifference <= 5 ? 1 : 0;
    }

    // 몸무게 점수
    private double weightScore (Post post, UserInfo userInfo) {
        double weightDifference = Math.abs(post.getUserInfo().getWeight() - userInfo.getWeight());
        return weightDifference <= 5 ? 1: 0;
    }

    // 체질 점수
    private double constitutionScore(Post post, UserInfo userInfo) {
        return post.getUserInfo().getConstitution() == userInfo.getConstitution() ? 3 : 0;
    }

    // 스타일 점수
//    private double styleScore(Post post, UserInfo userInfo) {
//
//    }





}
