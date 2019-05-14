package cn.seecoder;

import java.util.regex.Pattern;

class Lexer {
    //
    //data part
    private StringBuilder data;
    private int loc;
    private Token _token;

    //constructor func
    Lexer(String data) {
        this.data = new StringBuilder(data);
        this.loc = 0;
        this._getToken();
    }

    //
    //specific methods to get Tokens
    private char _getChar() {
        if (this.loc >= this.data.length()) {
            loc++;
            return '\0';
        }
        return this.data.charAt(loc++);
    }

    private void _getToken() {
        char tempChar;
        do {
            tempChar = this._getChar();
        } while (Pattern.matches("\\s", String.valueOf(tempChar)));
        switch (tempChar) {
            case '\\':
                this._token = new Token(Token.Type.LAMBDA, new StringBuilder("\\"));
                break;
            case '.':
                this._token = new Token(Token.Type.DOT, new StringBuilder("."));
                break;
            case '(':
                this._token = new Token(Token.Type.LPAREN, new StringBuilder("("));
                break;
            case ')':
                this._token = new Token(Token.Type.RPAREN, new StringBuilder(")"));
                break;
            case '\0':
                this._token = new Token(Token.Type.EOF, new StringBuilder("\0"));
                break;
            default:
                if (Pattern.matches("[a-z]", String.valueOf(tempChar))) {
                    StringBuilder tempStr = new StringBuilder();
                    do {
                        tempStr.append(tempChar);
                        tempChar = this._getChar();
                    } while (Pattern.matches("[a-zA-Z]", String.valueOf(tempChar)));
                    this.loc--;
                    this._token = new Token(Token.Type.LCID, tempStr);
                }
        }
    }

    //
    //check whether given type is equal to now's
    boolean check(Token.Type type) {
        return this._token.getType() == type;
    }

    //
    //check & skip(get next Token)
    boolean skip(Token.Type type) {
        if (this.check(type)) {
            this._getToken();
            return true;
        }
        return false;
    }

    //
    //if next token has the given type & skip
    void match(Token.Type type) {
        if (this.check(type)) this._getToken();
    }

    //
    //if next token
    StringBuilder token(Token.Type type) {
        //if(type==null)return this._token.getValue();
        Token tempToken = this._token;
        this.match(type);
        return tempToken.getValue();
    }
}
