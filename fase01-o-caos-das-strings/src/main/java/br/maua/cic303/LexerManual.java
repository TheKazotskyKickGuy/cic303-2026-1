package br.maua.cic303;

public class LexerManual {
    private String entrada;
    private int posicao;

    public LexerManual(String entrada) {
        this.entrada = entrada;
        this.posicao = 0;
    }

    public Token nextToken() {

    while (posicao < entrada.length() && Character.isWhitespace(entrada.charAt(posicao))) {
        posicao++;
    }

    if (posicao >= entrada.length()) {
        return new Token(Tag.EOF, "");
    }

    char atual = entrada.charAt(posicao);

    if (atual == '=') {
        posicao++;
        return new Token(Tag.ASSIGN, "=");
    }
    if (atual == '+' || atual == '-') {
        posicao++;
        return new Token(Tag.ADD_OP, String.valueOf(atual));
    }
    if (atual == '*' || atual == '/') {
        posicao++;
        return new Token(Tag.MUL_OP, String.valueOf(atual));
    }

    if (Character.isDigit(atual)) {
        StringBuilder sb = new StringBuilder();
        while (posicao < entrada.length() && (Character.isDigit(entrada.charAt(posicao)) || entrada.charAt(posicao) == '.')) {
            sb.append(entrada.charAt(posicao));
            posicao++;
        }
        return new Token(Tag.NUMBER, sb.toString());
    }

    if (Character.isLetter(atual)) {
        StringBuilder sb = new StringBuilder();
        while (posicao < entrada.length() && (Character.isLetterOrDigit(entrada.charAt(posicao)) || entrada.charAt(posicao) == '_')) {
            sb.append(entrada.charAt(posicao));
            posicao++;
        }
        return new Token(Tag.ID, sb.toString());
    }
        if (posicao >= entrada.length()) {
            return new Token(Tag.EOF, "");
        }
        
        String lexemaNaoReconhecido = String.valueOf(entrada.charAt(posicao));
        posicao++;

        return new Token(Tag.ERROR, lexemaNaoReconhecido);
    }
}