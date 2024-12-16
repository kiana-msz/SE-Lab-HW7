package MiniJava.parser;

import MiniJava.Log.Log;
import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.lexicalAnalyzer;
import MiniJava.scanner.token.Token;
import java.util.Scanner;

public class ParserFacade {
    private Parser parser;

    public ParserFacade() {
        parser = new Parser();
    }

    public void startParse(Scanner scanner) {
        parser.initializeParser();
        parser.loadRules();

        lexicalAnalyzer lexicalAnalyzer = new lexicalAnalyzer(scanner);
        Token lookAhead = lexicalAnalyzer.getNextToken();
        boolean finish = false;

        while (!finish) {
            try {
                Log.print(lookAhead.toString() + "\t" + parser.getParsStack().peek());
                Action currentAction = parser.getParseTable().getActionTable(parser.getParsStack().peek(), lookAhead);

                Log.print(currentAction.toString());
                switch (currentAction.action) {
                    case shift:
                        parser.shiftAction(lookAhead);
                        lookAhead = lexicalAnalyzer.getNextToken();
                        break;

                    case reduce:
                        parser.reduceAction(currentAction.number, lookAhead);
                        break;

                    case accept:
                        finish = true;
                        break;
                }
                Log.print("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!ErrorHandler.hasError) {
            parser.getCodeGeneratorFacade().printMemory();
        }
    }
}
