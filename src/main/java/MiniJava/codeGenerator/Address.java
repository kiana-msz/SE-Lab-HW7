package MiniJava.codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */

public class Address {
    private final int num;
    private final TypeAddress type;
    private final varType varType;

    public Address(int num, varType varType, TypeAddress Type) {
        this.num = num;
        this.type = Type;
        this.varType = varType;
    }

    public Address(int num, varType varType) {
        this.num = num;
        this.type = TypeAddress.Direct;
        this.varType = varType;
    }


    public int getNum() {
        return num;
    }

    public TypeAddress getType() {
        return type;
    }

    public MiniJava.codeGenerator.varType getVarType() {
        return varType;
    }


    public String toString() {
        switch (getType()) {
            case Direct:
                return getNum() + "";
            case Indirect:
                return "@" + getNum();
            case Imidiate:
                return "#" + getNum();
        }
        return getNum() + "";
    }
}