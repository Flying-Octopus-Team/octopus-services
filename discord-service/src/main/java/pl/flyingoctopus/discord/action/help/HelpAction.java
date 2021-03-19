package pl.flyingoctopus.discord.action.help;

import discord4j.core.object.entity.Message;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public interface HelpAction extends DiscordAction {

    Pattern DEFAULT_COMMAND_COMPILED_PATTERN = Pattern.compile("(help)|(-h)|(--help)|(-help)");

    Mono<Message> sendHelpMessage(MessageArguments messageArguments);

    @Override
    default boolean isMatching(MessageArguments messageArguments) {
        return messageArguments.getArguments().stream()
                .findFirst()
                .map(arg -> getHelpPattern().matcher(arg).matches())
                .orElse(false);
    }

    @Override
    default Mono<Void> run(MessageArguments messageArguments) {
        return sendHelpMessage(messageArguments).then();
    }

    default Pattern getHelpPattern() {
        return DEFAULT_COMMAND_COMPILED_PATTERN;
    }
}
