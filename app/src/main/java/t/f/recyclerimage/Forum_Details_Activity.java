package t.f.recyclerimage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.utils.L;
import com.othershe.calendarview.utils.CalendarUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.Comment_Adapter;
import JavaBeans.AnswerBean;
import JavaBeans.CommentBean;
import JavaBeans.ForumBean;
import JavaBeans.UserBean;
import infos.UserInfo;
import okhttp3.Call;
import utils.ForumUtil;
import utils.GlideCircleTransform;
import utils.Property;
import utils.StatusBarLightModeUtil;
import views.CustomDialog;
import views.FullyLinearLayoutManager;
import views.NineGridTestLayout;

/**
 * Created by Friday on 2018/8/2.
 */

public class Forum_Details_Activity extends Activity implements View.OnClickListener {
    private static final String TAG = "Forum_Details_Activity";
    private RelativeLayout relativeLayout;                          //显示点赞人 的 父控件
    private TextView[] lovers_txt;                                   //显示点赞人
    private List<UserBean> lovers_users;                               //点赞人

    private ForumBean forumBean;                                    //本帖子的信息

    private LinearLayout back;
    private ImageView pb, hand, sex, love, comment, share;
    //分别是头部旋转进度环，头像，性别，点赞，评论，分享
    private TextView forumer, time, forumer_school, context, lovers_nickname;
    //分别是发帖人昵称，发帖时间，发帖人学校，帖子正文,赞帖子的人
    private NineGridTestLayout nineGridTestLayout;          //帖子的图片

    private RecyclerView recyclerView;                      //诸多的评论
    private Comment_Adapter commentAdapter;                 //评论的适配器

    private LinearLayout write_comment;                          //写评论的EditText

    private List<CommentBean> commentBeans = null;            //诸多评论

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_details);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(), true);
        initView();
        onClick();
        getLoversAndComments();
    }

    private void initView() {
        Intent intent = getIntent();
        forumBean = (ForumBean) intent.getSerializableExtra(Property.FORUM_DETAILS_AVTIVITY_PARAM_KEY);//获取序列化对象

        back = (LinearLayout) findViewById(R.id.back);
        pb = (ImageView) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        hand = (ImageView) findViewById(R.id.say_item_touxiang);
        sex = (ImageView) findViewById(R.id.say_item_sayer_sex);
        love = (ImageView) findViewById(R.id.say_item_love);
        comment = (ImageView) findViewById(R.id.say_item_comment);
        share = (ImageView) findViewById(R.id.say_item_share);

        forumer = (TextView) findViewById(R.id.say_item_sayer);
        time = (TextView) findViewById(R.id.say_item_time);
        forumer_school = (TextView) findViewById(R.id.say_item_school);
        context = (TextView) findViewById(R.id.say_item_content);
        lovers_nickname = (TextView) findViewById(R.id.lovers_nikename);

        nineGridTestLayout = (NineGridTestLayout) findViewById(R.id.layout_nine_grid);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this,FullyLinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.comment_rectcle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        write_comment = (LinearLayout) findViewById(R.id.wirte_comment);

        Glide.with(this).load(forumBean.getUserImg()).transform(new GlideCircleTransform(this)).into(hand);     //加载头像
        if (forumBean.getUserSex().toString().trim().equals("女")) {                              //更改性别，默认为男
            sex.setImageResource(R.mipmap.woman);
        }
        if (forumBean.isLoved()) {                                                                      //设置点赞
            love.setImageResource(R.mipmap.ic_launcher_round);
        }
        time.setText(forumBean.getForumDate());                                                   //设置发帖时间
        forumer.setText(forumBean.getUserNickname());                                             //设置发帖人昵称
        forumer_school.setText(forumBean.getUserSchool());                                       //设置发帖人所在学校

        context.setText(forumBean.getForumContent());                                             //设置帖子正文

        if (forumBean.getImageList() == null) {                                                       //设置帖子图片
            nineGridTestLayout.setVisibility(View.GONE);
        } else {
            nineGridTestLayout.setIsShowAll(true);
            nineGridTestLayout.setUrlList(forumBean.getImageList().urlList);
        }


        /****
         *
         * 有Bug，待修改
         */
        commentBeans = new ArrayList<>();
        forumBean.setCommentBeans(commentBeans);

        commentAdapter = new Comment_Adapter(this,commentBeans);           //给评论加适配器
        recyclerView.setAdapter(commentAdapter);
    }

    private void onClick() {
        write_comment.setOnClickListener(this);
        back.setOnClickListener(this);
        hand.setOnClickListener(this);
        love.setOnClickListener(this);
        comment.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    private void addViews() {                                        //动态添加显示点赞人的TextView

        String[] txt_lovers_nickname = new String[lovers_users.size()];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < lovers_users.size(); i++) {
            if (i == lovers_users.size() - 1) {
                txt_lovers_nickname[i] = lovers_users.get(i).getNikeName();
                sb.append(lovers_users.get(i).getNikeName());
            } else {
                txt_lovers_nickname[i] = lovers_users.get(i).getNikeName() + "、";
                sb.append(lovers_users.get(i).getNikeName() + "、");
            }
        }
        SpannableString spannableString = new SpannableString(sb.toString() + "点了赞");
        int index = 0;
        for (int i = 0; i < lovers_users.size(); i++) {
            final int j = i;
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    //显示的文本有下划线，false没有
                }

                @Override
                public void onClick(View widget) {
                    Toast.makeText(Forum_Details_Activity.this, lovers_users.get(j).getNikeName() + "点了赞", Toast.LENGTH_SHORT).show();
                }
            }, index, txt_lovers_nickname[i].length() + index, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            index += txt_lovers_nickname[i].length();
        }
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, sb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        lovers_nickname.setMovementMethod(LinkMovementMethod.getInstance());
        lovers_nickname.setText(spannableString);
    }

    private void getLoversAndComments() {
        String url = getResources().getString(R.string.dongtai_in) + "?dongtai_id=" + forumBean.getForumId();
        Log.e(TAG, "getLoversAndComments()" + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                paramsJSON(response);
            }
        });
    }

    private void paramsJSON(String response) {
        Log.e(TAG, response);

        JSONObject commentJSON = null;
        try {
            commentJSON = new JSONObject(response);
            JSONArray commentArray = commentJSON.getJSONArray("comment");
            if (commentArray != null && commentArray.length() > 0) {
                for (int i =0; i <commentArray.length(); i++) {
                    CommentBean commentBean = new CommentBean();
                    JSONObject commentItem = commentArray.getJSONObject(i);
                    commentBean.setCommentId(commentItem.getInt(Property.JSON_COMMENT_ID_KEY));                 //获取评论ID
                    commentBean.setCommenterNickname(commentItem.getString(Property.JSON_COMMENT_USERNAME_KEY));//获取评论者昵称
                    commentBean.setCommenterHead(getResources().getString(R.string.base_image_url) + commentItem.getString(Property.JSON_COMMENT_HEAD_KEY));        //获取评论者的头像
                    commentBean.setCommentContext(commentItem.getString(Property.JSON_COMMENT_CONTENT_KEY));    //获取评论的正文
                    commentBean.setCommenterId(commentItem.getLong(Property.JSON_COMMENTERR_ID_KEY));           //获取评论者的id
                    commentBean.setCommentTime(commentItem.getString(Property.JSON_COMMENT_TIME_KEY));          //获取评论发布的时间

                    /****
                     *
                     * 有Bug，待修改
                     */
                    if (!commentItem.isNull(Property.JSON_COMMENT_ANSWER_KEY)) {
                        JSONArray answerArray = commentItem.getJSONArray(Property.JSON_COMMENT_ANSWER_KEY);           //获取回复的数据

                        if (answerArray != null && answerArray.length() > 0) {
                            List<AnswerBean> answerBeans = new ArrayList<>();
                            for (int j = 0; j < answerArray.length(); j++) {
                                JSONObject answerItem = answerArray.getJSONObject(j);

                                AnswerBean answerBean = new AnswerBean();

                                UserBean answer = new UserBean();
                                answer.setNikeName(answerItem.getString(Property.JSON_ANSWER_YOU_KEY));
                                answerBean.setAnswer(answer);


                                UserBean answeder = new UserBean();
                                answeder.setNikeName(answerItem.getString(Property.JSON_ANSWER_ME_KEY));
                                answerBean.setAnsweder(answeder);

                                answerBean.setAnswerContext(answerItem.getString(Property.JSON_ANSWER_CONTENT_KEY));

                                answerBeans.add(answerBean);
                            }
                            commentBean.setAnswerBeans(answerBeans);                    //给评论加上回复
                        }
                    }
                    Log.e(TAG,commentBean.getCommenterNickname()+"评论："+commentBean.getCommentContext());
                    commentBeans.add(commentBean);                                  //给诸多的评论绑定集合
                }
                forumBean.setCommentBeans(commentBeans);                    //给帖子加上评论
//                commentAdapter.notifyItemRangeChanged(0,commentBeans.size());
                commentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!commentJSON.isNull("zan_name")) {
            try {
                JSONArray loveArray = commentJSON.getJSONArray("zan_name");           //得到点赞的人的名字的数组
                if (loveArray != null && loveArray.length() > 0) {
                    lovers_users = new ArrayList<>();
                    for (int i = 0; i < loveArray.length(); i++) {
                        UserBean userBean = new UserBean();
                        String love_name = loveArray.getString(i);
                        userBean.setNikeName(love_name);
                        lovers_users.add(userBean);
                    }
                    addViews();
                }//在界面上动态添加点赞的人的名字
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lovers_nickname.setText("还没有人点赞");
        }
    }


    private void showWriteCommentDialog() {
        int ids[] = new int[]{R.id.cancel, R.id.send_comment, R.id.comment_context, R.id.biaoqing, R.id.pictures};
        CustomDialog dialog = new CustomDialog(this, ids);
        dialog.setOnClick(new CustomDialog.OnClick() {
            @Override
            public void onClick(Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.cancel:
                        dialog.cancel();
                        break;
                    case R.id.send_comment:
                        final String txt_comment = ((EditText) dialog.findViewById(R.id.comment_context)).getText().toString();
                        if (txt_comment.trim().equals("")) {
                            Toast.makeText(Forum_Details_Activity.this, "评论的内容不能为空，呦", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sendComment(txt_comment);
                        dialog.cancel();
                        OkHttpUtils.get().url(getResources().getString(R.string.image_url) + "?phone=" + new UserInfo(Forum_Details_Activity.this).getLongInfo(Property.NO)).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(Forum_Details_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e(TAG,"请求头像"+response);

                                try {
                                    if (response.trim().equals("0")) {
                                        return;
                                    }
                                    String imageUrl = getResources().getString(R.string.base_image_url) + response;
                                    Log.i(TAG, imageUrl);
                                    CommentBean commentBean = new CommentBean();                //获取评论ID
                                    commentBean.setCommenterNickname("我");//获取评论者昵称
                                    commentBean.setCommenterHead(imageUrl); //获取评论者的头像
                                    commentBean.setCommentContext(txt_comment);    //获取评论的正文
                                    int[] current = CalendarUtil.getCurrentDate();  //获取系统时间
                                    current[0] = current[0] % 100;
                                    commentBean.setCommentTime(current[0] + "年 " + current[1] + "/" + current[2]);          //获取评论发布的时间

                                    Log.e(TAG,"增加"+txt_comment);

                                    commentBeans.add(commentBean);                 //评论绑定集合
                                    forumBean.setCommentBeans(commentBeans);
//                                    commentAdapter.notifyItemInserted(0);
                                    commentAdapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case R.id.biaoqing:
                        break;
                    case R.id.pictures:
                        break;
                }
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void sendComment(String txt_comment) {
        String url = getResources().getString(R.string.comment_add);

        Map<String,String> data=new HashMap<>();
        data.put("dongtai_id",forumBean.getForumId()+"");
        data.put("content",txt_comment);
        data.put("phone",new UserInfo(this).getLongInfo(Property.NO)+"");
        OkHttpUtils.post().url(url).params(data).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wirte_comment:                //点击显示写评论的dialog
                showWriteCommentDialog();
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.say_item_touxiang:            //点击产看头像
                break;
            case R.id.say_item_love:                //给这帖子点赞
                forumBean = new ForumUtil().doLove(forumBean, Forum_Details_Activity.this);
                if (forumBean.isLoved()) {
                    love.setImageResource(R.mipmap.ic_launcher_round);
                } else {
                    love.setImageResource(R.mipmap.cancer);
                }
                break;
            case R.id.say_item_comment:             //给这帖子评论
                showWriteCommentDialog();
                break;
            case R.id.say_item_share:               //分享此帖子
                break;
        }
    }
}
