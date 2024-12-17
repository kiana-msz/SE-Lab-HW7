package semantic.symbol;
public class MethodParameters {
    private final String className;
    private final String methodName;
    public MethodParameters(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }
    public String getClassName() {
        return className;
    }
    public String getMethodName() {
        return methodName;
    }
}