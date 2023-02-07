package Program;

import java.util.Scanner;
import Interpreters.*;
import Parser.Parser;

import static spark.Spark.*;

public class MatrixScape {

    public static void main(String[] args) {
        port(80);
        get("/", (req, res) -> "Hello World!");
        /*Scanner scanner = new Scanner(System.in);

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
        }*/
    }
}
