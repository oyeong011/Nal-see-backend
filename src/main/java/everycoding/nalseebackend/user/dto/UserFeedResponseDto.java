package everycoding.nalseebackend.user.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserFeedResponseDto {
    private int feedCount;
    private int followingCount;
    private int followerCount;

    private long userId;
    private String userImage;
    private String username;

    private boolean isFollowed;

    @Setter
    private List<Post> postList;

    @Getter
    @AllArgsConstructor
    static public class Post {
        private long postId;
        private String postPicture;

        static public Post fromEntity(everycoding.nalseebackend.post.domain.Post postEntity) {
            return new Post(postEntity.getId(), postEntity.getPictureList().get(0));
        }
    }

    @Builder
    public UserFeedResponseDto(int feedCount, int followingCount, int followerCount, long userId, String userImage, String username, boolean isFollowed) {
        this.feedCount = feedCount;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.userId = userId;
        this.userImage = userImage;
        this.username = username;
        this.isFollowed = isFollowed;
    }
}
