package pl.flyingoctopus.discord.service;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import pl.flyingoctopus.trello.CoolTrello;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class ExampleService {

    private final CoolTrello coolTrello;

    private final MemberRepository memberRepository;

    public Mono<Void> coolifyMessage(Message message) {
        return Mono.just(message)
                .filter(msg -> msg.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(msg -> msg.getContent().equalsIgnoreCase("!example"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> {
                            var author = message.getAuthor()
                                    .orElseThrow(RuntimeException::new);
                            var member = Member.builder()
                                    .discordId(author.getId().asString())
                                    .build();
                            return memberRepository.save(member)
                                    .flatMap(mbr -> channel.createMessage(String.format("Member %s added: id(%s), discordId(%s)", author.getUsername(), mbr.getId(), mbr.getDiscordId())));
                        }
                )
                .then();
    }
}
