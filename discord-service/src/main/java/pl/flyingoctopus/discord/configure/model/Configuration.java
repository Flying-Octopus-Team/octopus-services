package pl.flyingoctopus.discord.configure.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("configuration")
public class Configuration {

    @Id
    @Column("id")
    private UUID id;

    @Column("key")
    private ConfigurationKey key;

    @Column("value")
    private String value;
}
