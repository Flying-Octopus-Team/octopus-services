package pl.flyingoctopus.discord.action;

import discord4j.core.object.entity.Message;
import pl.flyingoctopus.discord.command.MessageArgumentsDTO;
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

    public HelpAction(String helpMessageResponse, Pattern helpPattern) {
        this.helpMessageResponse = helpMessageResponse;
        this.helpPattern = helpPattern;
    }

    @Override
    public boolean isMatching(MessageArgumentsDTO messageArgumentsDTO) {
        return messageArgumentsDTO.getArguments().stream()
                .findFirst()
                .map(arg -> helpPattern.matcher(arg).matches())
                .orElse(false);
    }

    @Override
    public Mono<Void> run(MessageArgumentsDTO messageArgumentsDTO) {
        return Mono.just(messageArgumentsDTO.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(helpMessageResponse))
                .then();
    }
}
