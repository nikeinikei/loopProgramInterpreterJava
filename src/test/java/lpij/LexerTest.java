package lpij;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lpij.TokenType.*;

public class LexerTest {
    private List<Token> getAllTokens(Lexer lexer) {
        var list = new ArrayList<Token>();
        while (lexer.hasNext()) {
            list.add(lexer.next());
        }
        return list;
    }

    private <T> void assertListEquals(List<T> list1, List<T> list2) {
        Assert.assertEquals(list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            assert list1.get(i).equals(list2.get(i));
        }
    }

    private void assertTokenTypes(List<Token> tokens, List<TokenType> types) {
        var tokenTypes = tokens.stream().map(token -> token.type).collect(Collectors.toList());
        assertListEquals(tokenTypes, types);
    }

    @Test
    public void testAssignment() {
        String input = "x0 = x1 + 345";
        var lexer = new Lexer(input);
        var tokens = getAllTokens(lexer);
        var types = Arrays.asList(VARIABLE, ASSIGNMENT, VARIABLE, PLUS, NUMBER);
        assertTokenTypes(tokens, types);
    }

    @Test
    public void testIdentifiers() {
        String input = "LOOP DO END x10";
        var lexer = new Lexer(input);
        var tokens = getAllTokens(lexer);
        var types = Arrays.asList(LOOP, DO, END, VARIABLE);
        assertTokenTypes(tokens, types);
    }

    @Test(expected = ParseException.class)
    public void expectedExceptionOnUnknownIdentifier() {
        String input = "LOOPP";
        new Lexer(input).next();
    }

    @Test
    public void shouldNotCareAboutWhitespaces() {
        String input = "x0                =                x1                      ; ";
        var lexer = new Lexer(input);
        var tokens = getAllTokens(lexer);
        var types = Arrays.asList(VARIABLE, ASSIGNMENT, VARIABLE, SEMICOLON);
        assertTokenTypes(tokens, types);
    }

    @Test
    public void dealCorrectlyWithNewlines() {
        String input = "x0\nx1\nx2";
        var lexer = new Lexer(input);
        var tokens = getAllTokens(lexer);
        var types = Arrays.asList(VARIABLE, VARIABLE, VARIABLE);
        assertTokenTypes(tokens, types);
    }
}
