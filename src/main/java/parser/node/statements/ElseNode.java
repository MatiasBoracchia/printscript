package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenFound;
import parser.ParserException;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.visitor.NodeVisitor;

import java.util.List;

public class ElseNode extends Node {

    private final ExpressionNode condition;

    public ElseNode(TokenFound nodeValue, List<TokenFound> tokens, ExpressionNode condition) {
        super(nodeValue, tokens);
        this.condition = condition;
    }

    @Override
    public Node nextNode() throws ParserException {
        Node newChild;
        if (getTokens().isEmpty()) {
            if (hasChildren()) throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("}", lastToken()));
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("{", getNodeValue()));
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case CURLY_OPEN:
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
                if (hasChildren()) {
                    newChild = new StatementNode(token, getTokens()).nextNode();
                    setTokens(newChild.getTokens());
                    addChild(newChild);
                    return nextNode();
                }
                throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
            case CURLY_CLOSE:
                if (!hasChildren())
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                return this;
            default:
                if (hasChildren()) throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("}", lastToken()));
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("{", getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String s = "Node: ElseNode\n"
                + getNodeValue().toString() +
                "\nCondition"
                + condition.toString();
        if (hasChildren()) {
            for (Node child : getChildren()) {
                child.printNode("");
            }
        }
        return s;
    }

    public ExpressionNode getCondition() {
        return condition;
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
