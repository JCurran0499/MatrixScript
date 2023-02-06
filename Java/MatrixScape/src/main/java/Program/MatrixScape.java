package Program;

import java.util.ArrayList;
import java.util.Scanner;
import Interpreters.*;
import Interpreters.Variables.Variables;
import Parser.Parser;
import Parser.Token;

public class MatrixScape {

    public static void main(String[] args) {

        //get("/matrixscape", (request, response) -> {
         //   response.body("{Hello: world}");
           // return execute(request.queryParams("input"));
       // });
    }

    private static String execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit"))
            return "";

        // ---------- Command Processing ---------- \\
        Primitive result = Parser.parse(command).solve();

        if (result.id().equals("null"))
            return "null";

        if (result.printValue) {
            return result.string();
        } else return "";
    }

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
