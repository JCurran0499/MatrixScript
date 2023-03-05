package Program;

import java.util.Scanner;
import java.util.TreeMap;

import java.util.UUID;

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
            res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type");

            return "OK";
        });

        ObjectMapper mapper = new ObjectMapper();

        get("/token", (req, res) -> {
            String sessionToken = UUID.randomUUID().toString();
            int success = VarHandler.createSession(sessionToken);
            if (success == -1)
                halt(500, "error generating token");

            res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type");

            res.header("Content-Type", "application/json");

            return mapper.readTree(String.format("{\"sessionToken\": \"%s\"}", sessionToken));
        });

        delete("/token/:token", (req, res) -> {
            int success = VarHandler.invalidateSession(req.params(":token"));
            if (success == -1)
                halt(404, "invalid session token");

            res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type");

            return "OK";
        });

        post("/", (req, res) -> {
            String sessionToken = req.queryParams("token");
            if (sessionToken == null) {
                sessionToken = req.session().id();
                VarHandler.createSession(sessionToken);
            }

            if (!VarHandler.validSession(sessionToken))
                halt(401, "invalid session token");
            VarHandler.variables = VarHandler.variableMap.get(sessionToken);

            JsonNode body;
            try {
                body = mapper.readTree(req.body()).get("command");
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            res.header("Access-Control-Allow-Methods", "POST,GET,DELETE");
            res.header("Access-Control-Allow-Origin", "http://" + dotenv.get("FRONTEND"));
            res.header("Access-Control-Allow-Credentials", "true");
            res.header("Access-Control-Allow-Headers", "content-type");

            res.header("Content-Type", "application/json");

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
