package test;

public class Student {
    String score;
    String appNo;
    String appDataSource;
    String examNode;
    String custSex;
    String relationRet;

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getAppDataSource() {
        return appDataSource;
    }

    public void setAppDataSource(String appDataSource) {
        this.appDataSource = appDataSource;
    }

    public String getExamNode() {
        return examNode;
    }

    public void setExamNode(String examNode) {
        this.examNode = examNode;
    }

    public String getCustSex() {
        return custSex;
    }

    public void setCustSex(String custSex) {
        this.custSex = custSex;
    }

    public String getRelationRet() {
        return relationRet;
    }

    public void setRelationRet(String relationRet) {
        this.relationRet = relationRet;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo;
    }

    String custName;
    String certInfo;
    public static boolean isNUllOrEmpty(String value){
        if (value == null || "".equals(value)){
            return  true;
        }else {
            return false;
        }
    }
    public Student(){
        this.score = "zhangsan";
    }

    public String getScore() {
        return score;
    }

    public void setScore(String name) {
        this.score = name;
    }

    public boolean check(int num){
        if (num == 1){
            return true;
        }
        return false;
    }
}
