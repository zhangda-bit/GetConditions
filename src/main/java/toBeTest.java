public class toBeTest {
    int studentId;
    String name;
    int classId;
    int age;
    char sex;

    public toBeTest(int studentId, String name, int classId, int age, char sex) {
        this.studentId = studentId;
        this.name = name;
        this.classId = classId;
        this.age = age;
        this.sex = sex;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void modelCheck() {
        float m = 1;
        if (m < 2){

        }
        if(this.getName() != null){

        }
        if (age < 18 && "1001".equals(classId)) {
            System.out.println("未成年");
        } else {
            System.out.println("成年");
        }
        if (sex == '男') {
            System.out.println("男");
        } else {
            System.out.println("女");
        }
        if ("".equals(classId)) {
            System.out.println("未分配班级");
        } else {
            System.out.println("班级为:" + classId);
        }
        if ("".equals(name)) {
            System.out.println("姓名未录入");
        } else {
            System.out.println("姓名为:" + name);
        }
    }
}
