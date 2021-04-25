package pl.flyingoctopus.trello.service;

import reactor.core.publisher.Mono;

public interface TrelloService {

    public Mono<Void> addCommentToCard(String brief);

    public String getReportCardId();

}
