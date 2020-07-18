package parser.node.expressions;

import executor.ExecutorException;
import lexer.token.TokenType;
import parser.ParserException;
import parser.node.Node;
import parser.node.visitor.NodeVisitor;
import lexer.token.TokenFound;

import java.util.List;

public class ExpressionNode extends Node {

    private final TokenType expressionEnd;
    private Node lhs;
    private Node rhs;

    public ExpressionNode(TokenFound nodeValue,
                          List<TokenFound> tokens,
                          TokenType expressionEnd) {
        super(nodeValue, tokens);
        this.expressionEnd = expressionEnd;
        lhs = null;
        rhs = null;
    }

    public ExpressionNode(TokenFound nodeValue,
                          List<TokenFound> tokens,
                          TokenType expressionEnd,
                          Node lhs) {
        super(nodeValue, tokens);
        this.expressionEnd = expressionEnd;
        this.lhs = lhs;
        rhs = null;
    }

    public ExpressionNode(TokenFound nodeValue,
                          List<TokenFound> tokens,
                          TokenType expressionEnd,
                          Node lhs,
                          Node rhs) {
        super(nodeValue, tokens);
        this.expressionEnd = expressionEnd;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    // TODO having trouble with '(' expression ')'
    @Override
    public Node nextNode() throws ParserException {
        if (getTokens().isEmpty())
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(expressionEnd.getName(), lastToken()));
        TokenFound token = getTokens().remove(0);
        if (expressionEnd.getId() == token.getType().getId()) {
            if (rhs == null && (getNodeValue().getType().getId() == TokenType.BINARY_OPERATOR.getId()))
                throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
            return this;
        }
        ExpressionNode node;
        switch (token.getType()) {
            case BINARY_OPERATOR:
                if ((lhs == null && rhs == null) || (lhs != null && rhs != null)) {
                    node = new ExpressionNode(token, getTokens(), this.expressionEnd);
                    node.setLHS(this);
                    ExpressionNode rhs = (ExpressionNode) node.nextNode();
                    node.setRHS(rhs);
                    return node.nextNode();
                } else {
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                }
            case IDENTIFIER:
            case STRING:
            case NUMBER:
            case BOOLEAN:
                if (lhs != null && rhs == null)
                    return new ExpressionNode(token, getTokens(), this.expressionEnd);
                else
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
            case PARENTHESIS_OPEN:
                if (lhs != null && rhs == null) {
                    if (!getTokens().isEmpty()) {
                        token = getTokens().remove(0);
                        node = (ExpressionNode) new ExpressionNode(token, getTokens(), TokenType.PARENTHESIS_CLOSE).nextNode();
                        setTokens(node.getTokens());
                        return node;
                    }
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                } else
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));

            default:
                throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String s = "Node: ExpressionNode\n";
        if ((lhs == null && rhs == null) || (lhs != null && rhs != null)) {
            s += getNodeValue().toString() + "\n";
            if (lhs != null) {
                s += "Left Branch: " + lhs.toString();
            }
            if (rhs != null) {
                s += "Right Branch: " + rhs.toString();
            }
        }
        return s;
    }

    public Node getLHS() {
        return lhs;
    }

    public void setLHS(Node lhs) {
        this.lhs = lhs;
    }

    public Node getRHS() {
        return rhs;
    }

    public void setRHS(Node rhs) {
        this.rhs = rhs;
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
