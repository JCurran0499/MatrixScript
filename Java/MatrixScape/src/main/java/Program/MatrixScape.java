package Program;

import java.util.Scanner;
import Interpreters.*;
import Parser.Parser;

import static spark.Spark.*;

public class MatrixScape {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            run();
        else {
            port(4567);
            get("/", (req, res) -> {
                String command = req.queryParams("command");
                return execute(command);
            });
        }
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.contains("//"))
                command = command.substring(0, command.indexOf("//")).stripTrailing();

            if (command.equals("quit") || command.equals("exit"))
                break;

            // ---------- Command Processing ---------- \\
            Primitive result = Parser.parse(command).solve();

            if (result.id().equals("null"))
                continue;

            if (result.printValue) {
                System.out.println(result.string());
                System.out.println();
            } else result.printValue = true;
        }
    }

    private static String execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit"))
            return "<quit program>";

        // ---------- Command Processing ---------- \\
        Primitive result = Parser.parse(command).solve();

        if (result.id().equals("null"))
            return "NULL";

        if (result.printValue)
            return result.string();
        else
            return "";
    }
}
