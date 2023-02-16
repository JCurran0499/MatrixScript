package Program;

import java.util.Scanner;
import java.util.TreeMap;

import Interpreters.*;
import Parser.Parser;
import Interpreters.Variables.VarHandler;

import static spark.Spark.get;
import static spark.Spark.port;
import spark.Request;
import spark.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MatrixScape {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }
    }

    public static void runAPI() {
        port(4567);
        get("/", (req, res) -> {
            VarHandler.api = true;
            VarHandler.session = req.session();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode body;
            try {
                body = mapper.readValue(req.body(), JsonNode.class).get("command");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            String command = body.asText();
            return execute(command);
        });
    }

    public static void runCommands() {
        VarHandler.api = false;
        VarHandler.variables = new TreeMap<>();

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
