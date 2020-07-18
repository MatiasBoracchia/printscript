package executor.visitors;

import executor.ExecutorException;
import executor.variable.Variable;
import executor.variable.VariableType;
import lexer.token.Token;
import lexer.token.TokenFound;
import lexer.token.TokenType;
import parser.node.Node;
import parser.node.expressions.ExpressionNode;
import parser.node.expressions.LeafNode;
import parser.node.statements.*;
import parser.node.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class ExecutorNodeVisitor implements NodeVisitor {

    List<Variable> variables;

    public ExecutorNodeVisitor() {
        variables = new ArrayList<>();
    }

    @Override
    public void visit(ElseNode elseNode) throws ExecutorException {
        boolean conditionEval = getBool(getExpressionNodeValue(elseNode.getCondition(), VariableType.BOOLEAN));
        if (!conditionEval) {
            System.out.println("### Entered else block on line: " + elseNode.getNodeValue().getLine() + "...");
            for (Node child : elseNode.getChildren()) {
                child.accept(this);
            }
        }
    }

    @Override
    public void visit(IfNode ifNode) throws ExecutorException {
        boolean conditionEval = getBool(getExpressionNodeValue(ifNode.getCondition(), VariableType.BOOLEAN));
        Node elseNode = ifNode.getElseNode();
        if (conditionEval) {
            System.out.println("### Entered if block on line: " + ifNode.getNodeValue().getLine() + "...");
            for (Node child : ifNode.getChildren()) {
                child.accept(this);
            }
        } else if (elseNode != null) {
            elseNode.accept(this);
        }
    }

    @Override
    public void visit(ImportNode importNode) {

    }

    @Override
    public void visit(StatementNode statementNode) throws ExecutorException {
        for (Node child : statementNode.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(VarCreationNode varCreationNode) throws ExecutorException {
        TokenFound creatingToken = varCreationNode.getIdentifier();
        if (variablesContainIdentifier(creatingToken))
            throw new ExecutorException(ExecutorException.alreadyDefinedVariable(creatingToken));
        Variable newVar = new Variable(
                varCreationNode.getIdentifier().getValue(),
                null,
                VariableType.getType(varCreationNode.getVarType()),
                !varCreationNode.getNodeValue().getValue().equals("const")
        );
        if(varCreationNode.hasChildren()) {
            newVar.setValue(getExpressionNodeValue((ExpressionNode) varCreationNode.getChildren().get(0), newVar.getType()));
        }
        variables.add(newVar);
        System.out.println("### Declared " + (newVar.isMutable() ? "variable '" : "constant '")
                + newVar.getKey() +
                "' of type '"
                + newVar.getType().name() +
                "' with value '"
                + newVar.getValue() +
                "'...");
    }

    @Override
    public void visit(VarReassignmentNode varReassignmentNode) throws ExecutorException {
        TokenFound reassigningToken = varReassignmentNode.getNodeValue();
        Variable reassigningVar = getVariable(reassigningToken);
        if (reassigningVar == null) throw new ExecutorException(ExecutorException.undefinedVariable(reassigningToken));
        updateValue(reassigningToken, reassigningVar, getExpressionNodeValue((ExpressionNode) varReassignmentNode.getChildren().get(0), reassigningVar.getType()));
        System.out.println("### Reassigned variable '"
                + reassigningVar.getKey() +
                "' to value '"
                + reassigningVar.getValue() +
                "'...");
    }

    @Override
    public void visit(ExpressionNode expressionNode) {
    }

    @Override
    public void visit(LeafNode leafNode) {

    }

    public String getExpressionNodeValue(ExpressionNode expressionNode, VariableType expectedType) throws ExecutorException {
        Token expValue = getExpressionNodeValue(expressionNode);
        if (expectedType != null) {
            if (expectedType.equals(VariableType.getType(expValue))) {
                return expValue.getValue();
            }
            throw new ExecutorException("Type mismatch in expression return value. Expected: '"
                    + expectedType.name()
                    + "'. Received: '"
                    + expValue.getType().getName() + "'.");
        }
        return expValue.getValue();
    }

    private Token getExpressionNodeValue(ExpressionNode expressionNode) throws ExecutorException {
        ExpressionNode lhs = (ExpressionNode) expressionNode.getLHS();
        ExpressionNode rhs = (ExpressionNode) expressionNode.getRHS();
        if (lhs == null || rhs == null) {
            TokenFound token = expressionNode.getNodeValue();
            if (token.getType() == TokenType.IDENTIFIER) {
                if (variablesContainIdentifier(token)) {
                    Variable variable = getVariable(token);
                    if (variable == null) throw new ExecutorException(ExecutorException.undefinedVariable(token));
                    return new Token(VariableType.getType(variable.getType()), variable.getValue());
                }
            }
            return token;
        }
        return evaluateExp(getExpressionNodeValue(lhs), expressionNode.getNodeValue(), getExpressionNodeValue(rhs));
    }

    private Token evaluateExp(Token leftToken, TokenFound operator, Token rightToken) throws ExecutorException {
        switch (operator.getValue()) {
            case "+":
                if (leftToken.getType().getId() == TokenType.STRING.getId() || rightToken.getType().getId() == TokenType.STRING.getId()) {
                    String leftTokenVal;
                    String rightTokenVal;
                    if (leftToken.getType().getId() == TokenType.STRING.getId())
                        leftTokenVal = getStr(leftToken.getValue());
                    else leftTokenVal = leftToken.getValue();
                    if (rightToken.getType().getId() == TokenType.STRING.getId())
                        rightTokenVal = getStr(rightToken.getValue());
                    else rightTokenVal = rightToken.getValue();
                    return new Token(TokenType.STRING, "\"" + leftTokenVal + "" + rightTokenVal + "\"");
                }
                if (leftToken.getType().getId() == TokenType.NUMBER.getId() && rightToken.getType().getId() == TokenType.NUMBER.getId()) {
                    Double leftTokenVal = getNum(leftToken.getValue());
                    Double rightTokenVal = getNum(rightToken.getValue());
                    return new Token(TokenType.NUMBER, "" + (leftTokenVal + rightTokenVal));
                }
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
            case "/":
            case "*":
            case "-":
                if (leftToken.getType().getId() == TokenType.NUMBER.getId() && rightToken.getType().getId() == TokenType.NUMBER.getId()) {
                    Double leftTokenVal = getNum(leftToken.getValue());
                    Double rightTokenVal = getNum(rightToken.getValue());
                    switch (operator.getValue()) {
                        case "/":
                            return new Token(TokenType.NUMBER, "" + (leftTokenVal / rightTokenVal));
                        case "*":
                            return new Token(TokenType.NUMBER, "" + (leftTokenVal * rightTokenVal));
                        case "-":
                            return new Token(TokenType.NUMBER, "" + (leftTokenVal - rightTokenVal));
                    }
                }
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
            case "<=":
            case ">=":
            case ">":
            case "<":
                if (leftToken.getType().getId() == TokenType.NUMBER.getId() && rightToken.getType().getId() == TokenType.NUMBER.getId()) {
                    Double leftTokenVal = getNum(leftToken.getValue());
                    Double rightTokenVal = getNum(rightToken.getValue());
                    switch (operator.getValue()) {
                        case ">":
                            return new Token(TokenType.BOOLEAN, "" + (leftTokenVal > rightTokenVal));
                        case "<":
                            return new Token(TokenType.BOOLEAN, "" + (leftTokenVal < rightTokenVal));
                        case ">=":
                            return new Token(TokenType.BOOLEAN, "" + (leftTokenVal >= rightTokenVal));
                        case "<=":
                            return new Token(TokenType.BOOLEAN, "" + (leftTokenVal <= rightTokenVal));
                    }
                }
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
            case "==":
                if ((leftToken.getType().getId() == TokenType.BOOLEAN.getId() && rightToken.getType().getId() == TokenType.BOOLEAN.getId()) ||
                        (leftToken.getType().getId() == TokenType.NUMBER.getId() && rightToken.getType().getId() == TokenType.NUMBER.getId())) {
                    boolean leftTokenVal = getBool(leftToken.getValue());
                    boolean rightTokenVal = getBool(rightToken.getValue());
                    return new Token(TokenType.BOOLEAN, "" + (leftTokenVal == rightTokenVal));
                }
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
            case "!=":
                if ((leftToken.getType().getId() == TokenType.BOOLEAN.getId() && rightToken.getType().getId() == TokenType.BOOLEAN.getId()) ||
                        (leftToken.getType().getId() == TokenType.NUMBER.getId() && rightToken.getType().getId() == TokenType.NUMBER.getId())) {
                    boolean leftTokenVal = getBool(leftToken.getValue());
                    boolean rightTokenVal = getBool(rightToken.getValue());
                    return new Token(TokenType.BOOLEAN, "" + (leftTokenVal != rightTokenVal));
                }
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
            default:
                throw new ExecutorException(ExecutorException.invalidExpression(operator));
        }
    }

    protected boolean getBool(String value) {
        return Boolean.parseBoolean(value);
    }

    protected double getNum(String value) {
        return Double.parseDouble(value);
    }

    protected String getStr(String value) {
        return value.substring(1, value.length() - 1);
    }

    protected boolean variablesContainIdentifier(TokenFound token) {
        return getVariable(token) != null;
    }

    protected Variable getVariable(TokenFound token) {
        for (Variable var : variables) {
            if (var.equalsIdentifier(token)) return var;
        }
        return null;
    }

    protected void updateValue(TokenFound token, Variable reassigningVar, String expressionNodeValue) throws ExecutorException {
        if (!reassigningVar.isMutable())
            throw new ExecutorException(ExecutorException.unmutableVariable(token));
        reassigningVar.setValue(expressionNodeValue);
    }

}
