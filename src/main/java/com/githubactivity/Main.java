package com.githubactivity;

import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        AnsiConsole.systemInstall();
        System.out.println("Github Username: ");
        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        List<GithubEvent> events = GithubClient.fetchEvents(username);
        events.stream()
                .map(EventFormatter::format)
                .forEach(line -> System.out.println("- " + line));
        AnsiConsole.systemUninstall();
    }
}
