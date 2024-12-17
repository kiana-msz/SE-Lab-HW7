package MiniJava.semantic.symbol;

/**
 * Created by mohammad hosein on 6/28/2015.
 */

public enum SymbolType {
    Int(codeGenerator.varType.Int),
    Bool(codeGenerator.varType.Bool),
    ;
    public final codeGenerator.varType varType;
    SymbolType(codeGenerator.varType varType) {
        this.varType = varType;
    }
}
