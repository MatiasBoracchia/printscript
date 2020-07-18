package parser.node.statements;

import executor.ExecutorException;
import lexer.token.TokenFound;
import parser.ParserException;
import parser.node.Node;
import parser.node.visitor.NodeVisitor;

import java.util.List;

public class ImportNode extends Node {

    private TokenFound string;

    public ImportNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
        string = null;
    }

    @Override
    public Node nextNode() throws ParserException {
        if (getTokens().isEmpty()) {
            if (string == null)
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("STRING_LITERAL", getNodeValue()));
            throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(";", string));
        }
        TokenFound token = getTokens().remove(0);
        switch (token.getType()) {
            case STRING:
                if (string != null)
                    throw new ParserException(ParserException.expectedAfterPreviousErrorMessage(";", string));
                string = token;
                return nextNode();
            case LINE_END:
                if (string == null)
                    throw new ParserException(ParserException.unexpectedTokenErrorMessage(token));
                return this;
            default:
                throw new ParserException(ParserException.expectedAfterPreviousErrorMessage("STRING_LITERAL", getNodeValue()));
        }
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Node: ImportNode\n"
                + getNodeValue().toString() +
                "\n|"
                + string.toString();
    }

}
