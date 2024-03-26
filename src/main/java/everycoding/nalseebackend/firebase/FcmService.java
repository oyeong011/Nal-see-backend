package everycoding.nalseebackend.firebase;

import everycoding.nalseebackend.firebase.dto.FcmSendDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface FcmService {

    int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;

}