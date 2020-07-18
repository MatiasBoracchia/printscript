package parser.node.expressions;

import executor.ExecutorException;
import parser.ParserException;
import parser.node.Node;
import parser.node.visitor.NodeVisitor;
import lexer.token.TokenFound;

import java.util.List;

public class LeafNode extends Node {

    public LeafNode(TokenFound nodeValue, List<TokenFound> tokens) {
        super(nodeValue, tokens);
    }

    @Override
    public Node nextNode() throws ParserException {
        return null;
    }

    @Override
    public void accept(NodeVisitor visitor) throws ExecutorException {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return getNodeValue().toString();
    }

}
