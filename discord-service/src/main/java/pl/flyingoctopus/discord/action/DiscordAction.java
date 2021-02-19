package pl.flyingoctopus.discord.action;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface DiscordAction {

    String getName();

    Mono<Void> run(Message message);

}
