package cn.seecoder;

//Define class Token for lexer to analyse
class Token {
    private Type type;
    private StringBuilder value;//record value of specific token

    public enum Type {
        LPAREN, RPAREN, LAMBDA, DOT, LCID, EOF
    }//help recognize types

    Token(Type type, StringBuilder value) {
        this.type = type;
        this.value = value;
    }

    Type getType() {
        return type;
    }

    StringBuilder getValue() {
        return value;
    }
}
