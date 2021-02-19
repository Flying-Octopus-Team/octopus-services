package pl.flyingoctopus.discord.member.action;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddAction implements DiscordAction {

    private final MemberRepository memberRepository;

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public Mono<Void> run(Message message) {
        return Mono.just(message)
                // TODO: validate arguments
                .flatMap(Message::getChannel)
                .flatMap(channel ->
                        message.getUserMentions()
                                .next()
                                .flatMap(user -> {
                                    var member = Member.builder().discordId(user.getId().asString()).build();
                                    return memberRepository.save(member)
                                            .flatMap(mbr -> channel.createMessage(String.format("Member %s added: id(%s), discordId(%s)", user.getUsername(), mbr.getId(), mbr.getDiscordId())));
                                })
                )
                .then();
    }
}
