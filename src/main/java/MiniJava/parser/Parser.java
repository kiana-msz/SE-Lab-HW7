package MiniJava.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import MiniJava.Log.Log;
import MiniJava.codeGenerator.CodeGeneratorFacade;
import MiniJava.scanner.token.Token;
import lombok.Getter;

public class Parser {
    private ArrayList<Rule> rules;
    @Getter
    private Stack<Integer> parsStack;
    @Getter
    private ParseTable parseTable;
    private CodeGeneratorFacade cgf;

    public Parser() {
        rules = new ArrayList<>();
        parsStack = new Stack<>();
        cgf = new CodeGeneratorFacade();
    }

    public void initializeParser() {
        parsStack.push(0); // Initial state
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRules() {
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shiftAction(Token lookAhead) {
        int nextState = parseTable.getActionTable(parsStack.peek(), lookAhead).number;
        parsStack.push(nextState);
    }

    public void reduceAction(int ruleIndex, Token lookAhead) {
        Rule rule = rules.get(ruleIndex);
        for (int i = 0; i < rule.RHS.size(); i++) {
            parsStack.pop();
        }

        int gotoState = parseTable.getGotoTable(parsStack.peek(), rule.LHS);
        parsStack.push(gotoState);

        try {
            cgf.semanticFunction(rule.semanticAction, lookAhead);
        } catch (Exception e) {
            Log.print("Code Generator Error");
        }
    }

    public CodeGeneratorFacade getCodeGeneratorFacade() {
        return cgf;
    }
}
