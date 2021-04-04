package pl.flyingoctopus.discord.configure.action;

import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.AuthorisedAction;
import pl.flyingoctopus.discord.action.help.HelpWithArgumentsAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.arguments.ValidatedArguments;
import pl.flyingoctopus.discord.arguments.ValidationException;
import pl.flyingoctopus.discord.configuration.DiscordProperties;
import pl.flyingoctopus.discord.configure.ConfigureArguments;
import pl.flyingoctopus.discord.configure.model.Configuration;
import pl.flyingoctopus.discord.configure.model.ConfigurationKey;
import pl.flyingoctopus.discord.configure.service.ConfigurationService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ConfigureAction implements AuthorisedAction {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("configure");
    private static final int ARGUMENTS_COUNT = 1;

    private static final Option ROLE_MENTION_OPT = Option.builder("r")
            .required(true)
            .longOpt("member-role")
            .hasArg()
            .argName("MEMBER_ROLE_MENTION")
            .numberOfArgs(ARGUMENTS_COUNT)
            .optionalArg(false)
            .desc("Mention of role to add as configured member role").build();

    public static final List<Option> AVAILABLE_OPTIONS = List.of(ROLE_MENTION_OPT);

    private final HelpWithArgumentsAction helpAction = new HelpWithArgumentsAction("!fo configure", "Command used to configure bot", AVAILABLE_OPTIONS, "Read more here: https://wiki.flyingoctopus.pl/");

    private final DiscordProperties properties;
    private final ConfigurationService configurationService;

    @Override
    public boolean isAuthorised(MessageArguments messageArguments) {
        if (messageArguments.hasMemberRole()) {
            return true;
        }
        return messageArguments.getMessage().getAuthor()
                .map(User::getTag)
                .filter(tag -> properties.getAdmins().contains(tag))
                .isPresent();
    }

    @Override
    public boolean isMatching(MessageArguments messageArguments) {
        return messageArguments.getArguments().stream()
                .findFirst()
                .map(arg -> COMMAND_COMPILED_PATTERN.matcher(arg).matches())
                .orElse(false);
    }

    @Override
    public Mono<Void> run(MessageArguments messageArguments) {
        removeFirstArgument(messageArguments);
        return Mono.just(messageArguments)
                .filter(args -> !helpAction.isMatching(args))
                .map(this::tryParseOptions)
                .map(this::validateArguments)
                .filter(ValidatedArguments::isValid)
                .map(ValidatedArguments::getArguments)
                .flatMap(this::persistConfiguration)
                .flatMap(configuration -> messageArguments
                        .getMessage()
                        .getChannel()
                        .flatMap(channel -> channel.createMessage(messageCreateSpec -> prepareResponse(messageArguments, messageCreateSpec)))
                )
                .switchIfEmpty(helpAction.sendHelpMessage(messageArguments))
                .then();
    }

    private void prepareResponse(MessageArguments messageArguments, MessageCreateSpec messageCreateSpec) {
        messageCreateSpec
                .setContent("Configuration persisted!")
                .setMessageReference(messageArguments.getMessage().getId());
    }

    private Mono<Configuration> persistConfiguration(ConfigureArguments configureArguments) {
        return configureArguments
                .getMessage()
                .getRoleMentions()
                .next()
                .flatMap(role -> configurationService.saveConfiguration(ConfigurationKey.MEMBER_ROLE_ID, role.getId().asString()));
    }

    private ValidatedArguments<ConfigureArguments> tryParseOptions(MessageArguments args) {
        try {
            var parsedArgs = parse(args);
            return new ValidatedArguments<>(parsedArgs);
        } catch (ParseException e) {
            var validated = new ValidatedArguments<>(new ConfigureArguments(args));
            validated.getErrors().add(new ValidationException("Parse error occurred", e));
            return validated;
        }
    }

    private ValidatedArguments<ConfigureArguments> validateArguments(ValidatedArguments<ConfigureArguments> addValidatedArguments) {
        if (!addValidatedArguments.isValid()) {
            return addValidatedArguments;
        }
        if (isArgsValid(addValidatedArguments.getArguments())) {
            return addValidatedArguments;
        }
        addValidatedArguments.getErrors().add(new ValidationException("Invalid arguments"));
        return addValidatedArguments;
    }

    private boolean isArgsValid(ConfigureArguments parsedArgs) {
        if (parsedArgs.getMessage().getRoleMentionIds().size() != 1) {
            return false;
        }
        return !StringUtils.isEmpty(parsedArgs.getRoleMention());
    }

    public ConfigureArguments parse(MessageArguments dto) throws ParseException {
        var options = new Options();
        AVAILABLE_OPTIONS.forEach(options::addOption);
        CommandLineParser parser = new DefaultParser();
        var commandLine = parser.parse(options, dto.getArguments().toArray(new String[0]));
        return mapLineToConfigureArguments(dto, commandLine);
    }

    private ConfigureArguments mapLineToConfigureArguments(MessageArguments dto, CommandLine commandLine) {
        var builder = ConfigureArguments.builder();
        builder.messageArguments(dto);

        if (commandLine.hasOption(ROLE_MENTION_OPT.getOpt())) {
            builder.roleMention(commandLine.getOptionValue(ROLE_MENTION_OPT.getOpt()));
        }

        return builder.build();
    }
}
