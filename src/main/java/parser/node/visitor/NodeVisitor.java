package parser.node.visitor;

import executor.ExecutorException;
import parser.node.expressions.ExpressionNode;
import parser.node.expressions.LeafNode;
import parser.node.statements.*;

public interface NodeVisitor {

    void visit(ElseNode elseNode) throws ExecutorException;

    void visit(IfNode ifNode) throws ExecutorException;

    void visit(ImportNode importNode) throws ExecutorException;

    void visit(MethodNode methodNode) throws ExecutorException;

    void visit(StatementNode statementNode) throws ExecutorException;

    void visit(VarCreationNode varCreationNode) throws ExecutorException;

    void visit(VarReassignmentNode varCreationNode) throws ExecutorException;

    void visit(ExpressionNode expressionNode) throws ExecutorException;

    void visit(LeafNode leafNode) throws ExecutorException;

}
