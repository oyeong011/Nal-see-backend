package everycoding.nalseebackend.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/index")
    public String hello() {
        return "hello";
    }

}
