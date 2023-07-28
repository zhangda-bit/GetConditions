import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GetConditions {
    public static List<Parameter> getConditionsByParser(String filePath){
        CompilationUnit cu = null;
        try {
            //使用JavaParser解析java文件
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Parameter> res = null;
        if (cu != null) {
            // 创建自定义的访问器类
            IfConditionParser ifConditionVisitor = new IfConditionParser();
            // 遍历AST的所有IfStmt
            List<IfStmt> ifStmtList = cu.findAll(IfStmt.class);
            for (IfStmt ifStmt : ifStmtList) {
                ifConditionVisitor.visit(ifStmt, null);
                // 输出提取的参数与参数取值范围
            }
             res = ifConditionVisitor.getConditions();
            for (Parameter parm : res) {
                System.out.println(parm.toString());
            }
        }
        return res;
    }
}
