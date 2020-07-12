package lpij;

import java.util.HashMap;
import java.util.Map;

/**
 * the int[] that the Interpreter uses to store all the variables, should be as small as possible
 * however in the program it's possible that the user "skips" some variables and just uses high values
 * without using some of the lower ones (example: when {x1, x2, x5, x6, x89} are the used variables,
 * we wouldn't want the array to be of size 90)
 * to only have an array that stores the relevant variables this class identifies all of the ones
 * that are really used and returns the mapping for them to the underlying array, as well as the total amount
 * of variables used
 */
public class VariableMapper implements AbstractLoopProgram.Visitor<Void> {
    //starting at one since this is always the return value of the loop program
    private int length = 1;
    private final Map<Integer, Integer> variableMap = new HashMap<>();
    private final AbstractLoopProgram program;

    public VariableMapper(AbstractLoopProgram program) {
        this.program = program;
        variableMap.put(0, 0);
    }

    public VariableMapping mapVariables() {
        program.accept(this);
        return new VariableMapping(variableMap, length);
    }

    private void checkVariable(int i) {
        if (!variableMap.containsKey(i)) {
            variableMap.put(i, length);
            length++;
        }
    }

    @Override
    public Void visitAssignment(AbstractLoopProgram.Assignment assignment) {
        checkVariable((Integer) assignment.leftVariable.value);
        checkVariable((Integer) assignment.rightVariable.value);

        return null;
    }

    @Override
    public Void visitGrouping(AbstractLoopProgram.Grouping grouping) {
        grouping.left.accept(this);
        grouping.right.accept(this);

        return null;
    }

    @Override
    public Void visitLoop(AbstractLoopProgram.Loop loop) {
        checkVariable((Integer) loop.loopVariable.value);
        loop.body.accept(this);

        return null;
    }
}
