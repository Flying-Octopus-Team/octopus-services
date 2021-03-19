package pl.flyingoctopus.discord.action;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.configuration.DiscordProperties;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(DiscordProperties.class)
public class LinksAction implements DiscordAction {

    private final DiscordProperties discordProperties;
    private final String thumbnailUrl = "https://wiki.flyingoctopus.pl/bin/download/Dla%20Cz%C5%82onk%C3%B3w/Zasoby/Logo/WebHome/octopus_pictorial_transparent.png?rev=1.1";

    @Override
    public boolean isMatching(MessageArguments messageArguments) {
        return true;
    }

    @Override
    public Mono<Void> run(MessageArguments messageArguments) {
        return Mono.just(messageArguments.getMessage())
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createEmbed(spec ->
                        spec.setColor(Color.LIGHT_SEA_GREEN)
                                .setTitle("Linki")
                                .setDescription(CreateEmbedDescription())
                                .setThumbnail(thumbnailUrl)))
                .then();
    }

    private String CreateEmbedDescription() {
        String description = "";
        Map<String, String> property = discordProperties.getLinks();
        Set<String> keySet = property.keySet();
        for (String key: keySet) description += String.format("[%s](%s)\n", key, property.get(key));

        return description;
    }
}