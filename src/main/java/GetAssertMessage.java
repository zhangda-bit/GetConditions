import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.sun.xml.internal.ws.encoding.MtomCodec;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAssertMessage extends VoidVisitorAdapter<Void> {
    private HashMap<String, String> assertMessage;
    private CompilationUnit cu;

    public GetAssertMessage() {
        this.assertMessage = new HashMap<>();
    }

    public GetAssertMessage(CompilationUnit cu) {
        this.cu = cu;
    }

    public GetAssertMessage(HashMap<String, String> assertMessage) {
        this.assertMessage = assertMessage;
    }

    public GetAssertMessage(HashMap<String, String> assertMessage, CompilationUnit cu) {
        this.cu = cu;
        this.assertMessage = assertMessage;
    }

    public HashMap<String, String> getAssertMessage() {
        return assertMessage;
    }

    @Override
    public void visit(IfStmt ifStmt, Void arg) {
        super.visit(ifStmt, arg);
        process(ifStmt);
    }

    /*
     * 假设待提取格式均为if(GBeanUtil.isNullEmpty(appInfo.getAppNo())){errmsg.add("申请编号不能为空！")}
     * */
    public void process(IfStmt ifStmt) {
        Expression condition = ifStmt.getCondition();
        if (condition instanceof MethodCallExpr) {
            processMethodCall(condition, ifStmt);
        }else if(condition instanceof  BinaryExpr){
            Expression left = ((BinaryExpr) condition).getLeft();
            processMethodCall(left, ifStmt);
        }
    }

    public void processMethodCall(Expression condition, IfStmt ifStmt) {
        String paramName = null, message = null;
        // 获取 if 语句中的大括号块
        BlockStmt block = ifStmt.getThenStmt().asBlockStmt();
        NodeList<Statement> statements = block.getStatements();
        if (statements.size() == 0){
            return;
        }
        Statement statement = statements.get(0);
        String str = statement.toString();
        if (str.contains("errmsgs")) {
            message = extractSubstrings(str);
        } else return;
        // 抽取参数名
        MethodCallExpr methodCallExpr = (MethodCallExpr) condition;
        Expression argumentExpr = methodCallExpr.getArguments().get(0);
        if (argumentExpr instanceof MethodCallExpr) {
            MethodCallExpr subMethodCallExpr = (MethodCallExpr) argumentExpr;
            paramName = subMethodCallExpr.getName().asString().substring(3);
            paramName = paramName.substring(0, 1).toLowerCase(Locale.ROOT) + paramName.substring(1);
        }
        if (!assertMessage.containsKey(paramName) && !"".equals(message)) {
            assertMessage.put(paramName, message);
        }

    }

    public String extractSubstrings(String str) {
        // 使用正则表达式匹配在引号内的子串
        String pattern = "\"([^\"]*)\"";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(str);

        // 提取子串
        String res = "";
        if (matcher.find()) {
            res = matcher.group(1);
        }
        return res;
    }
}
