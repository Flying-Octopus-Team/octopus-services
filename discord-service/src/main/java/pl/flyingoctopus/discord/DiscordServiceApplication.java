package pl.flyingoctopus.discord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class DiscordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordServiceApplication.class, args);
    }

}
