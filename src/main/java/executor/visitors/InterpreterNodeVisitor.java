package executor.visitors;

import executor.ExecutorException;
import parser.node.expressions.ExpressionNode;
import parser.node.statements.*;

public class InterpreterNodeVisitor extends ExecutorNodeVisitor {

    @Override
    public void visit(MethodNode methodNode) throws ExecutorException {
        System.out.println("### Performing print method...");
        System.out.println(getExpressionNodeValue((ExpressionNode) methodNode.getChildren().get(0), null));
    }



}
