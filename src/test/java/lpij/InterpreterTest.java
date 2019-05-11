package lpij;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterpreterTest {
    @Test
    public void simpleInterpreterTest(){
        var program = new AbstractLoopProgram.Assignment(
                new Token(TokenType.VARIABLE, "x0", 0, 0),
                new Token(TokenType.VARIABLE, "x0", 0, 1),
                new Token(TokenType.PLUS, "+", null, 2),
                new Token(TokenType.NUMBER, "1", 3, 3)
        );
        var mapping = new VariableMapper(program).mapVariables();
        var interpreter = new Interpreter(program, mapping);
        int result = interpreter.execute(new int[]{});
        assertEquals(result, 3);
    }
}
