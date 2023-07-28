import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;


public class IfConditionParser extends VoidVisitorAdapter<Void> {
    private List<Parameter> conditions;

    public IfConditionParser() {
        conditions = new ArrayList<>();
    }
    public IfConditionParser(List<Parameter> conditions) {
        this.conditions = conditions;
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
        if (condition instanceof MethodCallExpr) {
            String paramName = null, paramType = null, paramValue = null;
            Parameter param = null;
            boolean containsParam = false;
            String scope = ((MethodCallExpr) condition).getScope().toString();
            scope = scope.replaceAll("Optional\\[\"", "").replaceAll("\"\\]","");
            MethodCallExpr methodCallExpr = (MethodCallExpr) condition;
            Expression argumentExpr = methodCallExpr.getArgument(0);
            if (argumentExpr.isNameExpr()) {
                paramName = ((NameExpr) argumentExpr).getNameAsString();
                for (Parameter p : conditions) {
                    if (p != null && p.getName().equals(paramName)) {
                        param = p;
                        containsParam = true;
                    }
                }
                paramType = "String";
                paramValue = scope;
                if (!containsParam) {
                    param = new Parameter();
                    if (paramName != null && paramType != null && paramValue != null) {
                        param.setName(paramName);
                        param.setType(paramType);
                        param.getValues().add(paramValue);
                        if (param.getValues().size() < 2) {
                            param.getValues().add("paramValue");
                            if (!"".equals(paramValue)){
                                param.getValues().add("");
                            }
                        }
                        conditions.add(param);
                    }
                }else {
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
}
