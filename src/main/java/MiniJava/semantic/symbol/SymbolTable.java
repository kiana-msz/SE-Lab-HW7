package MiniJava.semantic.symbol;

import MiniJava.codeGenerator.Address;
import MiniJava.codeGenerator.Memory;
import MiniJava.codeGenerator.TypeAddress;
import MiniJava.codeGenerator.varType;
import MiniJava.errorHandler.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Klass> klasses;
    private final Map<String, Address> keyWords;
    private final Memory mem;
    private SymbolType lastType;

    public SymbolTable(Memory memory) {
        mem = memory;
        klasses = new HashMap<>();
        keyWords = new HashMap<>();
        keyWords.put("true", new Address(1, varType.Bool, TypeAddress.Imidiate));
        keyWords.put("false", new Address(0, varType.Bool, TypeAddress.Imidiate));
    }

    public void setLastType(SymbolType type) {
        lastType = type;
    }

    public void addClass(String className) {
        if (klasses.containsKey(className)) {
            ErrorHandler.printError("This class already defined");
        }
        klasses.put(className, new Klass());
    }

    public void addField(String fieldName, String className) {
        klasses.get(className).fields.put(fieldName, new Symbol(lastType, mem.getDateAddress()));
    }

    public void addMethod(MethodParameters methodParameters, int address) {
        if (klasses.get(methodParameters.getClassName()).methods.containsKey(methodParameters.getMethodName())) {
            ErrorHandler.printError("This method already defined");
        }
        klasses.get(methodParameters.getClassName())
                .methods.put(methodParameters.getMethodName(), new Method(address, lastType));
    }

    public void addMethodParameter(MethodParameters methodParameters, String parameterName) {
        klasses.get(methodParameters.getClassName())
                .methods.get(methodParameters.getMethodName()).addParameter(parameterName);
    }

    public void addMethodLocalVariable(MethodParameters methodParameters, String localVariableName) {
        if (klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).localVariable.containsKey(localVariableName)) {
            ErrorHandler.printError("This variable already defined");
        }
        klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).localVariable.put(localVariableName, new Symbol(lastType, mem.getDateAddress()));
    }

    public void setSuperClass(String superClass, String className) {
        klasses.get(className).superClass = klasses.get(superClass);
    }

    public Address get(String keywordName) {
        return keyWords.get(keywordName);
    }

    public Symbol get(String fieldName, String className) {
        return klasses.get(className).getField(fieldName);
    }

    public Symbol get(MethodParameters methodParameters, String variable) {
        Symbol res = klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).getVariable(variable);
        if (res == null) res = get(variable, methodParameters.getClassName());
        return res;
    }

    public Symbol getNextParam(MethodParameters methodParameters) {
        return klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).getNextParameter();
    }

    public void startCall(MethodParameters methodParameters) {
        klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).reset();
    }

    public int getMethodCallerAddress(MethodParameters methodParameters) {
        return klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).callerAddress;
    }

    public int getMethodReturnAddress(MethodParameters methodParameters) {
        return klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).returnAddress;
    }

    public SymbolType getMethodReturnType(MethodParameters methodParameters) {
        return klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).returnType;
    }

    public int getMethodAddress(MethodParameters methodParameters) {
        return klasses.get(methodParameters.getClassName()).methods.get(methodParameters.getMethodName()).codeAddress;
    }


    class Klass {
        public Map<String, Symbol> fields;
        public Map<String, Method> methods;
        public Klass superClass;

        public Klass() {
            fields = new HashMap<>();
            methods = new HashMap<>();
        }

        public Symbol getField(String fieldName) {
            if (fields.containsKey(fieldName)) {
                return fields.get(fieldName);
            }
            return superClass.getField(fieldName);
        }
    }

    class Method {
        private final ArrayList<String> orderedParameters;
        public int codeAddress;
        public Map<String, Symbol> parameters;
        public Map<String, Symbol> localVariable;
        public int callerAddress;
        public int returnAddress;
        public SymbolType returnType;
        private int index;

        public Method(int codeAddress, SymbolType returnType) {
            this.codeAddress = codeAddress;
            this.returnType = returnType;
            this.orderedParameters = new ArrayList<>();
            this.returnAddress = mem.getDateAddress();
            this.callerAddress = mem.getDateAddress();
            this.parameters = new HashMap<>();
            this.localVariable = new HashMap<>();
        }

        public Symbol getVariable(String variableName) {
            if (parameters.containsKey(variableName)) return parameters.get(variableName);
            if (localVariable.containsKey(variableName)) return localVariable.get(variableName);
            return null;
        }

        public void addParameter(String parameterName) {
            parameters.put(parameterName, new Symbol(lastType, mem.getDateAddress()));
            orderedParameters.add(parameterName);
        }

        private void reset() {
            index = 0;
        }

        private Symbol getNextParameter() {
            return parameters.get(orderedParameters.get(index++));
        }
    }
}