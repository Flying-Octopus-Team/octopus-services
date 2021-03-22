package pl.flyingoctopus.discord.action.help;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HelpActionFormatter {

    private static final String DEFAULT_ARG_NAME = "arg";
    private static final String DEFAULT_SYNTAX_PREFIX = "usage: ";
    private static final String DEFAULT_LONG_OPT_SEPARATOR = " ";
    private static final String USAGE_OPTIONS_SEPARATOR = " ";
    private static final String DEFAULT_LONG_OPT_PREFIX = "--";
    private static final String DEFAULT_OPT_PREFIX = "-";

    private static final String NEW_LINE = "\n";

    private static final int LEFT_PADDING = 8;
    private static final int RIGHT_PADDING = 4;

    public static String getFormattedHelp(String cmdLineSyntax, String header, Options options, String footer) {
        StringBuilder help = new StringBuilder("```");
        help.append(NEW_LINE);
        appendUsage(help, cmdLineSyntax, options)
                .append(NEW_LINE)
                .append(header)
                .append(NEW_LINE)
                .append(NEW_LINE);
        return appendOptions(help, options)
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append(footer)
                .append(NEW_LINE)
                .append("```")
                .toString();
    }

    private static StringBuilder appendUsage(StringBuilder builder, String app, Options options) {
        builder.append(DEFAULT_SYNTAX_PREFIX);
        builder.append(app);
        builder.append(' ');
        List<Option> optList = new ArrayList<>(options.getOptions());
        for (Iterator<Option> it = optList.iterator(); it.hasNext(); ) {
            Option option = it.next();
            appendOption(builder, option, option.isRequired());
            if (it.hasNext()) {
                builder.append(USAGE_OPTIONS_SEPARATOR);
            }
        }
        return builder;
    }

    private static void appendOption(StringBuilder builder, Option option, boolean required) {
        if (!required) {
            builder.append("[");
        }
        if (option.getOpt() != null) {
            builder.append("-").append(option.getOpt());
        } else {
            builder.append("--").append(option.getLongOpt());
        }
        if (option.hasArg() && (option.getArgName() == null || option.getArgName().length() != 0)) {
            builder.append(option.getOpt() == null ? DEFAULT_LONG_OPT_SEPARATOR : " ");
            builder.append("<");
            builder.append(option.getArgName() != null ? option.getArgName() : DEFAULT_ARG_NAME);
            builder.append(">");
        }
        if (!required) {
            builder.append("]");
        }
    }

    private static StringBuilder appendOptions(StringBuilder help, Options options) {

        final String lpad = createPadding(HelpActionFormatter.LEFT_PADDING);
        final String rpad = createPadding(HelpActionFormatter.RIGHT_PADDING);

        // first create list containing only <lpad>-a,--aaa where
        // -a is opt and --aaa is long opt; in parallel look for
        // the longest opt string this list will be then used to
        // sort options ascending
        int max = 0;
        List<StringBuffer> prefixList = new ArrayList<>();

        List<Option> optList = new ArrayList<>(options.getOptions());

        for (Option option : optList) {
            StringBuffer optBuf = new StringBuffer();

            if (option.getOpt() == null) {
                optBuf.append(lpad).append("   ").append(DEFAULT_LONG_OPT_PREFIX).append(option.getLongOpt());
            } else {
                optBuf.append(lpad).append(DEFAULT_OPT_PREFIX).append(option.getOpt());

                if (option.hasLongOpt()) {
                    optBuf.append(", ").append(DEFAULT_LONG_OPT_PREFIX).append(option.getLongOpt());
                }
            }

            if (option.hasArg()) {
                String argName = option.getArgName();
                if (argName != null && argName.length() == 0) {
                    // if the option has a blank argname
                    optBuf.append(' ');
                } else {
                    optBuf.append("=");
                    optBuf.append("<").append(argName != null ? option.getArgName() : DEFAULT_ARG_NAME).append("> ");
                }
            }

            prefixList.add(optBuf);
            max = Math.max(optBuf.length(), max);
        }

        int x = 0;

        for (Iterator<Option> it = optList.iterator(); it.hasNext(); ) {
            Option option = it.next();

            StringBuilder optBuf = new StringBuilder(prefixList.get(x++).toString());

            if (optBuf.length() < max) {
                optBuf.append(createPadding(max - optBuf.length()));
            }

            optBuf.append(rpad);

            if (option.getDescription() != null) {
                optBuf.append(option.getDescription());
            }

            help.append(optBuf.toString());

            if (it.hasNext()) {
                help.append(NEW_LINE);
            }
        }

        return help;
    }

    private static String createPadding(int padLen) {
        char[] padding = new char[padLen];
        Arrays.fill(padding, ' ');

        return new String(padding);
    }
}
