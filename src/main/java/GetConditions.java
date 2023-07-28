import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetConditions {
    public static List<Parameter> getConditionsByParser(String filePath) {
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

    public static List<Parameter> getSwitchConditionsByParser(String filePath) {
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
            SwitchConditionParser switchConditionParser = new SwitchConditionParser();
            // 遍历AST的所有IfStmt
            List<SwitchStmt> switchStmtList = cu.findAll(SwitchStmt.class);
            for (SwitchStmt switchStmt : switchStmtList) {
                switchConditionParser.visit(switchStmt, null);
                // 输出提取的参数与参数取值范围
            }
            res = switchConditionParser.getConditions();
            for (Parameter parm : res) {
                System.out.println(parm.toString());
            }
        }
        return res;
    }
    public static List<Parameter> mergeParamList(List<Parameter> l1, List<Parameter> l2){
        List<Parameter> l = new ArrayList<>(l2);
        for (Parameter p1 : l1){
            boolean hasParam = false;
            for (Parameter p2 : l){
                if (p1.getName().equals(p2.getName())){
                    hasParam = true;
                    List<String> paramValues1 = p1.getValues();
                    List<String> paramValues2 = p2.getValues();
                    for(String value1 : paramValues1){
                        boolean hasValue = false;
                        for (String value2 : paramValues2){
                            if(value1.equals(value2)){
                                hasValue = true;
                                break;
                            }
                        }
                        if (!hasValue){
                            paramValues2.add(value1);
                        }
                    }
                }
            }
            l.add(p1);
        }
        return l;
    }
}
