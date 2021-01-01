package pl.flyingoctopus.discord.listener;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.service.ExampleService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExampleCreatedListener implements EventListener<MessageCreateEvent> {

    private final ExampleService service;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return service.coolifyMessage(event.getMessage());
    }
}
