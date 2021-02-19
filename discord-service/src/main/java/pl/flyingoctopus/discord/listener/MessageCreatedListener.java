package pl.flyingoctopus.discord.listener;

import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.member.command.MemberCommand;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageCreatedListener implements EventListener<MessageCreateEvent> {

    private final MemberCommand memberCommand;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return memberCommand.handleMessage(event.getMessage());
    }
}
