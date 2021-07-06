package pl.flyingoctopus.discord.member.action;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.DefaultHelpAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import pl.flyingoctopus.trello.configuration.TrelloProperties;
import pl.flyingoctopus.trello.service.impl.TrelloServiceImpl;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class BriefAction implements DiscordAction {
    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("brief");

    private final DefaultHelpAction helpAction = new DefaultHelpAction("  usage: !fo member brief <brief-report-content>");
    private final MemberRepository memberRepository;

    private final TrelloProperties trelloProperties;
    private final WebClient.Builder webClientBuilder = WebClient.builder().baseUrl("https://api.trello.com/1");
    private TrelloServiceImpl trelloServiceImpl;

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

        if (messageArguments.getArguments().isEmpty()) {
            return helpAction.sendHelpMessage(messageArguments).then();
        }

        return Mono.justOrEmpty(getAuthorIdOrEmpty(messageArguments))
                .flatMap(memberRepository::findByDiscordId)
                .flatMap(member -> sendCommentToTrelloCard(member, messageArguments))
                .filter(HttpStatus.OK::equals)
                .flatMap(httpStatus -> messageArguments
                        .getMessage()
                        .getChannel()
                        .flatMap(channel -> channel.createMessage("Komentarz został pomyślnie dodany")))
                .switchIfEmpty(helpAction.sendHelpMessage(messageArguments))
                .then();
    }

    private String getAuthorIdOrEmpty(MessageArguments messageArguments) {
        return messageArguments.getMessage().getAuthor().map(User::getId).map(Snowflake::asString).orElse(null);
    }

    private Mono<HttpStatus> sendCommentToTrelloCard(Member member, MessageArguments messageArguments) {
        trelloServiceImpl = new TrelloServiceImpl(trelloProperties, webClientBuilder);

        return trelloServiceImpl.addCommentToCard(member.getTrelloReportCardId(), String.join(" ", messageArguments.getArguments()));
    }
}
