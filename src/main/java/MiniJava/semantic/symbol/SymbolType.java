package MiniJava.semantic.symbol;

/**
 * Created by mohammad hosein on 6/28/2015.
 */

public enum SymbolType {
    Int(MiniJava.codeGenerator.varType.Int),
    Bool(MiniJava.codeGenerator.varType.Bool),
    ;
    public final MiniJava.codeGenerator.varType varType;
    SymbolType(MiniJava.codeGenerator.varType varType) {
        this.varType = varType;
    }
}
