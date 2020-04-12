package JavaBeans;

import utils.NineGridTestModel;

/**
 * Created by Friday on 2018/8/11.
 */

public class LostForumBean {
    private int forumId;                //每条帖子有一个ID
    private String forumContent;        //帖子正文

    private long userId;                 //发帖人ID，即联系方式
    private String forumDate;           //发帖日期
    private String userImg;             //发帖人头像
    private String userSex;             //发帖人性别
    private String userNickname;       //发帖人昵称
    private String userSchool;          //发帖人所在学校

    private boolean isLost;             //是为丢失，还是为捡到

    private String[] things;              //丢失的东西有哪些
    private NineGridTestModel imageList;//东西的图片

    public String[] getThings() {
        return things;
    }

    public void setThings(String[] things) {
        this.things = things;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getForumDate() {
        return forumDate;
    }

    public void setForumDate(String forumDate) {
        this.forumDate = forumDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getForumContent() {
        return forumContent;
    }

    public void setForumContent(String forumContent) {
        this.forumContent = forumContent;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public void setImageList(NineGridTestModel mList){
        this.imageList=mList;
    }
    public  NineGridTestModel getImageList(){
        return imageList;
    }
}
