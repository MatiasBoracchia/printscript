package lexer.dictionary;

import lexer.token.TokenFound;
import lexer.token.TokenRule;
import lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class DictionaryImpl implements Dictionary {

    private List<TokenRule> rules;

    public DictionaryImpl() {
        rules = basicRules();
    }

    @Override
    public List<TokenFound> getTokensFromString(String text, int line) {
        return getTokenList(text, line, new ArrayList<>(), 0, 1);
    }

    @Override
    public void addRule(TokenRule newRule) {
        if (rules.contains(newRule)) {
            int oldIndex = rules.indexOf(newRule);
            rules.add(oldIndex, newRule);
        } else {
            rules.add(0, newRule);
        }
    }

    private List<TokenRule> basicRules() {
        List<TokenRule> basicRules = new ArrayList<>();
        basicRules.add(new TokenRule(TokenType.KEYWORD_IF, "\\b(if)\\b", true));
        basicRules.add(new TokenRule(TokenType.KEYWORD_ELSE, "\\b(else)\\b", true));
        basicRules.add(new TokenRule(TokenType.KEYWORD_IMPORT, "\\b(import)\\b", true));
        basicRules.add(new TokenRule(TokenType.METHOD, "\\b(print)\\b", true));
        basicRules.add(new TokenRule(TokenType.CURLY_OPEN, "[{]{1}", true));
        basicRules.add(new TokenRule(TokenType.CURLY_CLOSE, "[}]{1}", true));
        basicRules.add(new TokenRule(TokenType.PARENTHESIS_OPEN, "[(]{1}", true));
        basicRules.add(new TokenRule(TokenType.PARENTHESIS_CLOSE, "[)]{1}", true));
        basicRules.add(new TokenRule(TokenType.LINE_END, "[;]{1}", true));
        basicRules.add(new TokenRule(TokenType.BINARY_OPERATOR, "[+|-|*|/]{1}", true));
        basicRules.add(new TokenRule(TokenType.ASSIGN_TYPE, "\\b(let)\\b", true));
        basicRules.add(new TokenRule(TokenType.ASSIGN_FUNC, "[=]{1}", true));
        basicRules.add(new TokenRule(TokenType.VAR_TYPE_DEFF, "[:]{1}", true));
        basicRules.add(new TokenRule(TokenType.VAR_TYPE, "\\b(number)\\b|\\b(string)\\b", true));
        basicRules.add(new TokenRule(TokenType.STRING, "\"\\w*\"|'\\w*'", true));
        basicRules.add(new TokenRule(TokenType.NUMBER, "(\\d*\\.\\d+|\\d+)", true));
        basicRules.add(new TokenRule(TokenType.IDENTIFIER, "[a-zA-Z]+\\w*", true));
        basicRules.add(new TokenRule(TokenType.COMMENT, "[/]{2}.*\\n|(/\\*).*(\\*/)", false));
        basicRules.add(new TokenRule(TokenType.SPACER, "\\s", false));
        return basicRules;
    }

    private List<TokenFound> getTokenList(String text, int line, List<TokenFound> tokenFoundList, int columnFrom, int columnTo) {
        if (columnTo > text.length()) {
            return tokenFoundList;
        }
        String tokenCandidate = text.substring(columnFrom, columnTo);
        for (TokenRule rule : rules) {
            if (rule.matches(tokenCandidate)) {
                columnTo = getEndOfToken(text, columnFrom, columnTo, tokenCandidate, rule);
                tokenCandidate = text.substring(columnFrom, columnTo);
                if (rule.getType().equals(TokenType.IDENTIFIER)) {
                    for (TokenRule otherRules : rules) {
                        if (otherRules.matches(tokenCandidate) && otherRules.shouldGenerateToken()) {
                            tokenFoundList.add(new TokenFound(otherRules.getType(), tokenCandidate, line, columnFrom, columnTo));
                            break;
                        }
                    }
                } else if (rule.getType().equals(TokenType.ASSIGN_FUNC) && (columnTo < text.length() - 1)) {
                    if (text.substring(columnFrom, columnTo + 1).equals("==")) {
                        columnTo++;
                        tokenCandidate = text.substring(columnFrom, columnTo);
                        tokenFoundList.add(new TokenFound(TokenType.BINARY_OPERATOR, tokenCandidate, line, columnFrom, columnTo));
                    } else {
                        if (rule.shouldGenerateToken()) {
                            tokenFoundList.add(new TokenFound(rule.getType(), tokenCandidate, line, columnFrom, columnTo));
                        }
                    }
                } else {
                    if (rule.shouldGenerateToken()) {
                        tokenFoundList.add(new TokenFound(rule.getType(), tokenCandidate, line, columnFrom, columnTo));
                    }
                }
                columnFrom = columnTo;
                break;
            }
        }
        columnTo++;
        return getTokenList(text, line, tokenFoundList, columnFrom, columnTo);
    }

    private int getEndOfToken(String text, int columnFrom, int columnTo, String tokenCandidate, TokenRule rule) {
        if (!rule.matches(tokenCandidate)) {
            return columnTo - 1;
        }
        if (columnTo >= text.length()) {
            return columnTo;
        }
        columnTo++;
        tokenCandidate = text.substring(columnFrom, columnTo);
        return getEndOfToken(text, columnFrom, columnTo, tokenCandidate, rule);
    }

}
