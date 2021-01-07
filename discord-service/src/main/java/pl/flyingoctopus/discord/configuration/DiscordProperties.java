package pl.flyingoctopus.discord.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    /**
     * Used to authenticate bot within Discord API
     */
    private String token;

    /**
     * Enables discord integration
     */
    private boolean enabled = false;
}
