import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends VoidVisitorAdapter<Void> {

    private List<Parameter> conditions;

    public MethodDeclarationVisitor() {
        conditions = new ArrayList<>();
    }

    public MethodDeclarationVisitor(List<Parameter> conditions) {
        this.conditions = conditions;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        super.visit(methodDeclaration, arg);

        // 获取方法体内的语句列表
        NodeList<Statement> statements = methodDeclaration.getBody().get().getStatements();

        // 创建StatementVisitor对象来访问语句
        IfConditionParser ifConditionParser = new IfConditionParser(this.conditions);
        SwitchConditionParser switchConditionParser = new SwitchConditionParser(this.conditions);

        // 遍历语句列表，对每个语句进行访问
        for (Statement statement : statements) {
            if (statement instanceof IfStmt){
                ifConditionParser.visit((IfStmt) statement, null);
            }else if (statement instanceof SwitchStmt){
                switchConditionParser.visit((SwitchStmt) statement, null);
            }
        }
    }
}
