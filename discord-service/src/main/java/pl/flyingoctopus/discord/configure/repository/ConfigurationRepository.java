package pl.flyingoctopus.discord.configure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.flyingoctopus.discord.configure.model.Configuration;
import pl.flyingoctopus.discord.configure.model.ConfigurationKey;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ConfigurationRepository extends ReactiveCrudRepository<Configuration, UUID> {

    Mono<Configuration> findByKey(ConfigurationKey key);
}
