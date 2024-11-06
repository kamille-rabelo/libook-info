package com.rabelo.libook_info.view;

import com.rabelo.libook_info.service.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Scanner;

@Component
public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<Integer, Runnable> commands = new HashMap<>();
    @Autowired
    private Options options;

    public void start() {
        addInitialCommands();
        var command = -1;

        while (true) {
            displayOptions();
            command = getCommand();

            if (command == 0) {
                break;
            }
            commands.get(command).run();
        }
    }

    private int getCommand() {
        var command = -1;
        while (true) {
            try {
                command = Integer.parseInt(scanner.nextLine());
                if (command == 0 || commands.containsKey(command)) break;
                System.err.println("Invalid command. Must be 0-" + commands.size() + ". Try again:");
            } catch (NumberFormatException e) {
                System.err.println("Invalid Input. Must be a number. Try again:");
            }
        }
        return command;
    }

    private void addInitialCommands() {
        commands.put(1, options::searchBooksByTitle);
        commands.put(2, options::listBooksRegistered);
        commands.put(3, options::listAuthorsRegistered);
        commands.put(4, options::listAuthorsAliveInYear);
        commands.put(5, options::listBooksByLanguage);
        commands.put(6, options::listBookLanguageStatistics);
        commands.put(7, options::listTop10MostDownloadedBooks);
        commands.put(8, options::searchAuthorsByName);
    }

    public void displayOptions() {
        System.out.println("********************************");
        System.out.println("""
                1) Search books by title
                2) List all books registered
                3) List authors registered
                4) List authors alive in a given year
                5) List books by language
                6) List book language statistics
                7) List top 10 most downloaded books
                8) Search authors by name
                
                0) Exit
                """);
        System.out.println("********************************");
    }
}
