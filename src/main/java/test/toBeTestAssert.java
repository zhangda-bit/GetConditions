package test;

import java.util.ArrayList;
import java.util.List;

public class toBeTestAssert {
    public static void main(String[] args) {
        List<String> errmsgs = new ArrayList<>();
        Student s = new Student();
        if (Student.isNUllOrEmpty(s.getAppNo())){
            errmsgs.add("申请号不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getAppDataSource())){
            errmsgs.add("渠道号不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getAppNo())){
            errmsgs.add("申请号不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getCustSex())){
            errmsgs.add("客户性别不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getRelationRet())){
            errmsgs.add("联系人不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getCustName())){
            errmsgs.add("客户姓名（中文）不能为空！");
        }
        if (Student.isNUllOrEmpty(s.getCertInfo())){
            errmsgs.add("证件类型与号码不能为空！");
        }

    }
}