package pl.flyingoctopus.discord.action;

import discord4j.core.object.entity.Message;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public class HelpAction implements DiscordAction {

    private static final Pattern DEFAULT_COMMAND_COMPILED_PATTERN = Pattern.compile("(help)|(-h)|(--help)|(-help)");

    private final Pattern helpPattern;
    private final String helpMessageResponse;

    public HelpAction(String helpMessageResponse) {
        this.helpMessageResponse = helpMessageResponse;
        helpPattern = DEFAULT_COMMAND_COMPILED_PATTERN;
    }

    @Override
    public boolean isMatching(MessageArguments messageArguments) {
        return messageArguments.getArguments().stream()
                .findFirst()
                .map(arg -> helpPattern.matcher(arg).matches())
                .orElse(false);
    }

    @Override
    public Mono<Void> run(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helpMessageResponse))
                .then();
    }

    public Mono<Message> sendHelpMessage(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helpMessageResponse));
    }
}
