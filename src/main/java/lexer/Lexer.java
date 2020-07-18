package lexer;

import lexer.dictionary.Dictionary;
import lexer.dictionary.DictionaryImpl;
import lexer.token.TokenFound;
import lexer.token.TokenRule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final Dictionary dictionary;

    public Lexer() {
        this.dictionary = new DictionaryImpl();
    }

    public List<TokenFound> analyze(BufferedReader reader) throws IOException {
        int lineIndex = 1;
        String line;
        List<TokenFound> tokenFoundList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            tokenFoundList.addAll(dictionary.getTokensFromString(line, lineIndex));
            lineIndex++;
        }
        return tokenFoundList;
    }

    public void addRule(TokenRule newRule) {
        dictionary.addRule(newRule);
    }

}
