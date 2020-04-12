package utils;

/**
 * Created by Friday on 2018/7/13.
 */

public class Property {
    public static String NO="no";
    public static String SEX="sex";
    public static String PASS="pass";
    public static String NAME="name";
    public static String AGE="age";
    public static String PHOTO="photo";
    public static String NICKNAME="nickname";

    public static String CARD="card";

    public static int ON_LINE=1;
    public static int OUT_LINE=0;
    public static String state="state";

    public static String SCHOOL_ID="school_id";
    public static String SCHOOL_NAME="school_name";
    public static String SCHOOL_XIAOHUI="xiaohui";
    public static String SCHOOL_PICTURES="school_p";

    public static String STUDENT_ID="id";
    public static String STUDENT_NAME="name";
    public static String STUDENT_NO="student_no";
    public static String STUDENT_SEX="sex";
    public static String STUDENT_SCHOOLID="schoolId";

    public static String forum_id_key="id";
    public static String forum_time_key="time";
    public static String forumUserSex_key="sex";
    public static String forumUsername_key="user_name";
    public static String forumUserHeadxiang_key="head";
    public static String forum_content_key="content";
    public static String forum_love_count_key="zan";
    public static String forum_comment_count_key="comment";
    public static String forum_pictures_key="tu";
    public static String forum_isOrNoMine_key="my";     //是不是我的贴子
    public static String forum_isOrNoLoved_key="is_zan";   //赞没赞过这个帖子

    public static String forum_isLoved_key="Y";         //赞过帖子
    public static String forum_noLoved_key="Y";         //没赞过帖子

    public static String forum_isMine_key="Y";          //是我的帖子
    public static String forum_noMine_key="N";          //不是我的帖子



    public static int FORUM_ITEM_PARENT_LAYOUT=100;         //帖子父控件的监听的tag
    public static int FORUM_ITEM_SHOW_DIALOG_TAG=101;       //点击显示 删除、评论的dialog的tag
    public static int FORUM_ITEM_TOUXIANG_TAG=102;          //头像点击时间的tag
    public static int FORUM_ITEM_LOVE_TAG=103;              //赞 点击事件的tag
    public static int FORUM_ITEM_COMMENT_TAG=104;           //评论点击事件的tag
    public static int FORUM_ITEM_SHARE_TAG=105;             //分享事件的tag

    public static int COMMENT_ITEM_COMMENTER_HEAD=100;      //评论者头像的点击事件的监听tag
    public static int COMMENT_ITEM_COMMENTER_NICKNAME=100;  //评论者昵称的点击事件的监听tag，与头像一样都是100，点击之后进入被点击者的个人主页
    public static int COMMENT_ITEM_LOVE_COMMENT=101;        //给评论点赞的点击监听事件的tag
    public static int COMMENT_ITEM_COMMENT_COMMENT=102;     //评论  这个评论  的点击监听事件的tag

    public static String ANSWER_ITEM_ANSWER_KEY="answer";       //回复评论时用的回复人的key
    public static String ANSWER_ITEM_ANSWEDER_KEY="answeder";       //回复评论时用的被回复人的key
    public static String ANSWER_ITEM_CONTEXT_KEY="answer";       //回复评论时用的回复正文的key

    public static String FORUM_DETAILS_AVTIVITY_PARAM_KEY="forum_key";       //获取序列化对象的key

    public static String JSON_COMMENT_ID_KEY="comment_id";              //解析JSON数据时用的评论ID的key
    public static String JSON_COMMENTERR_ID_KEY="user_phone";           //解析JSON数据时用的评论者ID的key
    public static String JSON_COMMENT_USERNAME_KEY="user_name";         //解析JSON数据时用的评论人昵称的key
    public static String JSON_COMMENT_HEAD_KEY="head";                  //解析JSON数据时用的评论人头像的key
    public static String JSON_COMMENT_CONTENT_KEY="content";            //解析JSON数据时用的评论正文的key
    public static String JSON_COMMENT_TIME_KEY="time";                  //解析JSON数据时用的评论的发布时间的key
    public static String JSON_COMMENT_ANSWER_KEY="back";                //解析JSON数据时用的回复评论数组的key

    public static String JSON_ANSWER_ME_KEY="me";                 //解析JSON数据时用的回复人昵称的key
    public static String JSON_ANSWER_YOU_KEY="you";              //解析JSON数据时用的评论人昵称的key
    public static String JSON_ANSWER_CONTENT_KEY="content";          //解析JSON数据时用的回复正文的key

}
