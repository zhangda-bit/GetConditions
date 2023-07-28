import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;

public class test {
    public static void main(String[] args) throws Exception {

    CompilationUnit cu = StaticJavaParser.parse("class Example { public static void main(String[] args) { if(x > 0 && x < 10) { System.out.println(\"x is between 1 and 9\"); } } }");

    // 遍历所有的if语句
    for (IfStmt ifStmt : cu.findAll(IfStmt.class)) {

      // 获取if语句的条件表达式
      Expression condition = ifStmt.getCondition();

      // 如果条件表达式是一个二元表达式
      if (condition.isBinaryExpr()) {

        BinaryExpr binaryExpr = condition.asBinaryExpr();

        // 获取左边的表达式，即参数
        Expression left = binaryExpr.getLeft();

        // 获取右边的表达式，即参数取值范围
        Expression right = binaryExpr.getRight();

        System.out.println("Parameter: " + left + ", Value range: " + right);

      }
    }
  }
}
