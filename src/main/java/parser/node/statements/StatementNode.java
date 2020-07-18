package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenFound;
import parser.ParserException;
import parser.node.Node;
import parser.node.visitor.NodeVisitor;

import java.util.List;

public class StatementNode extends Node {

    public StatementNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
    }

    @Override
    public Node nextNode() throws ParserException {
        Node newChild;
        switch (getNodeValue().getType()) {
            case KEYWORD_IF:
                newChild = new IfNode(getNodeValue(), getTokens()).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            case KEYWORD_IMPORT:
                newChild = new ImportNode(getNodeValue(), getTokens()).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            case METHOD:
                newChild = new MethodNode(getNodeValue(), getTokens()).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            case ASSIGN_TYPE:
                newChild = new VarCreationNode(getNodeValue(), getTokens()).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            case IDENTIFIER:
                newChild = new VarReassignmentNode(getNodeValue(), getTokens()).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            default:
                throw new ParserException(ParserException.unexpectedTokenErrorMessage(getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node: StatementNode";
    }

}
