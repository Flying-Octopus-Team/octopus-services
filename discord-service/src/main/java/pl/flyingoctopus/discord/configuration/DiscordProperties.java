package pl.flyingoctopus.discord.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

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

    private Map<String, String> links;
}
