package pl.flyingoctopus.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.flyingoctopus.trello.configuration.TrelloProperties;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrelloMockService implements TrelloService {

    private final TrelloProperties trelloProperties;

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.trello.com/1").build();

    @Override
    public Mono<HttpStatus> addCommentToCard(String cardId, String comment) {
        return webClient.post()
                .uri(getAddCommentToCardUri(cardId, comment))
                .retrieve()
                .toBodilessEntity()
                .map(responseEntity -> responseEntity.getStatusCode())
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ex.getStatusCode()));

    }

    String getAddCommentToCardUri(String cardId, String comment) {
        String key = trelloProperties.getKey();
        String token = trelloProperties.getToken();
        return String.format("/cards/%s/actions/comments?key=%s&token=%s&text=%s", cardId, key, token, comment);
    }

}
