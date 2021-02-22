package pl.flyingoctopus.discord.action;

import pl.flyingoctopus.discord.command.MessageArgumentsDTO;
import reactor.core.publisher.Mono;

public interface DiscordAction {

    boolean isMatching(MessageArgumentsDTO messageArgumentsDTO);

    Mono<Void> run(MessageArgumentsDTO messageArgumentsDTO);

}
