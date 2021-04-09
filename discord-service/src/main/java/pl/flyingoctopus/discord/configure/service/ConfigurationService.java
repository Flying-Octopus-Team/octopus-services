package pl.flyingoctopus.discord.configure.service;

import discord4j.common.util.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.flyingoctopus.discord.configure.model.Configuration;
import pl.flyingoctopus.discord.configure.model.ConfigurationKey;
import pl.flyingoctopus.discord.configure.repository.ConfigurationRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository repository;

    public Mono<Configuration> saveConfiguration(ConfigurationKey key, String value) {
        return repository.findByKey(key)
                .map(config -> {
                    config.setValue(value);
                    return config;
                })
                .defaultIfEmpty(getNewConfiguration(key, value))
                .flatMap(repository::save);
    }

    private Configuration getNewConfiguration(ConfigurationKey key, String value) {
        var config = new Configuration();
        config.setKey(key);
        config.setValue(value);
        return config;
    }

    public Mono<Snowflake> getMemberRoleId() {
        return repository.findByKey(ConfigurationKey.MEMBER_ROLE_ID)
                .map(config -> Snowflake.of(config.getValue()));
    }

}
