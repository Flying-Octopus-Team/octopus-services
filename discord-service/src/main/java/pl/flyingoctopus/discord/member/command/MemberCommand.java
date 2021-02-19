package pl.flyingoctopus.discord.member.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.HelpAction;
import pl.flyingoctopus.discord.command.DiscordCommand;
import pl.flyingoctopus.discord.member.action.AddAction;

import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MemberCommand implements DiscordCommand {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("^!member .*");

    private final AddAction addAction;

    @Override
    public Pattern getCommandPattern() {
        return COMMAND_COMPILED_PATTERN;
    }

    @Override
    public DiscordAction getCommandUsageAction() {
        return new HelpAction("usage: !member add - ... ");
    }

    @Override
    public Set<DiscordAction> getActions() {
        return Set.of(addAction);
    }
}
