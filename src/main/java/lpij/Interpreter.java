package lpij;

import java.util.Arrays;

public class Interpreter implements AbstractLoopProgram.Visitor<Void> {
    private final int[] variableValues;
    private final VariableMapping variableMapping;
    private final AbstractLoopProgram program;

    public Interpreter(AbstractLoopProgram program, VariableMapping mapping) {
        this.program = program;
        this.variableMapping = mapping;
        this.variableValues = new int[variableMapping.length];
    }

    public int execute(int[] args) {
        Arrays.fill(variableValues, 0);

        //we need to put the initial variables in to their respective locations
        //in the variableValues array
        for (int i = 0; i < args.length; i++) {
            //offset of one since the first slot is reserved for the result (x0)
            int index = i + 1;
            var mappedIndex = variableMapping.get(index);

            //sanity checks
            if (mappedIndex != null && mappedIndex < variableValues.length) {
                variableValues[mappedIndex] = args[i];
            }
        }

        //run it
        program.accept(this);
        return variableValues[0];
    }

    @Override
    public Void visitAssignment(AbstractLoopProgram.Assignment assignment) {
        Integer index1 = (Integer) assignment.leftVariable.value;
        Integer index2 = (Integer) assignment.rightVariable.value;
        Integer number = (Integer) assignment.number.value;
        if (assignment.operator.type == TokenType.PLUS) {
            variableValues[variableMapping.get(index1)] = variableValues[variableMapping.get(index2)] + number;
        } else if (assignment.operator.type == TokenType.MINUS) {
            variableValues[variableMapping.get(index1)] = Math.max(variableValues[variableMapping.get(index2)] - number, 0);
        } else {
            throw new IllegalStateException("expected a PLUS or a MINUS token as operator, internal error?");
        }

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
        Integer variableIndex = (Integer) loop.loopVariable.value;
        int loopCount = variableValues[variableMapping.get(variableIndex)];
        for (int i = 0; i < loopCount; i++) {
            loop.body.accept(this);
        }

        return null;
    }
}
