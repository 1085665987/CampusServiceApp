package JavaBeans;

import java.io.Serializable;
import java.util.List;

import utils.NineGridTestModel;

/**
 * Created by Friday on 2018/7/17.
 */

public class ForumBean implements Serializable{

    private int forumId;                //每条帖子有一个ID
    private String forumContent;        //帖子正文
    private String[] forumImgs;         //帖子附加的图片，该为图片网址

    private int userId;                 //发帖人ID
    private String forumDate;           //发帖日期
    private String userImg;             //发帖人头像
    private String userSex;             //发帖人性别
    private String userNickname;       //发帖人昵称
    private String userSchool;          //发帖人所在学校

    private int loveCount;              //赞贴数量
    private List<UserBean> lovers;      //点赞的人

    private int commentCount;           //评论数量

    private List<CommentBean> commentBeans;//诸多评论

    public List<UserBean> getLovers() {
        return lovers;
    }

    public void setLovers(List<UserBean> lovers) {
        this.lovers = lovers;
    }

    public List<CommentBean> getCommentBeans() {
        return commentBeans;
    }

    public void setCommentBeans(List<CommentBean> commentBeans) {
        this.commentBeans = commentBeans;
    }

    private boolean isMine;                //帖子是否是自己的

    private boolean isLoved;                //赞没赞过这帖子

    public boolean isLoved() {
        return isLoved;
    }

    public void setLoved(boolean loved) {
        isLoved = loved;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setIsMine(boolean mine) {
        isMine = mine;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String[] getForumImgs() {
        return forumImgs;
    }

    public void setForumImgs(String[] forumImgs) {
        this.forumImgs = forumImgs;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(int loveCount) {
        this.loveCount = loveCount;
    }

    public String getForumDate() {
        return forumDate;
    }

    public void setForumDate(String forumDate) {
        this.forumDate = forumDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    private NineGridTestModel imageList;

    public void setImageList(NineGridTestModel mList){
        this.imageList=mList;
    }
    public  NineGridTestModel getImageList(){
            return imageList;
    }
}
