package pl.flyingoctopus.discord.action.help;

import discord4j.core.object.entity.Message;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Mono;

public class DefaultHelpAction implements HelpAction {

    private final String helpMessageResponse;

    public DefaultHelpAction(String helpMessageResponse) {
        this.helpMessageResponse = helpMessageResponse;
    }

    @Override
    public Mono<Message> sendHelpMessage(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helpMessageResponse));
    }
}
