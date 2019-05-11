package lpij;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void simpleProgramTest() {
        var tokens = Arrays.asList(
                new Token(TokenType.VARIABLE, "x0", 0, 0),
                new Token(TokenType.ASSIGNMENT, "=", null, 1),
                new Token(TokenType.VARIABLE, "x1", 1, 2),
                new Token(TokenType.PLUS, "+", null, 3),
                new Token(TokenType.NUMBER, "2", 2, 4)
        );
        var parser = new Parser(tokens);
        var program = parser.parseLoopProgram();
        assertTrue(program instanceof AbstractLoopProgram.Assignment);
    }

    @Test
    public void loopProgramTest() {
        var tokens = Arrays.asList(
                new Token(TokenType.LOOP, "LOOP", null, 0),
                new Token(TokenType.VARIABLE, "x0", 0, 1),
                new Token(TokenType.DO, "DO", null, 2),

                new Token(TokenType.VARIABLE, "x0", 0, 3),
                new Token(TokenType.ASSIGNMENT, "=", null, 4),
                new Token(TokenType.VARIABLE, "x1", 1, 5),
                new Token(TokenType.PLUS, "+", null, 6),
                new Token(TokenType.NUMBER, "2", 2, 7),

                new Token(TokenType.END, "END", null, 8)
        );
        var parser = new Parser(tokens);
        var program = parser.parseLoopProgram();
        assertTrue(program instanceof AbstractLoopProgram.Loop);
    }

    @Test
    public void groupingTest() {
        var tokens = Arrays.asList(
                new Token(TokenType.VARIABLE, "x0", 0, 0),
                new Token(TokenType.ASSIGNMENT, "=", null, 1),
                new Token(TokenType.VARIABLE, "x1", 1, 2),
                new Token(TokenType.PLUS, "+", null, 3),
                new Token(TokenType.NUMBER, "2", 2, 4),

                new Token(TokenType.SEMICOLON, ";", null, 5),

                new Token(TokenType.VARIABLE, "x0", 0, 6),
                new Token(TokenType.ASSIGNMENT, "=", null, 7),
                new Token(TokenType.VARIABLE, "x1", 1, 8),
                new Token(TokenType.PLUS, "+", null, 9),
                new Token(TokenType.NUMBER, "2", 2, 10)
        );
        var parser = new Parser(tokens);
        var program = parser.parseLoopProgram();
        assertTrue(program instanceof AbstractLoopProgram.Grouping);
        var grouping = (AbstractLoopProgram.Grouping) program;
        var leftProgram = grouping.left;
        var rightProgram = grouping.right;
        assertTrue(leftProgram instanceof AbstractLoopProgram.Assignment);
        assertTrue(rightProgram instanceof AbstractLoopProgram.Assignment);
    }
}
