package pl.flyingoctopus.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrelloMockService implements TrelloService {
    @Override
    public Mono<Boolean> addCommentToCard(String cardId, String brief) {
        return Mono.just(true);
    }

}
