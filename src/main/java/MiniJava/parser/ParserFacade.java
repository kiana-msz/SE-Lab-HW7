package MiniJava.parser;

import java.util.Scanner;

public class ParserFacade {
    private Parser parser;

    public ParserFacade() {
        parser = new Parser();
    }


    public void parse(Scanner scanner) {
        try {
            parser.startParse(scanner);
        } catch (Exception e) {
            System.err.println("An error occurred during parsing: " + e.getMessage());
        }
    }
}
