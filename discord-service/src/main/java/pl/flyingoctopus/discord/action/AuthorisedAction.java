package pl.flyingoctopus.discord.action;

import pl.flyingoctopus.discord.arguments.MessageArguments;

public interface AuthorisedAction extends DiscordAction {

    boolean isAuthorised(MessageArguments messageArguments);

}
