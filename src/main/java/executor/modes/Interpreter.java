package executor.modes;

import executor.ExecutorException;
import executor.modes.ExecutionStrategy;
import executor.visitors.InterpreterNodeVisitor;
import parser.node.Node;

public class Interpreter implements ExecutionStrategy {

    private final InterpreterNodeVisitor astNodeVisitor;

    public Interpreter() {
        astNodeVisitor = new InterpreterNodeVisitor();
    }

    @Override
    public void execute(Node ast) {
        try {
            ast.accept(astNodeVisitor);
        } catch (ExecutorException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
