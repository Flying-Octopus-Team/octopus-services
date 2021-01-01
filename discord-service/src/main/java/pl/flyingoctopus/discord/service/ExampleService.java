package pl.flyingoctopus.discord.service;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flyingoctopus.trello.CoolTrello;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final CoolTrello coolTrello;

    public Mono<Void> coolifyMessage(Message message) {
        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!example"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(coolTrello.coolify("hello world!")))
                .then();
    }

}
