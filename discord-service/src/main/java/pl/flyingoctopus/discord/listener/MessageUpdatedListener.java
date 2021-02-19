package pl.flyingoctopus.discord.listener;

import discord4j.core.event.domain.message.MessageUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.member.command.MemberCommand;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageUpdatedListener implements EventListener<MessageUpdateEvent> {

    private final MemberCommand memberCommand;

    @Override
    public Class<MessageUpdateEvent> getEventType() {
        return MessageUpdateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageUpdateEvent event) {
        return Mono.just(event)
                .filter(MessageUpdateEvent::isContentChanged)
                .flatMap(MessageUpdateEvent::getMessage)
                .flatMap(memberCommand::handleMessage);
    }
}
