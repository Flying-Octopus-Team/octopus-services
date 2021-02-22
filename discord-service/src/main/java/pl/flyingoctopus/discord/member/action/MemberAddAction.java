package pl.flyingoctopus.discord.member.action;

import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.HelpAction;
import pl.flyingoctopus.discord.command.MessageArgumentsDTO;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MemberAddAction implements DiscordAction {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("member-add");

    private final HelpAction helpAction = new HelpAction("    usage: !fo member-add [OPTIONS]");
    private final MemberRepository memberRepository;

    @Override
    public boolean isMatching(MessageArgumentsDTO messageArgumentsDTO) {
        return messageArgumentsDTO.getArguments().stream()
                .findFirst()
                .map(arg -> COMMAND_COMPILED_PATTERN.matcher(arg).matches())
                .orElse(false);
    }

    @Override
    public Mono<Void> run(MessageArgumentsDTO messageArgumentsDTO) {
        var message = messageArgumentsDTO.getMessage();
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
