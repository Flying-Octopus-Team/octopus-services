package pl.flyingoctopus.discord.action;

import org.springframework.util.CollectionUtils;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Mono;

public interface DiscordAction {

    boolean isMatching(MessageArguments messageArguments);

    Mono<Void> run(MessageArguments messageArguments);

    default void removeFirstArgument(MessageArguments messageArguments) {
        if (CollectionUtils.isEmpty(messageArguments.getArguments())) {
            throw new RuntimeException("Trying to remove first argument when no arguments given");
        }
        messageArguments.getArguments().remove(0);
    }
}
