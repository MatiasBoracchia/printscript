package lexer.dictionary;

import lexer.token.TokenFound;
import lexer.token.TokenRule;

import java.util.List;

public interface Dictionary {

    List<TokenFound> getTokensFromString(String text, int line);

    void addRule(TokenRule newRule);

}
