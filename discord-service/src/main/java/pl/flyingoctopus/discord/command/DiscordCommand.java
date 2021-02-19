package pl.flyingoctopus.discord.command;

import discord4j.core.object.entity.Message;
import org.springframework.util.CollectionUtils;
import pl.flyingoctopus.discord.action.DiscordAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Pattern;

public interface DiscordCommand {

    Pattern getCommandPattern();

    DiscordAction getCommandUsageAction();

    Set<DiscordAction> getActions();

    default Mono<Void> handleMessage(Message message) {
        return Mono.just(message)
                .filter(msg -> validateAuthor(msg) && validateCommand(msg.getContent()))
                .flatMap(msg -> {
                    var commandArgument = msg.getContent().split("\\s")[1];
                    return Flux.fromIterable(getActions())
                            .filter(action -> action.getName().equals(commandArgument))
                            .defaultIfEmpty(getCommandUsageAction())
                            .distinct()
                            .next()
                            .flatMap(discordAction -> discordAction.run(message));
                })
                .then();
    }

    default boolean validateAuthor(Message msg) {
        return msg.getAuthor().map(user -> !user.isBot()).orElse(false);
    }

    default boolean validateCommand(String content) {
        return isMatchingCommandPattern(content) && hasActionArgumentIfCommandHasActions(content);
    }

    default boolean isMatchingCommandPattern(String content) {
        return getCommandPattern().matcher(content).matches();
    }

    default boolean hasActionArgumentIfCommandHasActions(String content) {
        return CollectionUtils.isEmpty(getActions()) || content.split("\\s").length > 1;
    }

}
