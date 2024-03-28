package everycoding.nalseebackend.auth.dto.request;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String picture;
    private boolean newUser;

    public UserResponse(Long id, String username, String email, String picture, boolean newUser) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.newUser = newUser;
    }
}
