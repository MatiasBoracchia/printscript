package executor.modes;

import executor.ExecutorException;
import executor.modes.ExecutionStrategy;
import executor.variable.Variable;
import executor.visitors.ValidatorNodeVisitor;
import parser.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Validator implements ExecutionStrategy {

    private final ValidatorNodeVisitor astNodeVisitor;
    private List<Variable> variables;

    public Validator() {
        astNodeVisitor = new ValidatorNodeVisitor();
        variables = new ArrayList<>();
    }

    @Override
    public void execute(Node ast) throws ExecutorException {
        ast.accept(astNodeVisitor);
    }

}
