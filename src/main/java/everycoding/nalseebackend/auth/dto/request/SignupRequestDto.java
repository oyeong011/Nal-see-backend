package everycoding.nalseebackend.auth.dto.request;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String email;
    private String username;
    private String password;

    public SignupRequestDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
