package pl.flyingoctopus.discord.command;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.DefaultHelpAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.links.LinksAction;
import pl.flyingoctopus.discord.member.action.MemberCommand;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MainDiscordCommand implements DiscordCommand {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("^!fo.*");
    private static final String ARGUMENTS_SEPARATOR_REGEX = "\\s";

    private final DefaultHelpAction helpAction = new DefaultHelpAction("    usage: !fo <command>");
    private final MemberCommand memberCommand;
    private final LinksAction linksAction;

    @Override
    public Pattern getCommandPattern() {
        return COMMAND_COMPILED_PATTERN;
    }

    @Override
    public DiscordAction getHelpAction() {
        return helpAction;
    }

    @Override
    public Set<DiscordAction> getActions() {
        return Set.of(memberCommand, linksAction, helpAction);
    }

    public Mono<Void> handleMessage(Message message) {
        return Mono.just(message)
                .filter(msg -> validateAuthorIsNotBot(msg) && isMatchingCommandPattern(msg.getContent()))
                .flatMap(msg -> this.run(getMessageArgumentsDTO(message, msg)))
                .then();
    }

    private MessageArguments getMessageArgumentsDTO(Message message, Message msg) {
        var args = new LinkedList<>(Arrays.asList(msg.getContent().split(ARGUMENTS_SEPARATOR_REGEX)));
        return new MessageArguments(message, args);
    }

    private boolean validateAuthorIsNotBot(Message msg) {
        var author = msg.getAuthor();
        return author.map(user -> !user.isBot()).orElse(false);
    }

    private boolean isMatchingCommandPattern(String content) {
        return COMMAND_COMPILED_PATTERN.matcher(content).matches();
    }
}
