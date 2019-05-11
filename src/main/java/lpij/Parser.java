package lpij;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int index;
    private int length;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.length = tokens.size();
    }

    public AbstractLoopProgram parseLoopProgram() {
        Token token = peek();
        if (token.type == TokenType.VARIABLE) {
            AbstractLoopProgram assignment = assignment();
            if (!isAtEnd() && peek().type == TokenType.SEMICOLON) {
                consume(TokenType.SEMICOLON);
                AbstractLoopProgram right = parseLoopProgram();
                return new AbstractLoopProgram.Grouping(assignment, right);
            } else {
                return assignment;
            }
        } else if (token.type == TokenType.LOOP) {
            AbstractLoopProgram loop = loop();
            if (!isAtEnd() && peek().type == TokenType.SEMICOLON) {
                consume(TokenType.SEMICOLON);
                AbstractLoopProgram right = parseLoopProgram();
                return new AbstractLoopProgram.Grouping(loop, right);
            } else {
                return loop;
            }
        }
        throw new ParseException(token.index, null, "couldn't parseLoopProgram");
    }

    private AbstractLoopProgram.Loop loop() {
        consume(TokenType.LOOP);
        Token loopVariable = consume(TokenType.VARIABLE);
        consume(TokenType.DO);
        AbstractLoopProgram body = parseLoopProgram();
        consume(TokenType.END);
        return new AbstractLoopProgram.Loop(loopVariable, body);
    }

    private AbstractLoopProgram.Assignment assignment() {
        Token leftVariable = consume(TokenType.VARIABLE);
        consume(TokenType.ASSIGNMENT);
        Token rightVariable = consume(TokenType.VARIABLE);
        Token operator = peek();
        if (operator.type != TokenType.PLUS && operator.type != TokenType.MINUS) {
            throw new ParseException(operator.index, null, "expected operator, got " + operator + " instead.");
        }
        advance();
        Token number = consume(TokenType.NUMBER);
        return new AbstractLoopProgram.Assignment(leftVariable, rightVariable, operator, number);
    }

    private boolean isAtEnd() {
        return this.index >= length;
    }

    private void advance() {
        if (isAtEnd()) {
            throw new IllegalStateException("cannot advance if already at end");
        }
        this.index++;
    }

    private Token consume(TokenType type) {
        Token token = peek();
        if (token.type != type) {
            throw new ParseException(token.index, null, "Expected type " + type.name() + ", but got " + token.type.name() + ".");
        }
        advance();
        return token;
    }

    private Token peek() {
        if (isAtEnd()) {
            throw new IllegalStateException("cannot peek when at end");
        }

        return this.tokens.get(this.index);
    }
}