import java.util.ArrayList;

public class testJavaParser {
    public static void main(String[] args) {
        // Java代码文件路径
        String filePath = "./toBeTest.java";
        // 解析Java代码文件
        GetConditions.getConditionsByParser(filePath);
        Parameter p = new Parameter("name", "String", new ArrayList<String>(){{this.add(""); this.add("zhangsan");}});
        //System.out.println(p.toString());
    }
}
