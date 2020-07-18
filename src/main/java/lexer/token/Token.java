package lexer.token;

import java.util.Objects;

public class Token {

    protected TokenType type;
    protected String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token {" + type.getName() + ", value = '" + value + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

}
