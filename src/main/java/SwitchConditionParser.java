import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwitchConditionParser extends VoidVisitorAdapter<Void> {
    private List<Parameter> conditions;

    public SwitchConditionParser() {
        conditions = new ArrayList<>();
    }

    public SwitchConditionParser(List<Parameter> conditions) {
        this.conditions = conditions;
    }


    public List<Parameter> getConditions() {
        return conditions;
    }

    @Override
    public void visit(SwitchStmt switchStmt, Void arg) {
        super.visit(switchStmt, arg);
        process(switchStmt);
    }

    public void process(SwitchStmt switchStmt) {
        Parameter param = null;
        boolean containsParam = false;
        Expression switchExp = switchStmt.getSelector();
        String paramName = null, paramType = null;
        if (switchExp.isNameExpr()) {
            paramName = ((NameExpr) switchExp).getNameAsString();
            for (Parameter p : conditions) {
                if (p != null && p.getName().equals(paramName)) {
                    param = p;
                    containsParam = true;
                }
            }
        } else {
            return;
        }
        if (!containsParam) {
            param = new Parameter();
            // 获取switch语句中的case语句
            NodeList<SwitchEntry> entries = switchStmt.getEntries();
            for (SwitchEntry entry : entries) {
                NodeList<Expression> labels = entry.getLabels();
                for (Expression label : labels) {
                    if (paramType == null) {
                        if (label.isIntegerLiteralExpr()) {
                            paramType = "int";
                        } else if (label.isStringLiteralExpr()) {
                            paramType = "String";
                        } else if (label.isCharLiteralExpr()) {
                            paramType = "char";
                        }
                    }
                    String paramValue = label.toString();
                    param.getValues().add(paramValue);
                }
            }
            param.setName(paramName);
            param.setType(paramType);
            conditions.add(param);
        } else {
            NodeList<SwitchEntry> entries = switchStmt.getEntries();
            for (SwitchEntry entry : entries) {
                NodeList<Expression> labels = entry.getLabels();
                for (Expression label : labels) {
                    String paramValue = label.toString();
                    param.getValues().add(paramValue);
                }
            }
        }
    }
}
