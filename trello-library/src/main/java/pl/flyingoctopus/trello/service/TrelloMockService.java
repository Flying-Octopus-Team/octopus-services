package pl.flyingoctopus.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrelloMockService implements TrelloService {

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.trello.com/1").build();
    private final String key = "TEMPORARY_CONSTANT";
    private final String token = "TEMPORARY_CONSTANT";

    @Override
    public Mono<HttpStatus> addCommentToCard(String cardId, String brief) {
        return webClient.post()
                .uri(String.format("/cards/%s/actions/comments?key=%s&token=%s&text=%s", cardId, key, token, brief))
                .retrieve()
                .toBodilessEntity()
                .map(responseEntity -> responseEntity.getStatusCode())
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ex.getStatusCode()));

    }

}
