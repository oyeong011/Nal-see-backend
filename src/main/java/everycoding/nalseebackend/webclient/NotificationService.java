package everycoding.nalseebackend.webclient;

import everycoding.nalseebackend.webclient.dto.EventDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final WebClient webClient;

    public NotificationService(WebClient.Builder webclientBuilder) {
        this.webClient = webclientBuilder.baseUrl("http://localhost:8090/").build();
    }

    public Mono<Void> sentEvent(String message, Long receiverId, Long senderId, LocalDateTime createAt, String senderImage) {
        return webClient.post()
                .uri("/events")
                .bodyValue(EventDto.builder()
                        .message(message)
                        .receiverId(receiverId)
                        .senderId(senderId)
                        .createAt(createAt)
                        .senderImage(senderImage))
                .retrieve()
                .bodyToMono(Void.class);
    }

}
