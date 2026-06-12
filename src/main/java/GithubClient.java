import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GithubClient {
    private static final String BASE_URL = "https://api.github.com/users/%s/events";

    public static List<GithubEvent> fetchEvents(String username) throws IOException, InterruptedException {
        String url = String.format(BASE_URL, username);

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        if (response.statusCode() == 404) {
            throw new RuntimeException("User not found: " + username);
        }

        if (response.statusCode() != 200) {
            throw new RuntimeException("GitHub API error: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<>() {});
    }
}
