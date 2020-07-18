package lexer.token;

public class TokenFound extends Token {

    private final int line;
    private final int columnFrom;
    private final int columnTo;

    public TokenFound(TokenType type,
                      String value,
                      int line,
                      int columnFrom,
                      int columnTo) {
        super(type, value);
        this.line = line;
        this.columnFrom = columnFrom;
        this.columnTo = columnTo;
    }

    public int getLine() {
        return line;
    }

    public int getColumnFrom() {
        return columnFrom;
    }

    public int getColumnTo() {
        return columnTo;
    }

    @Override
    public String toString() {
        return super.toString() + " on line: " + line + ", columns = (" + columnFrom + "," + columnTo + ")";
    }

}
