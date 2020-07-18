package parser.node;

import lexer.token.TokenType;

public class NodeRule {

    private Node nodeToGenerate;
    private TokenType nodeTrigger;
    private TokenType nodeTerminator;
    private boolean shouldGenerateNode;

    public NodeRule(Node nodeToGenerate,
                    TokenType nodeTrigger,
                    TokenType nodeTerminator,
                    boolean shouldGenerateNode) {
        this.nodeToGenerate = nodeToGenerate;
        this.nodeTrigger = nodeTrigger;
        this.nodeTerminator = nodeTerminator;
        this.shouldGenerateNode = shouldGenerateNode;
    }

    public Node getNode() {
        return nodeToGenerate;
    }

    public TokenType getNodeTriggers() {
        return nodeTrigger;
    }

    public TokenType getNodeTerminators() {
        return nodeTerminator;
    }

    public boolean shouldGenerateNode() {
        return shouldGenerateNode;
    }

    public boolean isNodeTrigger(TokenType tokenType) {
        return tokenType.getId() == nodeTrigger.getId();
    }

    public boolean isNodeTerminator(TokenType tokenType) {
        return tokenType.getId() == nodeTerminator.getId();
    }

}
