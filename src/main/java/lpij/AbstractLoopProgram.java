package lpij;

abstract class AbstractLoopProgram {
    public interface Visitor<T> {
        T visitAssignment(Assignment assignment);

        T visitGrouping(Grouping grouping);

        T visitLoop(Loop loop);
    }

    public static class Assignment extends AbstractLoopProgram {
        final Token leftVariable;
        final Token rightVariable;
        final Token operator;
        final Token number;

        public Assignment(Token leftVariable, Token rightVariable, Token operator, Token number) {
            this.leftVariable = leftVariable;
            this.rightVariable = rightVariable;
            this.operator = operator;
            this.number = number;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignment(this);
        }

        @Override
        public String toString() {
            return "Assignment{" +
                    "leftVariable=" + leftVariable +
                    ", rightVariable=" + rightVariable +
                    ", operator=" + operator +
                    ", number=" + number +
                    '}';
        }
    }

    public static class Grouping extends AbstractLoopProgram {
        final AbstractLoopProgram left;
        final AbstractLoopProgram right;

        public Grouping(AbstractLoopProgram left, AbstractLoopProgram right) {
            this.left = left;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }

        @Override
        public String toString() {
            return "Grouping{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    public static class Loop extends AbstractLoopProgram {
        final Token loopVariable;
        final AbstractLoopProgram body;

        public Loop(Token loopVariable, AbstractLoopProgram body) {
            this.loopVariable = loopVariable;
            this.body = body;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLoop(this);
        }

        @Override
        public String toString() {
            return "Loop{" +
                    "loopVariable=" + loopVariable +
                    ", body=" + body +
                    '}';
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}