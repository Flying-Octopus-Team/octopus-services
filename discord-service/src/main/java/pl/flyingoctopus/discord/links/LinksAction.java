package pl.flyingoctopus.discord.links;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.DefaultHelpAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.configuration.DiscordProperties;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(DiscordProperties.class)
public class LinksAction implements DiscordAction {

    private final DiscordProperties discordProperties;
    private static final String THUMBNAIL_URL = "https://wiki.flyingoctopus.pl/bin/download/Dla%20Cz%C5%82onk%C3%B3w/Zasoby/Logo/WebHome/octopus_pictorial_transparent.png?rev=1.1";
    private static final String EMBED_TITLE = "Linki";

    private final DefaultHelpAction helpAction = new DefaultHelpAction("    usage: !fo links");

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("links");

    @Override
    public boolean isMatching(MessageArguments messageArguments) {
        if (CollectionUtils.isEmpty(messageArguments.getArguments())) {
            return false;
        }
        return getCommandPattern().matcher(messageArguments.getArguments().get(0)).matches();
    }

    public Pattern getCommandPattern() {
        return COMMAND_COMPILED_PATTERN;
    }

    @Override
    public Mono<Void> run(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createEmbed(this::buildEmbeddedMessage))
                .then();
    }

    private String createEmbedDescription() {
        return discordProperties.getLinks().stream()
                .map(linksProperty -> String.format("[%s](%s)\n", linksProperty.getAlias(), linksProperty.getUrl()))
                .reduce("", String::concat);
    }

    private void buildEmbeddedMessage(EmbedCreateSpec spec) {
        spec.setColor(Color.LIGHT_SEA_GREEN)
                .setTitle(EMBED_TITLE)
                .setDescription(createEmbedDescription())
                .setThumbnail(THUMBNAIL_URL);
    }
}
