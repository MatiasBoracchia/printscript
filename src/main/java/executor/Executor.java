package executor;

import executor.modes.ExecutionStrategy;
import parser.node.Node;

public class Executor {

    private ExecutionStrategy mode;

    public Executor(ExecutionStrategy mode) {
        this.mode = mode;
    }

    public void execute(Node ast) throws ExecutorException {
        mode.execute(ast);
    }

}
