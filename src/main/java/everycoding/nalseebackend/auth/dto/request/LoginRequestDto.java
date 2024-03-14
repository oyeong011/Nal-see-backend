package everycoding.nalseebackend.auth.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    public String email; //이메일
    public String password;

}
