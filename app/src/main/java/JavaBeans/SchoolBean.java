package JavaBeans;

import java.util.List;

/**
 * Created by Friday on 2018/7/14.
 */

public class SchoolBean {
    private int schoolId;
    private String schoolName;
    private String schoolXiaohui;

    public List<String> getSchoolPicture() {
        return schoolPicture;
    }

    public void setSchoolPicture(List<String> picture) {
        this.schoolPicture = picture;
    }

    private List<String> schoolPicture;

    public String getSchoolXiaohui() {
        return schoolXiaohui;
    }

    public void setSchoolXiaohui(String xiaohui) {
        this. schoolXiaohui= xiaohui;
    }

    public String getSchoolName() {

        return schoolName;
    }

    public void setSchoolName(String name) {
        this.schoolName = name;
    }

    public int getSchoolId() {

        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
