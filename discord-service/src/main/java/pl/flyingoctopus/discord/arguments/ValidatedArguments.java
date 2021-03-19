package pl.flyingoctopus.discord.arguments;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidatedArguments<K extends MessageArguments> {

    private final K arguments;
    private final List<ValidationException> errors = new ArrayList<>();

    public boolean isValid() {
        return errors.isEmpty();
    }
}
