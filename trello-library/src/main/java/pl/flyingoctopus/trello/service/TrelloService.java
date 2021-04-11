package pl.flyingoctopus.trello.service;

public interface TrelloService {

    default boolean addCommenttoCard(String trelloToken, String cardId, String brief) {
        return true;
    }

    default String getReportCardId(String trelloToken) {
        return "1234";
    }

}
