import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.CompilationUnit;

public class test {
    public static void main(String[] args) {
        // 创建一个样例代码来演示
        String code = "public class MyClass {\n" +
                "    public void myMethod() {\n" +
                "        if (check()) {\n" +
                "            System.out.println(\"Condition is true\");\n" +
                "        }\n" +
                "    }\n" +
                "    public boolean check() {\n" +
                "        return true;\n" +
                "    }\n" +
                "}";

        // 使用JavaParser解析代码
        CompilationUnit cu = StaticJavaParser.parse(code);

        // 创建Visitor对象来访问抽象语法树
        VoidVisitorAdapter<Void> visitorAdapter = new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(IfStmt ifStmt, Void arg) {
                super.visit(ifStmt, arg);

                // 检查条件判断语句中的表达式是否为方法调用表达式
                if (ifStmt.getCondition() instanceof MethodCallExpr) {
                    MethodCallExpr methodCallExpr = (MethodCallExpr) ifStmt.getCondition();

                    // 检查方法调用的名称是否为 "check"
                    if (methodCallExpr.getNameAsString().equals("check")) {
                        // 获取方法调用的父节点，即条件语句的父节点
                        Node parentNode = methodCallExpr.getParentNode().orElse(null);

                        // 确保父节点是MethodDeclaration
                        if (parentNode instanceof MethodDeclaration) {
                            MethodDeclaration methodDeclaration = (MethodDeclaration) parentNode;

                            // 输出方法声明对象
                            System.out.println("方法名: " + methodDeclaration.getNameAsString());
                            System.out.println("方法参数: " + methodDeclaration.getParameter(0));
                        }
                    }
                }
            }
        };
    }
}