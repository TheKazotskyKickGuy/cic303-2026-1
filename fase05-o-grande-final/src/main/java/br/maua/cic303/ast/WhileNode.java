// WhileNode.java
package br.maua.cic303.ast;

import br.maua.cic303.Environment;

public class WhileNode extends ASTNode {
    public final ASTNode condicao, bloco;

    public WhileNode(ASTNode c, ASTNode b) {
        condicao = c;
        bloco = b;
    }

}