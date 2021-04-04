package pl.flyingoctopus.discord.command;

import org.springframework.util.CollectionUtils;
import pl.flyingoctopus.discord.action.AuthorisedAction;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Discord Commands are supposed to be a form of control flow that call other actions
 * e.g. member command can switch into member add or member status actions (!fo member <add|status>)
 */
public interface DiscordCommand extends AuthorisedAction {

    Pattern getCommandPattern();

    DiscordAction getHelpAction();

    Set<DiscordAction> getActions();

    @Override
    default boolean isMatching(MessageArguments messageArguments) {
        if (CollectionUtils.isEmpty(messageArguments.getArguments())) {
            return false;
        }
        return getCommandPattern().matcher(messageArguments.getArguments().get(0)).matches();
    }

    @Override
    default Mono<Void> run(MessageArguments messageArguments) {
        removeFirstArgument(messageArguments);
        return Flux.fromIterable(getActions())
                .filter(action -> isAuthorised(messageArguments))
                .filter(action -> action.isMatching(messageArguments))
                .defaultIfEmpty(getHelpAction())
                .distinct()
                .next()
                .flatMap(discordAction -> discordAction.run(messageArguments));
    }
}
