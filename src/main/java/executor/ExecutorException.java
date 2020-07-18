package executor;

import executor.variable.VariableType;
import lexer.token.Token;
import lexer.token.TokenFound;

public class ExecutorException extends Exception {

    public ExecutorException(String message) {
        super(message);
    }

    public static String undefinedVariable(TokenFound token) {
        return "Undefined variable '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String unmutableVariable(TokenFound token) {
        return "Can't reassign constant variable '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String alreadyDefinedVariable(TokenFound token) {
        return "Can't declare already declared variable '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String unsopportedType(TokenFound token) {
        return "Unsopported variable type '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String unsopportedType(Token token) {
        return "Unsopported variable type '"
                + token.getType().getName() +
                "'.";
    }

    public static String unsopportedType(VariableType type) {
        return "Unsopported variable type '"
                + type.name() +
                ").";
    }

    public static String typeMismatch(TokenFound token) {
        return "Type mismatch for variable type '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String invalidExpression(TokenFound token) {
        return "Invalid expression starting with token '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

}
