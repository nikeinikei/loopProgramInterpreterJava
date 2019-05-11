package lpij;

public class Interpreter implements AbstractLoopProgram.Visitor<Void> {
    private int[] variableValues;
    private VariableMapping variableMapping;
    private AbstractLoopProgram program;

    public Interpreter(AbstractLoopProgram program, VariableMapping mapping) {
        this.program = program;
        this.variableMapping = mapping;
        this.variableValues = new int[variableMapping.length];
    }

    public int execute(int[] args) {
        for (int i = 0; i < variableValues.length; i++) {
            variableValues[i] = 0;
        }
        for (int i = 0; i < args.length; i++) {
            int index = i + 1;
            var mappedIndex = variableMapping.map.get(index);
            if (mappedIndex != null && mappedIndex < variableValues.length) {
                variableValues[mappedIndex] = args[i];
            }
        }

        program.accept(this);
        return variableValues[0];
    }

    @Override
    public Void visitAssignment(AbstractLoopProgram.Assignment assignment) {
        Integer index1 = (Integer) assignment.leftVariable.value;
        Integer index2 = (Integer) assignment.rightVariable.value;
        Integer number = (Integer) assignment.number.value;
        if (assignment.operator.type == TokenType.PLUS) {
            variableValues[variableMapping.map.get(index1)] = variableValues[variableMapping.map.get(index2)] + number;
        } else if (assignment.operator.type == TokenType.MINUS) {
            variableValues[variableMapping.map.get(index1)] = Math.max(variableValues[variableMapping.map.get(index2)] - number, 0);
        } else {
            throw new IllegalStateException("this shouldn't happen");
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
        int loopCount = variableValues[variableMapping.map.get(variableIndex)];
        for (int i = 0; i < loopCount; i++) {
            loop.body.accept(this);
        }

        return null;
    }
}
