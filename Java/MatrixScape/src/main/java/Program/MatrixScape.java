package Program;

import java.util.Scanner;
import Interpreters.*;
import Parser.Parser;

import static spark.Spark.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatrixScape {

    public static void main(String[] args) {
        execute("[1 2 3]");
        /*if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }*/
    }

    public static void runAPI() {
        port(4567);
        get("/", (req, res) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode body = mapper.readValue(req.body(), JsonNode.class);
                System.out.println(body.size());
                /*for (int i = 0; i < body.size(); i++)
                    System.out.println(body.get(i).get("name").toString() + " : " + body.get(i).get("value").toString());*/
            } catch (Exception e) {System.out.println("error!!");}

            String command = req.queryParams("command");
            return execute(command);
        });
    }

    public static void runCommands() {
        Scanner scanner = new Scanner(System.in);

        boolean end = false;
        while (!end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();
            String output = execute(command);

            if (output == null)
                end = true;
            else System.out.print(output);
        }

        System.out.println();
    }

    private static String execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit")) {
            return null;
        }

        // ---------- Command Processing ---------- \\
        Primitive result = Parser.parse(command).solve();

        if (result.id().equals("null"))
            return "";

        if (result.printValue) {
            return result.string() + "\n\n";
        }
        else {
            result.printValue = true;
            return "";
        }
    }
}
