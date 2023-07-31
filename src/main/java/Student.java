public class Student {
    String score;
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
