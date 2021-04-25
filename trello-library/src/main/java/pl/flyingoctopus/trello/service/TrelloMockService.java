package pl.flyingoctopus.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrelloMockService implements TrelloService {
    @Override
    public Mono<Void> addCommentToCard(String brief) {
        return Mono.empty();
    }

    @Override
    public String getReportCardId() {
        return "1234";
    }
}
