import com.fasterxml.jackson.databind.JsonNode;
import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.Color.*;

public class EventFormatter {

    private static String colorize(Ansi.Color color, String text) {
        return Ansi.ansi().fg(color).a(text).reset().toString();
    }

    public static String format(GithubEvent event) {
        String repo = event.repo.name;
        JsonNode payload = event.payload;

        return switch (event.type) {
            case "PushEvent" -> {
                String branch = payload.get("ref").asText().replace("refs/heads/", "");
                yield colorize(GREEN, "Pushed to " + branch + " in " + repo);
            }
            case "IssuesEvent" -> {
                String action = payload.get("action").asText();
                int number = payload.get("issue").get("number").asInt();
                yield colorize(YELLOW, "Issue #" + number + " " + action + " in " + repo);
            }
            case "IssueCommentEvent" -> {
                int number = payload.get("issue").get("number").asInt();
                yield colorize(YELLOW, "Commented on issue #" + number + " in " + repo);
            }
            case "PullRequestEvent" -> {
                String action = payload.get("action").asText();
                int number = payload.get("number").asInt();
                yield colorize(BLUE, "Pull request #" + number + " " + action + " in " + repo);
            }
            case "WatchEvent" -> colorize(MAGENTA, "Starred " + repo);
            case "ForkEvent" -> {
                String forkName = payload.get("forkee").get("full_name").asText();
                yield colorize(CYAN, "Forked " + repo + " to " + forkName);
            }
            case "CreateEvent" -> {
                String refType = payload.get("ref_type").asText();
                JsonNode ref = payload.get("ref");
                if (ref == null || ref.isNull()) yield colorize(GREEN, "Created " + refType + " " + repo);
                yield colorize(GREEN, "Created " + refType + " '" + ref.asText() + "' in " + repo);
            }
            case "DeleteEvent" -> {
                String refType = payload.get("ref_type").asText();
                String ref = payload.get("ref").asText();
                yield colorize(RED, "Deleted " + refType + " '" + ref + "' in " + repo);
            }
            default -> event.type.replace("Event", "") + " in " + repo;
        };
    }
}
