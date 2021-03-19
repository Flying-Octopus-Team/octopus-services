package pl.flyingoctopus.discord.member.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.HelpAction;
import pl.flyingoctopus.discord.command.DiscordCommand;

import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MemberCommand implements DiscordCommand {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("member");
    private final HelpAction helpAction = new HelpAction("    usage: !fo member <command>");

    private final AddMemberAction addMemberAction;

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
        return Set.of(addMemberAction, helpAction);
    }
}
