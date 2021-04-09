package pl.flyingoctopus.discord.arguments;

import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageArguments {

    private Message message;
    private List<String> arguments;
    @Accessors(fluent = true, chain = false)
    private boolean hasMemberRole;

}
