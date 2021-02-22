package pl.flyingoctopus.discord.command;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.HelpAction;
import pl.flyingoctopus.discord.member.action.MemberAddAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MainDiscordCommand {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("^!fo.*");
    private static final String ARGUMENTS_SEPARATOR_REGEX = "\\s";

    private final HelpAction helpAction = new HelpAction("    usage: !fo <command>");
    private final MemberAddAction memberAddAction;

    public Mono<Void> handleMessage(Message message) {
        return Mono.just(message)
                .filter(msg -> validateAuthorIsNotBot(msg) && isMatchingCommandPattern(msg.getContent()))
                .flatMap(msg -> {
                    final var messageArguments = getMessageArgumentsDTO(message, msg);
                    return Flux.just(memberAddAction, helpAction)
                            .filter(action -> action.isMatching(messageArguments))
                            .defaultIfEmpty(helpAction)
                            .distinct()
                            .next()
                            .flatMap(discordAction -> discordAction.run(messageArguments));
                })
                .then();
    }

    private MessageArgumentsDTO getMessageArgumentsDTO(Message message, Message msg) {
        var args = new LinkedList<>(Arrays.asList(msg.getContent().split(ARGUMENTS_SEPARATOR_REGEX)));
        args.remove(0);
        return new MessageArgumentsDTO(message, args);
    }

    private boolean validateAuthorIsNotBot(Message msg) {
        var author = msg.getAuthor();
        return author.map(user -> !user.isBot()).orElse(false);
    }

    private boolean isMatchingCommandPattern(String content) {
        return COMMAND_COMPILED_PATTERN.matcher(content).matches();
    }

}
