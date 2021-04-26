package pl.flyingoctopus.trello.service;

import reactor.core.publisher.Mono;

public interface TrelloService {

    Mono<Boolean> addCommentToCard(String cardId, String brief);

}
