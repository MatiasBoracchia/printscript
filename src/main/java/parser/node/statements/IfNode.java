package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenType;
import parser.ParserException;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.visitor.NodeVisitor;
import lexer.token.TokenFound;

import java.util.List;

public class IfNode extends Node {

    private ExpressionNode condition;
    private Node elseNode;

    public IfNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
        condition = null;
        elseNode = null;
    }

    @Override
    public Node nextNode() throws ParserException {
        Node newChild;
        if (getTokens().isEmpty()) {
            if (hasChildren()) throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("}", lastToken()));
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case PARENTHESIS_OPEN:
                if (condition != null)
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                token = getTokens().remove(0);
                newChild = new ExpressionNode(token, getTokens(), TokenType.PARENTHESIS_CLOSE).nextNode();
                setTokens(newChild.getTokens());
                condition = (ExpressionNode) newChild;
                return this.nextNode();
            case CURLY_OPEN:
                if (condition == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
                if (!getTokens().isEmpty()) {
                    token = getTokens().remove(0);
                    newChild = new StatementNode(token, getTokens()).nextNode();
                    setTokens(newChild.getTokens());
                    addChild(newChild);
                    return nextNode();
                } throw new ParserException("Ran out of tokens!");
            case IDENTIFIER:
            case METHOD:
            case KEYWORD_IF:
            case KEYWORD_IMPORT:
            case ASSIGN_TYPE:
                if (condition == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
                if (hasChildren()) {
                    newChild = new StatementNode(token, getTokens()).nextNode();
                    setTokens(newChild.getTokens());
                    addChild(newChild);
                    return nextNode();
                }
                throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
            case CURLY_CLOSE:
                if (condition == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
                if (!hasChildren())
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                if (!getTokens().isEmpty() && (getTokens().get(0).getType().getId() == TokenType.KEYWORD_ELSE.getId())) {
                    token = getTokens().remove(0);
                    newChild = new ElseNode(token, getTokens(), this.condition).nextNode();
                    setTokens(newChild.getTokens());
                    elseNode = newChild;
                    return this;
                }
                return this;
            default:
                if (hasChildren()) throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("}", lastToken()));
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("(", getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String s = "Node: IfNode\n"
                + getNodeValue().toString() + "\n"
                + "Condition\n"
                + condition.toString();
        if (elseNode != null) {
            s += elseNode.toString();
        }
        return s;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public Node getElseNode() {
        return elseNode;
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
