package Program;

import java.util.ArrayList;
import java.util.Scanner;
import Interpreters.*;
import Parser.Parser;
import Parser.Token;

public class MatrixScape {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">> ");
            String command = scanner.nextLine();

            if (command.equals("quit") || command.equals("exit"))
                break;

            Primitive result = Parser.parse(command).solve();

            if (result.id().equals("null"))
                continue;

            System.out.println(result.string());
            System.out.println();
        }
    }

}
