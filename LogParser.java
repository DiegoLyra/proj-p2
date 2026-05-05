package parser;

import model.LogEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsável por transformar uma linha bruta do log
 * em um objeto LogEntry.
 */
public class LogParser {

    // Regex simplificada para Apache log
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "(\\S+) .* \\[(.*?)\\] \"(\\S+) (.*?) .*\" (\\d{3}) (\\d+) .* \"(.*?)\" \"(.*?)\""
    );

    /**
     * Faz o parse de uma linha.
     * Retorna null caso a linha seja inválida.
     */
    public static LogEntry parse(String line) {

        Matcher matcher = LOG_PATTERN.matcher(line);

        if (!matcher.find()) return null;

        try {
            String ip = matcher.group(1);
            String timestamp = matcher.group(2);
            String method = matcher.group(3);
            String endpoint = matcher.group(4);
            int statusCode = Integer.parseInt(matcher.group(5));
            int responseSize = Integer.parseInt(matcher.group(6));
            String userAgent = matcher.group(8);

            return new LogEntry(ip, timestamp, method, endpoint,
                    statusCode, responseSize, userAgent);

        } catch (Exception e) {
            return null;
        }
    }
}
