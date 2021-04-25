package pl.flyingoctopus.discord.member.action;

import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import pl.flyingoctopus.discord.action.DiscordAction;
import pl.flyingoctopus.discord.action.help.HelpWithArgumentsAction;
import pl.flyingoctopus.discord.arguments.MessageArguments;
import pl.flyingoctopus.discord.arguments.ValidatedArguments;
import pl.flyingoctopus.discord.arguments.ValidationException;
import pl.flyingoctopus.discord.configure.service.ConfigurationService;
import pl.flyingoctopus.discord.member.AddArguments;
import pl.flyingoctopus.discord.member.model.Member;
import pl.flyingoctopus.discord.member.repository.MemberRepository;
import pl.flyingoctopus.trello.service.TrelloMockService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

//TODO: Might want to create something more universal for Arguments handling
@Component
@RequiredArgsConstructor
public class AddMemberAction implements DiscordAction {

    private static final Pattern COMMAND_COMPILED_PATTERN = Pattern.compile("add");
    private static final int ARGUMENTS_COUNT = 1;

    private static final Option USER_MENTION_OPT = Option.builder("u")
            .required(true)
            .longOpt("user-mention")
            .hasArg()
            .argName("DISCORD_USER_MENTION")
            .numberOfArgs(ARGUMENTS_COUNT)
            .optionalArg(false)
            .desc("Mention of user to add as member").build();
    private static final Option MEMBER_EMAIL_OPT = Option.builder("m")
            .required(false)
            .longOpt("member-email")
            .hasArg()
            .argName("MEMBER_EMAIL")
            .numberOfArgs(ARGUMENTS_COUNT)
            .optionalArg(false)
            .desc("Sends Trello and XWiki invites to given email").build();
    private static final Option TRELLO_EMAIL_OPT = Option.builder("t")
            .required(false)
            .longOpt("trello-email")
            .hasArg()
            .argName("TRELLO_EMAIL")
            .numberOfArgs(ARGUMENTS_COUNT)
            .optionalArg(false)
            .desc("Sends trello invite to given email").build();
    private static final Option WIKI_EMAIL_OPT = Option.builder("w")
            .required(false)
            .longOpt("wiki-email")
            .hasArg()
            .argName("WIKI_EMAIL")
            .numberOfArgs(ARGUMENTS_COUNT)
            .optionalArg(false)
            .desc("Sends xwiki invite to given email").build();

    public static final List<Option> AVAILABLE_OPTIONS = List.of(USER_MENTION_OPT, MEMBER_EMAIL_OPT, TRELLO_EMAIL_OPT, WIKI_EMAIL_OPT, TRELLO_TOKEN_OPT);

    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final HelpWithArgumentsAction helpAction = new HelpWithArgumentsAction("!fo member add", "Command used to add user as member", AVAILABLE_OPTIONS, "Read more here: https://wiki.flyingoctopus.pl/");

    private final MemberRepository memberRepository;
    private final ConfigurationService configurationService;

    private final TrelloMockService trelloMockService;

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
                .flatMap(this::addMember)
                .flatMap(this::sendTrelloInvite)
                .flatMap(member -> messageArguments
                        .getMessage()
                        .getChannel()
                        .flatMap(channel -> channel.createMessage(String.format("Member %s added: id(%s), discordId(%s)", createUserMention(member.getDiscordId()), member.getId(), member.getDiscordId())))
                )
                .switchIfEmpty(helpAction.sendHelpMessage(messageArguments))
                .then();
    }

    private String createUserMention(String userId) {
        return "<@" + userId + ">";
    }

    private Mono<Member> sendTrelloInvite(Member member) {
        return Mono.just(member);
    }

    private Mono<Member> addMember(AddArguments addArguments) {
        return addArguments
                .getMessage()
                .getUserMentions()
                .next()
                .flatMap(user -> configurationService.getMemberRoleId().flatMap(roleId -> addMemberRole(addArguments, user, roleId)))
                .flatMap(user -> memberRepository.save(buildMemberEntity(addArguments, user)));
    }

    private Mono<User> addMemberRole(AddArguments addArguments, User user, discord4j.common.util.Snowflake roleId) {
        var guildId = addArguments.getMessage().getGuildId()
                .orElseThrow(() -> new ValidationException("Guild missing!")); //TODO might want to persist guild during configure?
        return user.asMember(guildId)
                .flatMap(member -> member.addRole(roleId))
                .thenReturn(user);
    }

    private Member buildMemberEntity(AddArguments addArguments, User user) {
        var entity = new Member();
        entity.setDiscordId(user.getId().asString());
        entity.setDiscordName(user.getUsername());
        if (StringUtils.isEmpty(addArguments.getTrelloEmail())) {
            entity.setTrelloEmail(addArguments.getMemberEmail());
        } else {
            entity.setTrelloEmail(addArguments.getTrelloEmail());
        }

        if (StringUtils.isEmpty(addArguments.getWikiEmail())) {
            entity.setWikiEmail(addArguments.getMemberEmail());
        } else {
            entity.setWikiEmail(addArguments.getWikiEmail());
        }

        return entity;
    }

    private ValidatedArguments<AddArguments> tryParseOptions(MessageArguments args) {
        try {
            var parsedArgs = parse(args);
            return new ValidatedArguments<>(parsedArgs);
        } catch (ParseException e) {
            var validated = new ValidatedArguments<>(new AddArguments(args));
            validated.getErrors().add(new ValidationException("Parse error occurred", e));
            return validated;
        }
    }

    private ValidatedArguments<AddArguments> validateArguments(ValidatedArguments<AddArguments> addValidatedArguments) {
        if (!addValidatedArguments.isValid()) {
            return addValidatedArguments;
        }
        if (isArgsValid(addValidatedArguments.getArguments())) {
            return addValidatedArguments;
        }
        addValidatedArguments.getErrors().add(new ValidationException("Invalid arguments"));
        return addValidatedArguments;
    }

    private boolean isArgsValid(AddArguments parsedArgs) {
        if (parsedArgs.getMessage().getUserMentionIds().size() != 1) {
            return false;
        }
        if (StringUtils.isEmpty(parsedArgs.getUserMention())) {
            return false;
        }
        if (isInvalidEmail(parsedArgs.getMemberEmail())) {
            return false;
        }
        if (isInvalidEmail(parsedArgs.getTrelloEmail())) {
            return false;
        }
        if (isInvalidEmail(parsedArgs.getWikiEmail())) {
            return false;
        }

        return true;
    }

    private boolean isInvalidEmail(String email) {
        return !StringUtils.isEmpty(email) && !emailValidator.isValid(email);
    }

    public AddArguments parse(MessageArguments dto) throws ParseException {
        var options = new Options();
        AVAILABLE_OPTIONS.forEach(options::addOption);
        CommandLineParser parser = new DefaultParser();
        var commandLine = parser.parse(options, dto.getArguments().toArray(new String[0]));
        return mapLineToAddArguments(dto, commandLine);
    }

    private AddArguments mapLineToAddArguments(MessageArguments dto, CommandLine commandLine) {
        var builder = AddArguments.builder();
        builder.messageArguments(dto);

        if (commandLine.hasOption(USER_MENTION_OPT.getOpt())) {
            builder.userMention(commandLine.getOptionValue(USER_MENTION_OPT.getOpt()));
        }
        if (commandLine.hasOption(MEMBER_EMAIL_OPT.getOpt())) {
            builder.memberEmail(commandLine.getOptionValue(MEMBER_EMAIL_OPT.getOpt()));
        }
        if (commandLine.hasOption(TRELLO_EMAIL_OPT.getOpt())) {
            builder.trelloEmail(commandLine.getOptionValue(TRELLO_EMAIL_OPT.getOpt()));
        }
        if (commandLine.hasOption(WIKI_EMAIL_OPT.getOpt())) {
            builder.wikiEmail(commandLine.getOptionValue(WIKI_EMAIL_OPT.getOpt()));
        }
        if (commandLine.hasOption(TRELLO_TOKEN_OPT.getOpt())) {
            builder.trelloId(commandLine.getOptionValue(TRELLO_TOKEN_OPT.getOpt()));
        }

        return builder.build();
    }

}
