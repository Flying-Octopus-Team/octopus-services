package pl.flyingoctopus.discord.option;

import pl.flyingoctopus.discord.command.MessageArgumentsDTO;

import java.util.regex.Pattern;

public interface ActionOption {

    Pattern getOptionPattern();

    Pattern getOptionLongPattern();

    default boolean isMatching(MessageArgumentsDTO messageArgumentsDTO) {
        return messageArgumentsDTO.getArguments().stream()
                .findFirst()
                .map(this::isArgumentMatching)
                .orElse(false);
    }

    default boolean isArgumentMatching(String argument) {
        return getOptionPattern().matcher(argument).matches() || getOptionLongPattern().matcher(argument).matches();
    }

}
