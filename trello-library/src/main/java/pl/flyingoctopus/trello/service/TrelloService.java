package pl.flyingoctopus.trello.service;

public interface TrelloService {

    default boolean addCommenttoCard(String cardId, String brief) {
        return true;
    }

    default String getReportCardId() {
        return "1234";
    }

}
