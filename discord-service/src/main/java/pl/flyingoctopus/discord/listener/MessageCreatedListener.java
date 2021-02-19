package pl.flyingoctopus.discord.listener;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.command.MainDiscordCommand;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageCreatedListener implements EventListener<MessageCreateEvent> {

    private final MainDiscordCommand mainCommand;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        log.debug("New message: {}", event.getMessage());
        return mainCommand.handleMessage(event.getMessage());
    }
}
