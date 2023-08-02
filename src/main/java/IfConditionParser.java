import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class IfConditionParser extends VoidVisitorAdapter<Void> {
    private List<Parameter> conditions;
    private CompilationUnit cu;

    public IfConditionParser() {
        conditions = new ArrayList<>();
    }

    public IfConditionParser(List<Parameter> conditions) {
        this.conditions = conditions;
    }

    public IfConditionParser(CompilationUnit cu) {
        this.conditions = new ArrayList<>();
        this.cu = cu;
    }

    public IfConditionParser(List<Parameter> conditions, CompilationUnit cu) {
        this.conditions = conditions;
        this.cu = cu;
    }

    public List<Parameter> getConditions() {
        return conditions;
    }

    @Override
    public void visit(IfStmt ifStmt, Void arg) {
        Expression condition = ifStmt.getCondition();
        process(condition);
        super.visit(ifStmt, arg);
    }

    public void process(Expression condition) {
        if (condition instanceof BinaryExpr) {
            processBinaryExpr(condition);
        }

        // 检查条件是否为方法调用表达式
        if (condition instanceof MethodCallExpr) {
            MethodCallExpr methodCallExpr = (MethodCallExpr) condition;

            // 获取方法调用的名称
            String methodName = methodCallExpr.getNameAsString();

            // 找到包含方法调用的MethodDeclaration对象
            MethodDeclaration methodDeclaration = null;
            for (MethodDeclaration eachMethodDeclaration : cu.findAll(MethodDeclaration.class)) {
                if (methodName.equals(eachMethodDeclaration.getNameAsString())) {
                    methodDeclaration = eachMethodDeclaration;
                }
            }

            // 内部函数(包含着本java文件中)
            if (methodDeclaration != null) {
                processInternalMethodExpr(methodDeclaration);
            }
            // 外部函数
            else {
                processExternalMethodExpr(condition);
            }
        }

    }


    // 处理BinaryExpr表达式
    public void processBinaryExpr(Expression condition) {
        Expression leftExpr = ((BinaryExpr) condition).getLeft();
        Expression rightExpr = ((BinaryExpr) condition).getRight();
        if ((leftExpr instanceof BinaryExpr || leftExpr instanceof MethodCallExpr) && (rightExpr instanceof BinaryExpr || rightExpr instanceof MethodCallExpr)) {
            process(leftExpr);
            process(rightExpr);
        } else {
            String paramName = null, paramType = null, paramValue = null;
            Parameter param = null;
            boolean containsParam = false;
            if (leftExpr instanceof FieldAccessExpr) {
                paramName = ((FieldAccessExpr) leftExpr).getName().asString();
            } else if (leftExpr instanceof NameExpr) {
                paramName = ((NameExpr) leftExpr).getName().asString();
            }
            for (Parameter p : conditions) {
                if (p != null && p.getName().equals(paramName)) {
                    param = p;
                    containsParam = true;
                }
            }
            paramValue = rightExpr.toString();
            if (!containsParam) {
                param = new Parameter();
                List<String> list = param.getValues();
                if (rightExpr instanceof IntegerLiteralExpr) {
                    paramType = "int";
                    list.add(String.valueOf(Integer.MIN_VALUE));
                    list.add(String.valueOf(Integer.MAX_VALUE));
                } else if (rightExpr instanceof CharLiteralExpr) {
                    paramType = "char";
                    list.add("X");
                } else if (rightExpr instanceof LongLiteralExpr) {
                    paramType = "long";
                    list.add(String.valueOf(Long.MAX_VALUE));
                    list.add(String.valueOf(Long.MIN_VALUE));
                } else if (rightExpr instanceof DoubleLiteralExpr) {
                    paramType = "double";
                    list.add(String.valueOf(Double.MAX_VALUE));
                    list.add(String.valueOf(Double.MIN_VALUE));
                } else if (rightExpr instanceof BooleanLiteralExpr) {
                    paramType = "boolean";
                    if ("false".equals(paramValue)) {
                        list.add("true");
                    } else {
                        list.add("false");
                    }
                } else if (rightExpr instanceof NullLiteralExpr) {
                    paramType = "String";
                    list.add("");
                    list.add("null");
                }
                if (paramName != null && paramType != null && paramValue != null) {
                    param.setName(paramName);
                    param.setType(paramType);
                    list.add(paramValue);
                    conditions.add(param);
                }
            } else {
                List<String> list = param.getValues();
                if (list != null) {
                    for (String value : list) {
                        if (paramValue.equals(value)) {
                            return;
                        }
                    }
                }
                param.getValues().add(paramValue);
            }
        }
    }


    // 处理内部函数判断
    public void processInternalMethodExpr(MethodDeclaration methodDeclaration) {
        // 获取方法体内的语句列表
        NodeList<Statement> statements = methodDeclaration.getBody().get().getStatements();

        // 创建StatementVisitor对象来访问语句
        IfConditionParser ifConditionParser = new IfConditionParser(this.conditions, cu);

        // 遍历语句列表，对每个语句进行访问
        for (Statement statement : statements) {
            if (statement instanceof IfStmt) {
                ifConditionParser.visit((IfStmt) statement, null);
            }
        }
    }

    // 处理外部函数判断
    public void processExternalMethodExpr(Expression condition) {
        if (condition instanceof MethodCallExpr) {
            String paramName = null, paramType = null, paramValue = null;
            Parameter param = null;
            boolean containsParam = false;
            String scope = ((MethodCallExpr) condition).getScope().toString();
            scope = scope.replaceAll("Optional\\[", "").replaceAll("\\]", "");
            if (scope.matches("\".*\"")) {
                paramValue = scope.replaceAll("\"", "");
            }
            MethodCallExpr methodCallExpr = (MethodCallExpr) condition;

            // 如果是null / empty 判断，默认为string类型，并赋初值
            if (methodCallExpr.getName().asString().contains("Null") || methodCallExpr.getName().asString().contains("Empty")){
                paramValue = "";
            }
            Expression argumentExpr = methodCallExpr.getArgument(0);

            // 获得参数名称
            if (argumentExpr.isNameExpr()) {
                paramName = ((NameExpr) argumentExpr).getNameAsString();
            }
            // 处理函数中嵌套函数调用的情况
            else if (argumentExpr.isMethodCallExpr()) {
                MethodCallExpr subMethodCallExpr = (MethodCallExpr) argumentExpr;
                paramName = subMethodCallExpr.getName().asString().substring(3);
                paramName = paramName.substring(0, 1).toLowerCase(Locale.ROOT) + paramName.substring(1);
            }
            for (Parameter p : conditions) {
                if (p != null && p.getName().equals(paramName)) {
                    param = p;
                    containsParam = true;
                }
            }
            paramType = "String";
            if (!containsParam) {
                param = new Parameter();
                if (paramName != null && paramType != null && paramValue != null) {
                    param.setName(paramName);
                    param.setType(paramType);
                    param.getValues().add(paramValue);
                    if (param.getValues().size() < 2) {
                        param.getValues().add("string");
                        if (!"".equals(paramValue)) {
                            param.getValues().add("");
                        }
                    }
                    conditions.add(param);
                }
            } else {
                List<String> list = param.getValues();
                if (list != null) {
                    for (String value : list) {
                        if (paramValue.equals(value)) {
                            return;
                        }
                    }
                }
                list.add(paramValue);
            }
        }
    }
}
