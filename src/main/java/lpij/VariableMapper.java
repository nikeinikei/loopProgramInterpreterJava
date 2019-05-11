package lpij;

import java.util.HashMap;
import java.util.Map;

public class VariableMapper implements AbstractLoopProgram.Visitor<Void> {
    //starting at one since this is always the return value of the loop program
    private int length = 1;
    private Map<Integer, Integer> variableMap = new HashMap<>();
    private AbstractLoopProgram program;

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
