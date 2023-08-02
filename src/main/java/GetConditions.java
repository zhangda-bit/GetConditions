import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GetConditions {
    public static List<Parameter> getIfConditionsByParser(String filePath) {
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
            IfConditionParser ifConditionVisitor = new IfConditionParser(cu);
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

    public static List<List<Parameter>> getConditionsFromMethod(String filePath) {
        CompilationUnit cu = null;
        try {
            //使用JavaParser解析java文件
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<Parameter>> res = new ArrayList<>();
        if (cu != null) {
            // 创建方法声明收集器
            List<MethodDeclaration> methodDeclarations = new ArrayList<>();

            // 创建Visitor对象，并传入方法声明收集器
            MethodDeclarationExtractor methodDeclarationExtractor = new MethodDeclarationExtractor();
            methodDeclarationExtractor.visit(cu, methodDeclarations);

            // 遍历收集到的方法声明
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                // 创建处理器
                MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();

                // 处理MethodDeclaration
                System.out.println("Method name: " + methodDeclaration.getNameAsString());

                methodDeclarationVisitor.visit(methodDeclaration, null);
                List<Parameter> conditions = methodDeclarationVisitor.getConditions();
                if (conditions.size() > 0) {
                    res.add(conditions);
                }
                for (Parameter p : conditions) {
                    System.out.println(p.toString());
                }
            }

        }
        return res;
    }

    public static List<HashMap<String, String>> getAssertMessage(String filePath) {
        CompilationUnit cu = null;
        try {
            //使用JavaParser解析java文件
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<HashMap<String, String>> res = new ArrayList<>();
        if (cu != null) {
            // 创建方法声明收集器
            List<MethodDeclaration> methodDeclarations = new ArrayList<>();

            // 创建Visitor对象，并传入方法声明收集器
            MethodDeclarationExtractor methodDeclarationExtractor = new MethodDeclarationExtractor();
            methodDeclarationExtractor.visit(cu, methodDeclarations);

            // 遍历收集到的方法声明
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                System.out.println(methodDeclaration.getNameAsString());

                List<IfStmt> ifStmts = methodDeclaration.findAll(IfStmt.class);
                for (IfStmt ifStmt : ifStmts) {
                    GetAssertMessage getAssertMessage = new GetAssertMessage();
                    getAssertMessage.visit(ifStmt, null);
                    HashMap<String, String> assertMessage = getAssertMessage.getAssertMessage();
                    Set<String> keySet = assertMessage.keySet();
                    for (String key : keySet){
                        System.out.println(key+ ": " + assertMessage.get(key));
                    }
                    res.add(assertMessage);
                }
            }
        }
        return res;
    }

    public static List<Parameter> mergeParamList(List<Parameter> l1, List<Parameter> l2) {
        List<Parameter> l = new ArrayList<>(l2);
        for (Parameter p1 : l1) {
            boolean hasParam = false;
            for (Parameter p2 : l) {
                if (p1.getName().equals(p2.getName())) {
                    hasParam = true;
                    List<String> paramValues1 = p1.getValues();
                    List<String> paramValues2 = p2.getValues();
                    for (String value1 : paramValues1) {
                        boolean hasValue = false;
                        for (String value2 : paramValues2) {
                            if (value1.equals(value2)) {
                                hasValue = true;
                                break;
                            }
                        }
                        if (!hasValue) {
                            paramValues2.add(value1);
                        }
                    }
                }
            }
            if (!hasParam) {
                l.add(p1);
            }
        }
        return l;
    }
}
