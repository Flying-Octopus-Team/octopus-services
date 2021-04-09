package pl.flyingoctopus.discord.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.DefaultHelpAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.configure.action.ConfigureAction;
import pl.flyingoctopus.discord.configure.service.ConfigurationService;
import pl.flyingoctopus.discord.links.LinksAction;
import pl.flyingoctopus.discord.member.action.MemberCommand;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainDiscordCommand implements DiscordCommand {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("^!fo.*");
    private static final String ARGUMENTS_SEPARATOR_REGEX = "\\s";

    private final DefaultHelpAction helpAction = new DefaultHelpAction("    usage: !fo <command>");

    private final ConfigurationService configurationService;
    private final MemberCommand memberCommand;
    private final LinksAction linksAction;
    private final ConfigureAction configureAction;

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
        return Set.of(memberCommand, linksAction, helpAction, configureAction);
    }

    @Override
    public boolean isAuthorised(MessageArguments messageArguments) {
        return validateAuthorIsNotBot(messageArguments.getMessage());
    }

    public Mono<Void> handleMessage(MessageCreateEvent event) {
        return Mono.just(event)
                .filter(ev -> isMatchingCommandPattern(ev.getMessage().getContent()))
                .flatMap(this::getMessageArguments)
                .flatMap(this::run)
                .then();
    }

    private Mono<MessageArguments> getMessageArguments(MessageCreateEvent messageCreateEvent) {
        var message = messageCreateEvent.getMessage();
        var arguments = new LinkedList<>(Arrays.asList(message.getContent().split(ARGUMENTS_SEPARATOR_REGEX)));
        return messageCreateEvent.getMember()
                .map(messageMember -> getMessageArgumentsWithMemberRole(message, messageMember, arguments))
                .orElseGet(() -> Mono.just(new MessageArguments(message, arguments, false)));
    }

    private Mono<MessageArguments> getMessageArgumentsWithMemberRole(Message message, Member member, LinkedList<String> arguments) {
        return configurationService
                .getMemberRoleId()
                .map(memberRoleId -> member.getRoleIds().contains(memberRoleId))
                .defaultIfEmpty(false)
                .map(hasMemberRole -> new MessageArguments(message, arguments, hasMemberRole));
    }

    private boolean validateAuthorIsNotBot(Message msg) {
        var author = msg.getAuthor();
        return author.map(user -> !user.isBot()).orElse(false);
    }

    private boolean isMatchingCommandPattern(String content) {
        return COMMAND_COMPILED_PATTERN.matcher(content).matches();
    }
}
