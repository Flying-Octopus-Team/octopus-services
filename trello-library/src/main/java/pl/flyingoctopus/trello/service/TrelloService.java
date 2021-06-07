package pl.flyingoctopus.trello.service;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

public interface TrelloService {

    Mono<HttpStatus> addCommentToCard(String cardId, String brief);

    Mono<String> getCommentsFromCard(String cardId);

}
