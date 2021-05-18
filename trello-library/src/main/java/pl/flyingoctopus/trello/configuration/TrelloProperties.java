package pl.flyingoctopus.trello.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("trello")
public class TrelloProperties {

    /**
     * Access trello key
     * https://trello.com/app-key/
     */
    private String key;

    /**
     * Access trello token
     */
    private String token;


}
