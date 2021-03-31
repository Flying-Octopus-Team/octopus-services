package pl.flyingoctopus.discord.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.flyingoctopus.discord.links.LinksProperty;

import java.util.List;

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

    /**
     * Used as a body of links command message
     */
    private List<LinksProperty> links;
}
