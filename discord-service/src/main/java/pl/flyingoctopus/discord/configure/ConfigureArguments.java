package pl.flyingoctopus.discord.configure;

import lombok.Builder;
import lombok.Getter;
import pl.flyingoctopus.discord.arguments.MessageArguments;

@Getter
public class ConfigureArguments extends MessageArguments {

    private final String roleMention;

    public ConfigureArguments(MessageArguments messageArguments) {
        super(messageArguments.getMessage(), messageArguments.getArguments(), messageArguments.hasMemberRole());
        this.roleMention = null;
    }

    @Builder
    public ConfigureArguments(MessageArguments messageArguments, String roleMention) {
        super(messageArguments.getMessage(), messageArguments.getArguments(), messageArguments.hasMemberRole());
        this.roleMention = roleMention;
    }
}