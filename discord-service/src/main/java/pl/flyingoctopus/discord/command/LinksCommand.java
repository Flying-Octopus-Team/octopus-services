package pl.flyingoctopus.discord.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.HelpAction;
import pl.flyingoctopus.discord.action.LinksAction;

import java.util.Set;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class LinksCommand implements DiscordCommand {
    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("links");

    private final LinksAction linksAction;
    private final HelpAction helpAction = new HelpAction("    usage: !fo links");

    @Override
    public Pattern getCommandPattern() { return COMMAND_COMPILED_PATTERN; }

    @Override
    public DiscordAction getHelpAction() { return helpAction; }

    @Override
    public Set<DiscordAction> getActions() { return Set.of(linksAction, helpAction); }
}