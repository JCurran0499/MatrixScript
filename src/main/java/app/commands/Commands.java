package app.commands;

import app.parser.Parser;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Null;
import app.parser.interpreters.variables.SessionHandler;

import java.util.Scanner;

public class Commands {

    public static void runCommands() {
        System.out.println("\n\n");
        System.out.println("|\\   /|  / \\ |_   _||  _|  | | \\ \\/ / / __|  / _| |  _|  | | | -  ||_   _|");
        System.out.println("| \\ / | / _ \\  | |  |   \\  | |  |  |  \\__ \\ | |_  |   \\  | | |  _/   | |");
        System.out.println("|_| |_|/_/ \\_\\ |_|  |_||_| |_| /_/\\_\\ |___/  \\__| |_||_| |_| |_|     | |");
        System.out.println("========================================================================");
        System.out.println("\n");

        SessionHandler.createSession(SessionHandler.RUN_TOKEN);
        Scanner scanner = new Scanner(System.in);

        boolean end = true;
        while (end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.equals("quit()") || command.equals("exit()"))
                end = false;

            else {
                Primitive result = execute(command);
                if (!Null.is(result))
                    System.out.println(result.string() + "\n");
            }
        }

        System.out.println();
    }

    private static Primitive execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        return Parser.parse(SessionHandler.RUN_TOKEN, command).solve();
    }
}
