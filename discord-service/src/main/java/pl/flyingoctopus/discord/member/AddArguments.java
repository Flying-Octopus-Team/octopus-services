package pl.flyingoctopus.discord.member;

import lombok.Builder;
import lombok.Getter;
import pl.flyingoctopus.discord.arguments.MessageArguments;

@Getter
public class AddArguments extends MessageArguments {

    private final String userMention;
    private final String memberEmail;
    private final String trelloEmail;
    private final String wikiEmail;

    public AddArguments(MessageArguments messageArguments) {
        super(messageArguments.getMessage(), messageArguments.getArguments(), messageArguments.hasMemberRole());
        this.userMention = null;
        this.memberEmail = null;
        this.trelloEmail = null;
        this.wikiEmail = null;
    }

    @Builder
    public AddArguments(MessageArguments messageArguments, String userMention, String memberEmail, String trelloEmail, String wikiEmail) {
        super(messageArguments.getMessage(), messageArguments.getArguments(), messageArguments.hasMemberRole());
        this.userMention = userMention;
        this.memberEmail = memberEmail;
        this.trelloEmail = trelloEmail;
        this.wikiEmail = wikiEmail;
    }
}
