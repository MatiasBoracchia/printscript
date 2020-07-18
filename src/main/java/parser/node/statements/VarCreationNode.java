package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenFound;
import lexer.token.TokenType;
import parser.ParserException;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.visitor.NodeVisitor;

import java.util.List;

public class VarCreationNode extends Node {

    private TokenFound identifier;
    private TokenFound varTypeDeff;
    private TokenFound varType;

    public VarCreationNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
        identifier = null;
        varTypeDeff = null;
        varType = null;
    }

    @Override
    public Node nextNode() throws ParserException {
        if (getTokens().isEmpty()) {
            return throwErrors();
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case IDENTIFIER:
                if (identifier != null)
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                identifier = token;
                return nextNode();
            case VAR_TYPE_DEFF:
                if (identifier == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("IDENTIFIER", getNodeValue()));
                if (varTypeDeff != null)
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                varTypeDeff = token;
                return nextNode();
            case VAR_TYPE:
                if (identifier == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("IDENTIFIER", getNodeValue()));
                if (varTypeDeff == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(":", getNodeValue()));
                if (varType != null)
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                varType = token;
                return nextNode();
            case ASSIGN_FUNC:
                if (identifier == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("IDENTIFIER", getNodeValue()));
                if (varTypeDeff == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(":", identifier));
                if (varType == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("VAR_TYPE", varTypeDeff));
                token = getTokens().remove(0);
                Node newChild = new ExpressionNode(token, getTokens(), TokenType.LINE_END).nextNode();
                setTokens(newChild.getTokens());
                addChild(newChild);
                return this;
            case LINE_END:
                if (identifier == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("IDENTIFIER", getNodeValue()));
                if (varTypeDeff == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(":", identifier));
                if (varType == null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("VAR_TYPE", varTypeDeff));
                return this;
            default:
                return throwErrors();
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node: VarCreationNode\n"
                + getNodeValue().toString() +
                "\n"
                + identifier.toString() +
                "\n"
                + varType.toString();
    }

    public TokenFound getIdentifier() {
        return identifier;
    }

    public TokenFound getVarType() {
        return varType;
    }

    private Node throwErrors() throws ParserException {
        if (identifier == null)
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("IDENTIFIER", getNodeValue()));
        if (varTypeDeff == null)
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(":", identifier));
        if (varType == null)
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("VAR_TYPE", varTypeDeff));
        throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("; or =", varType));
    }

}
