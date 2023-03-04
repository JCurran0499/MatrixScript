package Program;

import java.util.Scanner;
import java.util.TreeMap;

import Interpreters.*;
import Interpreters.Primitives.Null;
import Parser.Parser;
import Interpreters.Variables.VarHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

import static spark.Spark.*;

public class MatrixScape {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            runCommands();
        else {
            runAPI();
        }
    }

    public static void runAPI() {
        Dotenv dotenv = Dotenv.load();

        port(4567);

        options("/*", (req, res) -> {
            res.header("Access-Control-Allow-Methods", "POST,OPTIONS");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type");
            res.header("Access-Control-Expose-Headers", "set-cookie");

            return "OK";
        });

        post("/", (req, res) -> {
            VarHandler.api = true;
            VarHandler.session = req.session();

            System.out.println(req.session().isNew());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode body;
            try {
                body = mapper.readTree(req.body()).get("command");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            res.header("Access-Control-Allow-Methods", "POST,OPTIONS");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type,cache-control");
            res.header("Access-Control-Expose-Headers", "set-cookie");

            res.header("Cache-Control", "no-cache");

            res.header("Content-Type", "application/json");

            res.cookie("SESSIONID", req.session().id(), 3600, false);
            res.cookie("SESSIONIDsecure", req.session().id(), 3600, true);

            String command = body.asText();
            Primitive result = execute(command);

            String response;
            if (result.id().equals("mat") && result.printValue) {
                String matString = result.string().replaceAll("\n", "n");
                response = String.format("{\"response\": {\"matrix\": \"%s\"}}", matString);
                return mapper.readTree(response);
            }
            else if (result.printValue) {
                response = String.format("{\"response\": \"%s\"}", result.string());
                return mapper.readTree(response);
            }
            else {
                result.printValue = true;
                response = String.format("{\"response\": \"%s\"}", "");
                return mapper.readTree(response);
            }
        });
    }

    public static void runCommands() {
        VarHandler.api = false;
        VarHandler.variables = new TreeMap<>();

        Scanner scanner = new Scanner(System.in);

        boolean end = true;
        while (end) {
            System.out.print(">> ");
            String command = scanner.nextLine().strip();

            if (command.equals("quit") || command.equals("exit"))
                end = false;
            else {

                Primitive result = execute(command);
                if (!result.id().equals("null") && result.printValue)
                    System.out.println(result.string() + "\n");
                else
                    result.printValue = true;
            }
        }

        System.out.println();
    }

    private static Primitive execute(String command) {
        if (command.contains("//"))
            command = command.substring(0, command.indexOf("//")).stripTrailing();

        if (command.equals("quit") || command.equals("exit")) {
            return Null.returnNull();
        }

        return Parser.parse(command).solve();
    }
}
