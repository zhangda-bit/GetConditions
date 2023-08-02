import java.util.ArrayList;
import java.util.regex.Pattern;

public class testJavaParser {
    public static void main(String[] args) {
        /*
        * 测试switch抽取
        * */

        /*
        String filePath = "./test.toBeTest.java";
        // 解析Java代码文件
        GetConditions.getSwitchConditionsByParser(filePath);
        */

        /*
        * 测试以方法为单位抽取switch+if抽取
        * */

        /*
        String filePath = "./test.toBeTest.java";
        GetConditions.getConditionsFromMethod(filePath);
        */


        /*
        * 测试if抽取（包含内部函数调用）
        * */
        /*
        String filePath = "./toBeTest.java";
        GetConditions.getIfConditionsByParser(filePath);
        */

        /*
        * 测试errmsgs信息抽取能力
        * */

        /*
        String filePath = "./test/toBeTestAssert.java";
        GetConditions.getAssertMessage(filePath);
         */


    }
}
