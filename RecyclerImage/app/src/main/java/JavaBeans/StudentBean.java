package JavaBeans;

/**
 * Created by Friday on 2018/7/14.
 */

public class StudentBean {
    private String name;
    private long sno;
    private int userId;
    private String schoolId;

    public long getSno() {
        return sno;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSno(long sno) {
        this.sno = sno;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
