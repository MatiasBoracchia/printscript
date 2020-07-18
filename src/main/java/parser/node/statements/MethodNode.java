package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenType;
import parser.ParserException;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.visitor.NodeVisitor;
import lexer.token.TokenFound;

import java.util.List;

public class MethodNode extends Node {

    public MethodNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
    }

    @Override
    public Node nextNode() throws ParserException {
        if (getTokens().isEmpty()) {
            if (hasChildren()) throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(";", lastToken()));
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case PARENTHESIS_OPEN:
                if (!hasChildren()) {
                    token = getTokens().remove(0);
                    Node newChild = new ExpressionNode(token, getTokens(), TokenType.PARENTHESIS_CLOSE).nextNode();
                    setTokens(newChild.getTokens());
                    addChild(newChild);
                    return nextNode();
                } else {
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                }
            case LINE_END:
                if (hasChildren()) return this;
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
            default:
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node: MethodNode\n"
                + getNodeValue().toString();
    }

    private TokenFound lastToken() {
        return lastToken(this);
    }

    private TokenFound lastToken(Node lastNode) {
        if (!lastNode.hasChildren()) {
            return lastNode.getNodeValue();
        }
        return lastToken(lastNode.getChildren().get(lastNode.getChildren().size() - 1));
    }

}
