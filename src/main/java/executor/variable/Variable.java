package executor.variable;

import executor.ExecutorException;
import lexer.token.Token;
import lexer.token.TokenFound;
import lexer.token.TokenType;
import parser.node.expressions.ExpressionNode;

public class Variable {

    private String key;
    private String value;
    private VariableType type;
    private boolean isMutable;

    public Variable(String key, String value, VariableType type, boolean isMutable) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.isMutable = isMutable;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public VariableType getType() {
        return type;
    }

    public VariableType getType(TokenFound token) throws ExecutorException {
        return VariableType.getType(token);
    }

    public boolean isMutable() {
        return isMutable;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Variable variable) {
        return key.equals(variable.getKey()) && value.equals(variable.getValue()) && type.equals(variable.getType()) && (isMutable == variable.isMutable());
    }

    public boolean equalsIdentifier(Token token) {
        return token.getValue().equals(key);
    }

}
