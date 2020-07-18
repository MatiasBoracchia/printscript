package parser;

import lexer.token.TokenFound;

public class ParserException extends Exception {

    public ParserException (String message) {
        super(message);
    }

    public static String unexpectedTokenErrorMessage(TokenFound token) {
        return "Unexpected token '"
                + token.getValue() +
                "' at line: "
                + token.getLine() +
                ", between columns: ("
                + token.getColumnFrom() +
                ", "
                + token.getColumnTo() +
                ").";
    }

    public static String expectedAfterPreviousErrorMessage(String expected, TokenFound previousToken) {
        return "'"
                + expected +
                "' token expected immediately after '"
                + previousToken.getValue() +
                "' at line: "
                + previousToken.getLine() +
                ", between columns: ("
                + previousToken.getColumnFrom() +
                ", "
                + previousToken.getColumnTo() +
                ").";
    }

}
