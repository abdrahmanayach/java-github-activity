import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubEvent {
    public String type;
    public Repo repo;
    public JsonNode payload;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repo {
        public String name;
    }
}
