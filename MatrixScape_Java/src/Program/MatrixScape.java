package Program;

import java.util.Scanner;
import Interpreters.*;
import Interpreters.Primitives.Set;
import Parser.Parser;

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

            if (result.id().equals("set"))
                ((Set) result).addVariable();

            System.out.println(result.string());
            System.out.println();
        }
    }

}
