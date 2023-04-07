package JavaBeans;

/**
 * Created by Friday on 2018/8/2.
 */

public class AnswerBean {
    private int answerId;           //回复ID

    private String answerTime;          //回复时间
    private UserBean answer;            //回复人
    private UserBean answeder;          //被回复人

    private String answerContext;       //回复内容

    public int getAnswerId() {
        return answerId;
    }

    public String getAnswerContext() {
        return answerContext;
    }

    public void setAnswerContext(String answerContext) {
        this.answerContext = answerContext;
    }

    public UserBean getAnsweder() {

        return answeder;
    }

    public void setAnsweder(UserBean answeder) {
        this.answeder = answeder;
    }

    public UserBean getAnswer() {

        return answer;
    }

    public void setAnswer(UserBean answer) {
        this.answer = answer;
    }

    public String getAnswerTime() {

        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public void setAnswerId(int answerId) {

        this.answerId = answerId;
    }
}
