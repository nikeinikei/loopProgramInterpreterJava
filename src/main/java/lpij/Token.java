package lpij;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object value;
    final int index;

    public Token(TokenType type, String lexeme, Object value, int index) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", value=" + value +
                ", index=" + index +
                '}';
    }
}