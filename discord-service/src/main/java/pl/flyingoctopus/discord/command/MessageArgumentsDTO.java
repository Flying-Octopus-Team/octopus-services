package pl.flyingoctopus.discord.command;

import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessageArgumentsDTO {

    private Message message;
    private List<String> arguments;

}
