package pl.flyingoctopus.trello.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.flyingoctopus.trello.configuration.TrelloProperties;
import pl.flyingoctopus.trello.service.TrelloService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrelloServiceImpl implements TrelloService {

    private final TrelloProperties trelloProperties;
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.trello.com/1").build();

    @Override
    public Mono<HttpStatus> addCommentToCard(String cardId, String comment) {
        return webClient.post()
                .uri(getAddCommentToCardUri(cardId, comment))
                .retrieve()
                .toBodilessEntity()
                .map(ResponseEntity::getStatusCode)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ex.getStatusCode()));
    }

    private String getAddCommentToCardUri(String cardId, String comment) {
        String key = trelloProperties.getKey();
        String token = trelloProperties.getToken();
        return String.format("/cards/%s/actions/comments?key=%s&token=%s&text=%s", cardId, key, token, comment);
    }

    @Override
    public Mono<HttpStatus> inviteToWorkspace(String email, String name) {
        return webClient.put()
                .uri(getInviteToWorkspaceUri(email, name))
                .retrieve()
                .toBodilessEntity()
                .map(ResponseEntity::getStatusCode)
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(ex.getStatusCode()));
    }

    private String getInviteToWorkspaceUri(String email, String name) {
        String key = trelloProperties.getKey();
        String token = trelloProperties.getToken();
        String workspaceId = trelloProperties.getWorkspaceId();
        return String.format("/organizations/%s/members?key=%s&token=%s&email=%s&fullName=%s", workspaceId, key, token, email, name);
    }

}
