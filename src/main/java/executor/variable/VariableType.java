package executor.variable;

import executor.ExecutorException;
import lexer.token.Token;
import lexer.token.TokenType;

public enum VariableType {

    NUMBER      (0, "NUMBER"),
    STRING      (1, "STRING"),
    BOOLEAN     (2, "BOOLEAN");

    VariableType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private final int id;
    private final String name;

    @Override
    public String toString() {
        return "VariableType { id = " + id + ", name = '" + name + "' }";
    }

    public boolean equals(VariableType variableType) {
        return variableType.id == id;
    }

    public static VariableType getType(Token token) throws ExecutorException {
        String varTypeTokenValue = token.getValue().toUpperCase();
        String tokenTypeValue = token.getType().getName();
        if (NUMBER.name.equals(varTypeTokenValue) || NUMBER.name.equals(tokenTypeValue)) return NUMBER;
        else if (STRING.name.equals(varTypeTokenValue) || STRING.name.equals(tokenTypeValue)) return STRING;
        else if (BOOLEAN.name.equals(varTypeTokenValue) || BOOLEAN.name.equals(tokenTypeValue)) return BOOLEAN;
        throw new ExecutorException(ExecutorException.unsopportedType(token));
    }

    public static TokenType getType(VariableType variableType) throws ExecutorException {
        if (variableType.id == NUMBER.id) return TokenType.NUMBER;
        else if (variableType.id == STRING.id) return TokenType.STRING;
        else if (variableType.id == BOOLEAN.id) return TokenType.BOOLEAN;
        throw new ExecutorException(ExecutorException.unsopportedType(variableType));
    }

}
