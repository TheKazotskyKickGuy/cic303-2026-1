// IfNode.java
package br.maua.cic303.ast;
import br.maua.cic303.Environment;

public class IfNode extends ASTNode {
    public final ASTNode condicao, blocoVerdadeiro, blocoFalso;
    public IfNode(ASTNode c, ASTNode b1, ASTNode b2) { condicao = c; blocoVerdadeiro = b1; blocoFalso = b2; }
    
}

