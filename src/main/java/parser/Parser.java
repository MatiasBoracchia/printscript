package parser;

import lexer.token.TokenFound;
import parser.node.Node;
import parser.node.statements.StatementNode;

import java.util.List;

public class Parser {

    public Parser() {
    }

    public Node analyze(List<TokenFound> tokens) {
        Node ast = null;
        while (!tokens.isEmpty()) {
            TokenFound token = tokens.remove(0);
            if(ast == null) {
                try {
                    ast = new StatementNode(token, tokens).nextNode();
                    tokens = ast.getTokens();
                } catch (ParserException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                try {
                    Node newChild = new StatementNode(token, tokens).nextNode().getChildren().get(0);
                    tokens = newChild.getTokens();
                    ast.addChild(newChild);
                } catch (ParserException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        return ast;
    }

}
