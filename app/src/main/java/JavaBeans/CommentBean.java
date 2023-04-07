package JavaBeans;

import java.util.List;
import java.util.Map;

/**
 * Created by Friday on 2018/8/2.
 */

public class CommentBean {
    private int commentId;                  //评论ID
    private long commenterId;               //评论者的ID
    private String commenterHead;               //评论者的头像
    private String commenterNickname;       //评论者昵称
    private String commentContext;          //评论正文
    private String commentTime;             //这条评论发表的时间

    private List<Map<String,Object>> answerList;    //以键值对形式存放回复

    public List<Map<String, Object>> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Map<String, Object>> answerList) {
        this.answerList = answerList;
    }

    private List<AnswerBean> answerBeans;      //诸多条回复

    public List<AnswerBean> getAnswerBeans() {return answerBeans;}

    public void setAnswerBeans(List<AnswerBean> answerBeans) {
        this.answerBeans = answerBeans;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getCommenterHead() {
        return commenterHead;
    }

    public void setCommenterHead(String commenterHead) {
        this.commenterHead = commenterHead;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContext() {
        return commentContext;
    }

    public void setCommentContext(String commentContext) {
        this.commentContext = commentContext;
    }

    public String getCommenterNickname() {
        return commenterNickname;
    }

    public void setCommenterNickname(String commenterNickname) {
        this.commenterNickname = commenterNickname;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(long commenterId) {
        this.commenterId = commenterId;
    }
}
