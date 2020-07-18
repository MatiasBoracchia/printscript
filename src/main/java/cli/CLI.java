package cli;

import executor.Executor;
import executor.ExecutorException;
import executor.modes.ExecutionStrategy;
import executor.modes.Interpreter;
import executor.modes.Validator;
import lexer.Lexer;
import lexer.token.TokenFound;
import lexer.token.TokenRule;
import lexer.token.TokenType;
import parser.Parser;
import parser.node.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO Should CLI instantiate Lexer, Parser & Executor components? Should they be static?

public class CLI {

    private static final Scanner SCANNER = new Scanner(System.in);

    public CLI() {}

    public static void run(String[] args) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        startCLI();
        if(args.length == 0) {
            List<TokenFound> lexStepResult = lexStep(lexer);
            if(!lexStepResult.isEmpty()) {
                Node ast = parseStep(parser, lexStepResult);
                ast.printNode("");
                execute(ast);
            }
        } else {
            // readArgs(args)
        }
    }

    private static List<TokenFound> lexStep(Lexer lexer) {
        System.out.print("Enter file path: ");
        String filePath = SCANNER.nextLine();
        lexer = defineLexOptions(lexer);
        List<TokenFound> tokenFoundList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            System.out.println("Found file: " + filePath);
            System.out.println("\nStarting lexical analysis...\n");
            tokenFoundList = lexer.analyze(reader);
            if(!tokenFoundList.isEmpty()) {
                for (TokenFound token : tokenFoundList) {
                    System.out.println(token.toString());
                }
                return tokenFoundList;
            } else {
                System.out.println("No tokens found");
            }
            lexStep(lexer);
        } catch (IOException e) {
            e.printStackTrace();
            lexStep(lexer);
        }
        return tokenFoundList;
    }

    private static Lexer defineLexOptions(Lexer lexer) {
        System.out.print("Should allow booleans? [y/n]: ");
        String optBool = SCANNER.nextLine();
        System.out.print("Should allow boolean operators (>,>=,<,<=)? [y/n]: ");
        String optBoolOp = SCANNER.nextLine();
        System.out.print("Should allow constants? [y/n]: ");
        String optConst = SCANNER.nextLine();
        switch (optBool.toLowerCase()) {
            case "y":
            case "yes":
                lexer.addRule(new TokenRule(TokenType.VAR_TYPE, "\\b(number)\\b|\\b(string)\\b|\\b(boolean)\\b", true));
                lexer.addRule(new TokenRule(TokenType.BOOLEAN, "\\b(true)\\b|\\b(false)\\b", true));
        }
        switch (optBoolOp.toLowerCase()) {
            case "y":
            case "yes":
                lexer.addRule(new TokenRule(TokenType.BINARY_OPERATOR, "[+|\\-|*|/|>|<]{1}|>=|<=|!=|==", true));
        }
        switch (optConst.toLowerCase()) {
            case "y":
            case "yes":
                lexer.addRule(new TokenRule(TokenType.ASSIGN_TYPE, "\\b(let)\\b|\\b(const)\\b", true));
        }
        return lexer;
    }

    private static Node parseStep(Parser parser, List<TokenFound> tokens) {
        System.out.println("\nStarting syntax analysis...\n");
        return parser.analyze(tokens);
    }

    private static void execute(Node ast) {
        ExecutionStrategy strategy = null;
        while (strategy == null) {
            System.out.print("Enter execution mode (validate[v]/interpret[i]): ");
            String executionMode = SCANNER.nextLine();
            switch (executionMode.toLowerCase()) {
                case "v":
                case "validate":
                    strategy = new Validator();
                    break;
                case "i":
                case "intepret":
                    strategy = new Interpreter();
                    break;
                default:
                    System.out.println("Please select a valid mode of execution!");
            }
        }
        Executor executor = new Executor(strategy);
        try {
            executor.execute(ast);
            System.out.println("\n" + "### No errors found :)");
        } catch (ExecutorException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void startCLI() {
        String asciiArt =
                "                                                                                                                              \n" +
                "        ##### ##                                                #######                                                       \n" +
                "     ######  /###                 #                           /       ###                          #                          \n" +
                "    /#   /  /  ###               ###                   #     /         ##                         ###                   #     \n" +
                "   /    /  /    ###               #                   ##     ##        #                           #                   ##     \n" +
                "       /  /      ##                                   ##      ###                                                      ##     \n" +
                "      ## ##      ## ###  /###   ###   ###  /###     ######## ## ###           /###   ###  /###   ###        /###     ######## \n" +
                "      ## ##      ##  ###/ #### / ###   ###/ #### / ########   ### ###        / ###  / ###/ #### / ###      / ###  / ########  \n" +
                "    /### ##      /    ##   ###/   ##    ##   ###/     ##        ### ###     /   ###/   ##   ###/   ##     /   ###/     ##     \n" +
                "   / ### ##     /     ##          ##    ##    ##      ##          ### /##  ##          ##          ##    ##    ##      ##     \n" +
                "      ## ######/      ##          ##    ##    ##      ##            #/ /## ##          ##          ##    ##    ##      ##     \n" +
                "      ## ######       ##          ##    ##    ##      ##             #/ ## ##          ##          ##    ##    ##      ##     \n" +
                "      ## ##           ##          ##    ##    ##      ##              # /  ##          ##          ##    ##    ##      ##     \n" +
                "      ## ##           ##          ##    ##    ##      ##    /##        /   ###     /   ##          ##    ##    ##      ##     \n" +
                "      ## ##           ###         ### / ###   ###     ##   /  ########/     ######/    ###         ### / #######       ##     \n" +
                " ##   ## ##            ###         ##/   ###   ###     ## /     #####        #####      ###         ##/  ######         ##    \n" +
                "###   #  /                                                |                                              ##                   \n" +
                " ###    /                                                  \\)                                            ##                  \n" +
                "  #####/                                                                                                 ##                   \n" +
                "    ###                                                                                                   ##                  \n" +
                "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                "\n";
        System.out.println(asciiArt);
    }
}
