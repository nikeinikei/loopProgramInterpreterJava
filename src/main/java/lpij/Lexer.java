package lpij;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class Lexer implements Iterator<Token> {
    private static final Map<String, TokenType> identifiers = new HashMap<>();

    static {
        identifiers.put("LOOP", TokenType.LOOP);
        identifiers.put("DO", TokenType.DO);
        identifiers.put("END", TokenType.END);
    }

    private final String input;
    private int index;
    private final int length;

    public Lexer(String input) {
        this.input = input
                .trim()
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ");
        this.index = 0;
        this.length = this.input.length();
    }

    @Override
    public boolean hasNext() {
        return !isAtEnd();
    }

    @Override
    public Token next() {
        if (isAtEnd()) {
            throw new NoSuchElementException("no more tokens to lex");
        }

        skipWhitespaces();

        if (match('+')) {
            return new Token(TokenType.PLUS, "+", null, index - 1);
        }
        if (match('-')) {
            return new Token(TokenType.MINUS, "-", null, index - 1);
        }
        if (match('=')) {
            return new Token(TokenType.ASSIGNMENT, "=", null, index - 1);
        }
        if (match(';')) {
            return new Token(TokenType.SEMICOLON, ";", null, index - 1);
        }
        if (match('x')) {
            return variableToken();
        }
        if (isNumeric(peek())) {
            return numberToken();
        }
        if (isAlpha(peek())) {
            return identifierToken();
        }

        throw new ParseException(index, this.input, "error while parsing");
    }

    private Token identifierToken() {
        int i = index;
        var sb = new StringBuilder();
        char c;
        while (!isAtEnd() && isAlpha(c = peek())) {
            sb.append(c);
            advance();
        }
        String identifierStr = sb.toString();
        TokenType type = identifiers.get(identifierStr);
        if (type == null) {
            throw new ParseException(index, this.input, "unknown identifier '" + identifierStr + "'");
        } else {
            return new Token(type, identifierStr, null, i);
        }
    }

    private String number() {
        var sb = new StringBuilder();
        char c;
        while (!isAtEnd() && isNumeric(c = peek())) {
            sb.append(c);
            advance();
        }
        return sb.toString();
    }

    private Token numberToken() {
        int i = index;
        String numStr = number();
        return new Token(TokenType.NUMBER, numStr, Integer.parseInt(numStr), i);
    }

    private Token variableToken() {
        int i = index;
        //already consumed the x
        String numStr = number();

        return new Token(
                TokenType.VARIABLE,
                "x" + numStr,
                Integer.parseInt(numStr), i);  //we store the integer value here, since we'll use this later to index the array during interpreting
    }

    private boolean isAtEnd() {
        return this.index >= this.length;
    }

    private boolean match(char c) {
        if (peek() == c) {
            advance();
            return true;
        }

        return false;
    }

    private char peek() {
        if (isAtEnd()) {
            throw new IllegalStateException("cannot peek when at the end of the input");
        }

        return this.input.charAt(this.index);
    }

    private void advance() {
        if (!isAtEnd()) {
            index++;
        }
    }

    private void skipWhitespaces() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }

    private boolean isAlpha(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }

    private boolean isNumeric(char c) {
        return '0' <= c && c <= '9';
    }
}