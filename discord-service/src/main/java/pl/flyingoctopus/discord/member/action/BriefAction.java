package pl.flyingoctopus.discord.member.action;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.DefaultHelpAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class BriefAction implements DiscordAction {
    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("brief");

    private final DefaultHelpAction helpAction = new DefaultHelpAction("  usage: !fo member brief <brief-report-content>");

    private final MemberRepository memberRepository;

    @Override
    public boolean isMatching(MessageArguments messageArguments) {
        return messageArguments.getArguments().stream()
                .findFirst()
                .map(arg -> COMMAND_COMPILED_PATTERN.matcher(arg).matches())
                .orElse(false);
    }

    @Override
    public Mono<Void> run(MessageArguments messageArguments) {
        removeFirstArgument(messageArguments);
        return Mono.just(messageArguments.getMessage().getAuthor().get())
                .flatMap(this::findMemberByDiscordId)
                //TODO: Send post request to trello api
                .flatMap(member -> messageArguments
                    .getMessage()
                    .getChannel()
                    .flatMap(channel -> channel.createMessage(member.getTrelloId())))
                .switchIfEmpty(helpAction.sendHelpMessage(messageArguments))
                .then();
    }

    private Mono<Member> findMemberByDiscordId(User author) {
        return memberRepository.findAll()
                .filter(member -> author.getId().asString().equals(member.getDiscordId()))
                .next();
    }
    /*messageArguments.getArguments().toArray(new String[0])[0])
    */
}
