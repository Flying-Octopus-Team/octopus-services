package pl.flyingoctopus.trello.service.impl.trellocomment;

import lombok.Data;

@Data
public class TrelloComment {
    private String date;

    private TrelloCommentData data;
}
