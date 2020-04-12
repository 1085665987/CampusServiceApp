package JavaBeans;

/**
 * Created by Friday on 2018/7/9.
 */

public class UserBean {

    public long getNo() {
        return no;
    }
    public void setNo(long no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getStar() {
        return star;
    }
    public void setStar(String star) {
        this.star = star;
    }
    public String getEthic() {
        return ethic;
    }
    public void setEthic(String ethic) {
        this.ethic = ethic;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPost() {
        return post;
    }
    public void setPost(int state) {
        this.state = state;
    }

    private long no;
    private String name;
    private String pwd;
    private int age;
    private String sex;
    private String star;
    private String ethic;
    private String photo;
    private String comment;
    private String signature;
    private String ip;
    private int post;
    private int state;
    public int getState() {
        return post;
    }
    public void setState(int state) {
        this.state = state;
    }


    private String nickName;

    public String getNikeName() {
        return nickName;
    }

    public void setNikeName(String nickName) {
        this.nickName = nickName;
    }
}
