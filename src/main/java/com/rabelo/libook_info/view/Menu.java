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
                System.err.println("Invalid command. Must be 1. Try again:");
            } catch (NumberFormatException e) {
                System.err.println("Invalid Input. Must be a number. Try again:");
            }
        }
        return command;
    }

    private void addInitialCommands() {
        commands.put(1, options::searchBookByTitleAndAuthorsName);
        commands.put(2, options::listBooksRegistered);
    }

    public void displayOptions() {
        System.out.println("********************************");
        System.out.println("""
                1) Search book by title
                2) List all books registered
                
                0) Exit
                """);
        System.out.println("********************************");
    }
}