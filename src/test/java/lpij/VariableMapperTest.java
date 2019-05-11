package lpij;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class VariableMapperTest {
    @Test
    public void test1() {
        AbstractLoopProgram program = new AbstractLoopProgram.Grouping(
                new AbstractLoopProgram.Assignment(
                        new Token(TokenType.VARIABLE, "x1", 1, 0),
                        new Token(TokenType.VARIABLE, "x2", 2, 1),
                        new Token(TokenType.PLUS, "+", null, 2),
                        new Token(TokenType.NUMBER, "1", 1, 3)
                ),
                new AbstractLoopProgram.Assignment(
                        new Token(TokenType.VARIABLE, "x6", 6, 4),
                        new Token(TokenType.VARIABLE, "x7", 7, 5),
                        new Token(TokenType.PLUS, "+", null, 6),
                        new Token(TokenType.NUMBER, "1", 1, 7)
                )
        );
        var mapper = new VariableMapper(program);
        var variableMapping = mapper.mapVariables();

        var expectedMap = new HashMap<Integer, Integer>();
        expectedMap.put(0, 0);
        expectedMap.put(1, 1);
        expectedMap.put(2, 2);
        expectedMap.put(6, 3);
        expectedMap.put(7, 4);
        var expected = new VariableMapping(
                expectedMap,
                5
        );

        assertEquals(variableMapping.length, expected.length);
        assertEquals(variableMapping.map, expectedMap);
    }
}
