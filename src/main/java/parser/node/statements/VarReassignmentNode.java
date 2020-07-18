package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenType;
import parser.ParserException;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.visitor.NodeVisitor;
import lexer.token.TokenFound;

import java.util.List;

public class VarReassignmentNode extends Node {

    public VarReassignmentNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
    }

    @Override
    public Node nextNode() throws ParserException {
        if (getTokens().isEmpty()) {
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("=", getNodeValue()));
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case ASSIGN_FUNC:
                token = getTokens().remove(0);
                Node newChild = new ExpressionNode(token, getTokens(), TokenType.LINE_END).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            default:
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("=", getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node: VarReassignmentNode\n"
                + getNodeValue().toString();
    }

}
