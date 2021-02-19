package pl.flyingoctopus.discord.action;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class HelpAction implements DiscordAction {

    private static final String ACTION_NAME = "help";

    private final String helpMessageResponse;

    @Override
    public String getName() {
        return ACTION_NAME;
    }

    @Override
    public Mono<Void> run(Message message) {
        return Mono.just(message)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helpMessageResponse))
                .then();
    }
}
