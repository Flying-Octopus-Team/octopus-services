package pl.flyingoctopus.discord.configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.flyingoctopus.discord.listener.EventListener;
import pl.flyingoctopus.trello.configuration.TrelloProperties;

import java.util.List;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DiscordProperties.class)
public class DiscordClientConfiguration {

    private final DiscordProperties properties;
    private final TrelloProperties trelloProperties;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") List<EventListener<T>> eventListeners) {
        var client = DiscordClientBuilder.create(properties.getToken())
                .build()
                .login()
                .block();

        for (EventListener<T> listener : eventListeners) {
            Objects.requireNonNull(client)
                    .on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }

        return client;
    }

}
