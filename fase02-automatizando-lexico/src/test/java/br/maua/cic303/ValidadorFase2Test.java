package br.maua.cic303;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
//import br.maua.cic303.Tag; 

public class ValidadorFase2Test {

    private List<Token> analisar(String codigo) throws Exception {
        Lexer lexer = new Lexer(new StringReader(codigo));
        List<Token> tokens = new ArrayList<>();
        Token t;
        while ((t = lexer.yylex()) != null && t.tag != Tag.EOF) {
            tokens.add(t);
        }
        return tokens;
    }

    @Test
    public void test01_PalavrasReservadasEPontuacao() throws Exception {
        List<Token> tokens = analisar("if ( ) then { } else while ;");
        
        Tag[] esperados = {
            Tag.IF, Tag.LPAREN, Tag.RPAREN, Tag.THEN, Tag.LBRACE, 
            Tag.RBRACE, Tag.ELSE, Tag.WHILE, Tag.SEMI
        };

        assertEquals("Faltou reconhecer alguma palavra reservada ou pontuação.", esperados.length, tokens.size());
        for (int i = 0; i < esperados.length; i++) {
            assertEquals("Erro no token da posição " + i, esperados[i], tokens.get(i).tag);
        }
    }

    @Test
    public void test02_OperadoresComPrecedenciaLexica() throws Exception {
        // Se o JFlex não estiver configurado direito, "==" pode ser lido como "=" e "="
        List<Token> tokens = analisar("= == != < > <= >= + - * / %");
        
        Tag[] esperados = {
            Tag.ASSIGN, Tag.REL_OP, Tag.REL_OP, Tag.REL_OP, Tag.REL_OP, Tag.REL_OP, Tag.REL_OP,
            Tag.ADD_OP, Tag.ADD_OP, Tag.MUL_OP, Tag.MUL_OP, Tag.MUL_OP
        };

        assertEquals("Falha ao reconhecer operadores (Atenção à regra do == e =).", esperados.length, tokens.size());
        for (int i = 0; i < esperados.length; i++) {
            assertEquals("Classificação incorreta na posição " + i, esperados[i], tokens.get(i).tag);
        }
    }

    @Test
    public void test03_NotacaoCientifica() throws Exception {
        // Baseado nas especificações do PDF da linguagem JACA
        List<Token> tokens = analisar("7 3.14 6.02E23 6.02e23 6.62E-34");
        
        assertEquals("Falhou ao ler os 5 números de teste", 5, tokens.size());
        for (Token t : tokens) {
            assertEquals("Deveria ser NUMBER: " + t.lexeme, Tag.NUMBER, t.tag);
        }
        assertEquals("3.14", tokens.get(1).lexeme);
        assertEquals("6.62E-34", tokens.get(4).lexeme);
    }

    @Test
    public void test04_RegraDos32Caracteres() throws Exception {
        // ID exato com 32 caracteres (Deve ser válido)
        String id32 = "A_23456789_123456789_123456789_2"; 
        List<Token> t1 = analisar(id32);
        assertEquals(Tag.ID, t1.get(0).tag);
        assertEquals(id32, t1.get(0).lexeme);

        // ID com 33 caracteres (Deve disparar o ERROR fallback do JFlex)
        String id33 = "A_23456789_123456789_123456789_23"; 
        List<Token> t2 = analisar(id33);
        assertEquals("O Lexer deveria recusar um ID com mais de 32 caracteres.", Tag.ERROR, t2.get(0).tag);
    }

    @Test
    public void test05_IgnorarEspacosEErros() throws Exception {
        // Testa se o programa ignora espaços e captura erro no @
        List<Token> tokens = analisar("   x   @   10  ");
        assertEquals(3, tokens.size());
        assertEquals(Tag.ID, tokens.get(0).tag);
        assertEquals(Tag.ERROR, tokens.get(1).tag); // Capturou o @
        assertEquals(Tag.NUMBER, tokens.get(2).tag);
    }
}