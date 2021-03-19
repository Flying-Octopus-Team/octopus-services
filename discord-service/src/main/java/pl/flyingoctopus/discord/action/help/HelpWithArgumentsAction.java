package pl.flyingoctopus.discord.action.help;

import discord4j.core.object.entity.Message;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class HelpWithArgumentsAction implements HelpAction {

    private final String commandSyntax;
    private final String helpHeader;
    private final Options options;
    private final String helpFooter;

    public HelpWithArgumentsAction(String commandSyntax, String helpHeader, Collection<Option> actionOptions, String helpFooter) {
        this.commandSyntax = commandSyntax;
        this.helpHeader = helpHeader;
        this.helpFooter = helpFooter;
        this.options = new Options();
        actionOptions.forEach(options::addOption);
    }

    @Override
    public Mono<Message> sendHelpMessage(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(HelpActionFormatter.getFormattedHelp(commandSyntax, helpHeader, options, helpFooter)));
    }
}
