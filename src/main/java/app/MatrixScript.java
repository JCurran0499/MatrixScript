package app;

import app.api.Api;
import app.commands.Commands;

public class MatrixScript {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("run"))
            Commands.runCommands();
        else {
            Api.runAPI();
        }
    }
}
