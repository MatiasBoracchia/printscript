package executor.visitors;

import executor.ExecutorException;
import parser.node.expressions.ExpressionNode;
import parser.node.statements.*;

public class ValidatorNodeVisitor extends ExecutorNodeVisitor {

    @Override
    public void visit(MethodNode methodNode) throws ExecutorException {
        System.out.println("### Performing print method...");
        getExpressionNodeValue((ExpressionNode) methodNode.getChildren().get(0), null);
    }

}
