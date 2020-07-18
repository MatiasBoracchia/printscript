package parser.node;

import executor.ExecutorException;
import lexer.token.TokenFound;
import parser.ParserException;
import parser.node.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public abstract class Node {

    TokenFound nodeValue;
    List<Node> children;
    private List<TokenFound> tokens;

    public Node(TokenFound nodeValue, List<Node> children, List<TokenFound> tokens) {
        this.nodeValue = nodeValue;
        this.children = children;
        this.tokens = tokens;
    }

    public Node(TokenFound nodeValue, List<TokenFound> tokens) {
        this.nodeValue = nodeValue;
        this.children = new ArrayList<>();
        this.tokens = tokens;
    }

    public Node(List<TokenFound> tokens) {
        this.nodeValue = null;
        this.children = new ArrayList<>();
        this.tokens = tokens;
    }

    public abstract Node nextNode() throws ParserException;

    public abstract void accept(NodeVisitor visitor) throws ExecutorException;

    public abstract String toString();

    public TokenFound getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(TokenFound nodeValue) {
        this.nodeValue = nodeValue;
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<TokenFound> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenFound> tokens) {
        this.tokens = tokens;
    }

    public void addChild(Node newNode) {
        children.add(newNode);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void printNode(String prefix) {
        System.out.println(prefix+toString().replaceAll("(?:\r\n?|\n)(?!\\z)", "$0"+ Matcher.quoteReplacement(prefix)));
        if(!children.isEmpty()) {
            System.out.println(prefix + "Children:");
            if (!prefix.equals("")) {
                prefix = "\t" + prefix;
            } else {
                prefix = "\t|--";
            }
            for (Node child: children) {
                child.printNode(prefix);
            }
        }
    }

}
