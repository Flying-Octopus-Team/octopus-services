package pl.flyingoctopus.trello.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "pl.flyingoctopus.trello")
@EnableConfigurationProperties(value = TrelloProperties.class)
public class TrelloModuleConfiguration {
}
