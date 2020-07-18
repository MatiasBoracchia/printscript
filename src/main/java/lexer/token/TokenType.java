package lexer.token;

public enum TokenType {

    KEYWORD_IF              (1, "KEYWORD_IF"),
    KEYWORD_ELSE            (2, "KEYWORD_ELSE"),
    KEYWORD_IMPORT          (3, "KEYWORD_IMPORT"),
    METHOD                  (4, "METHOD"),
    CURLY_OPEN              (5, "CURLY_OPEN"),
    CURLY_CLOSE             (6, "CURLY_CLOSE"),
    PARENTHESIS_OPEN        (7, "PARENTHESIS_OPEN"),
    PARENTHESIS_CLOSE       (8, "PARENTHESIS_CLOSE"),
    LINE_END                (9, "LINE_END"),
    ASSIGN_TYPE             (10, "ASSIGN_TYPE"),
    ASSIGN_FUNC             (11, "ASSIGN_FUNC"),
    VAR_TYPE_DEFF           (12, "VAR_TYPE_DEFF"),
    VAR_TYPE                (13, "VAR_TYPE"),
    BINARY_OPERATOR         (14, "BINARY_OPERATOR"),
    STRING                  (15, "STRING"),
    NUMBER                  (16, "NUMBER"),
    BOOLEAN                 (17, "BOOLEAN"),
    IDENTIFIER              (18, "IDENTIFIER"),
    COMMENT                 (19, "COMMENT"),
    SPACER                  (20, "SPACER");

    TokenType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private final int id;
    private final String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
