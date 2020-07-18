package executor.modes;

import executor.ExecutorException;
import parser.node.Node;

public interface ExecutionStrategy {

    void execute(Node ast) throws ExecutorException;

}
