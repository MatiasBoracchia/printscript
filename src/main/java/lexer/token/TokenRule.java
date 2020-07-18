package lexer.token;

import java.util.Objects;
import java.util.regex.Pattern;

public class TokenRule {

    private final TokenType type;
    private final Pattern regex;
    private final boolean shouldGenerateToken;

    public TokenRule(TokenType type, String regex, boolean shouldGenerateToken) {
        this.type = type;
        this.regex = Pattern.compile(regex);
        this.shouldGenerateToken = shouldGenerateToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenRule tokenRule = (TokenRule) o;
        return type.getId() == tokenRule.type.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public boolean matches(String input) {
        return regex.matcher(input).matches();
    }

    public TokenType getType() {
        return type;
    }

    public boolean shouldGenerateToken() {
        return shouldGenerateToken;
    }

}
