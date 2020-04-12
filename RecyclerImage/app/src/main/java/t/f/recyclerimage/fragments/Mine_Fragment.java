package t.f.recyclerimage.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import JavaBeans.SchoolBean;
import JavaBeans.StudentBean;
import JavaBeans.UserBean;
import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.Login_Activity;
import t.f.recyclerimage.My_Data_Activity;
import t.f.recyclerimage.PhotoView_Activity;
import t.f.recyclerimage.R;
import t.f.recyclerimage.StudentCard_Activity;
import t.f.recyclerimage.image_fragments.ImagePagerActivity;
import utils.GlideCircleTransform;
import utils.Property;
import views.CommomDialog;

/**
 * Created by Friday on 2018/7/11.
 */

public class Mine_Fragment extends Fragment implements View.OnClickListener{
    private static String TAG="Mine_Fragment";

    private View view;
    private ImageView head;
    private TextView nickname,school,my_forum_count,loved_forum_count,my_bean_count;
    private LinearLayout my_forum,loved_forum,my_beans;
    private RelativeLayout card_layout,pay_bean_layout,save_bean_layout,help_layout,setting_layout,mine;

    private CommomDialog outloginDialog;
    private RelativeLayout outlogin_relativeLayout;//退出登录

    private UserBean userBean;
    private StudentBean studentBean;
    private SchoolBean schoolBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment, null);

        initViews();
        setOnClick();

        getPersonData();
        return view;
    }

    private void initViews() {
        outlogin_relativeLayout=(RelativeLayout)view .findViewById(R.id.outlogin);

        outloginDialog=new CommomDialog(getActivity(), R.style.dialog, "您确定退出登录？", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {if (confirm){outLoign();}}
        });
        head=(ImageView)view.findViewById(R.id.head);

        nickname=(TextView)view.findViewById(R.id.nickname);
        school=(TextView)view.findViewById(R.id.school);
        my_forum_count=(TextView)view.findViewById(R.id.my_forum_count);
        loved_forum_count=(TextView)view.findViewById(R.id.loved_forum_count);
        my_bean_count=(TextView)view.findViewById(R.id.my_bean_count) ;

        my_forum=(LinearLayout)view.findViewById(R.id.my_forum);
        loved_forum=(LinearLayout)view.findViewById(R.id.my_loved);
        my_beans=(LinearLayout)view.findViewById(R.id.my_beans) ;

        mine=(RelativeLayout)view.findViewById(R.id.mine);
        card_layout=(RelativeLayout)view.findViewById(R.id.school_card_item) ;
        pay_bean_layout=(RelativeLayout)view.findViewById(R.id.bean_item);
        save_bean_layout=(RelativeLayout)view.findViewById(R.id.zhuan_bean_item);
        help_layout=(RelativeLayout)view.findViewById(R.id.help_item);
        setting_layout=(RelativeLayout)view.findViewById(R.id.setting_item);
    }
    private void setOnClick(){
        outlogin_relativeLayout.setOnClickListener(this);
        my_forum.setOnClickListener(this);
        loved_forum.setOnClickListener(this);
        my_beans.setOnClickListener(this);

        card_layout.setOnClickListener(this);
        pay_bean_layout.setOnClickListener(this);
        save_bean_layout.setOnClickListener(this);
        help_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);

        head.setOnClickListener(this);
        mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.outlogin:
                outloginDialog.setTitle("提示").show();
                break;
            case R.id.head:                      //查看头像的大图
//                Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
                intent = new Intent(getActivity(), PhotoView_Activity.class);
                Log.e(TAG,userBean.getPhoto());
                List<String> urls=new ArrayList<String>();
                urls.add(userBean.getPhoto());
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) urls);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                intent.putExtra(PhotoView_Activity.EXTRA_IMAGE_URL,userBean.getPhoto());
                getActivity().startActivity(intent);
                break;
            case R.id.nickname:                  //显示昵称
                break;
            case R.id.school:                   //显示学校
                break;
            case R.id.mine:                     //点击进入关于我的详情
                intent=new Intent(getActivity(), My_Data_Activity.class);
                startActivity(intent);
                break;

            case R.id.my_forum:             //点击之后是我发布的所有的帖子
                break;
            case R.id.my_loved:             //点击之后是我点过赞的帖子
                break;
            case R.id.my_beans:             //点击之后是
                break;

            case R.id.school_card_item:     //点击之后是校园卡信息
                intent=new Intent(getActivity(), StudentCard_Activity.class);
                startActivity(intent);
                break;
            case R.id.bean_item:            //点击之后是豆子购物
                break;
            case R.id.zhuan_bean_item:      //点击之后是赚豆子
                break;
            case R.id.help_item:            //帮助
                break;
            case R.id.setting_item:         //设置
                break;
        }
    }

    private void outLoign(){
        UserInfo userInfo=new UserInfo(getActivity());
        String url=getActivity().getResources().getString(R.string.outlogin_url)+"?phone="+userInfo.getLongInfo(Property.NO);
        //改变数据库登录状态
        userInfo.clear();                    //清空本地暂存账号的文件
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {}
            @Override
            public void onResponse(String response, int id) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        if(outloginDialog != null) {
            outloginDialog.dismiss();
        }
        super.onDestroy();
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            // 可视
        } else {
            // 不可视
        }
    }

    private void getPersonData(){
        String url=getActivity().getResources().getString(R.string.mine_data)+"?phone="+new UserInfo(getActivity()).getLongInfo(Property.NO);
        userBean=new UserBean();
        schoolBean=new SchoolBean();
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                String no_data=getActivity().getResources().getString(R.string.no_date);
                nickname.setText(no_data);
                school.setText(no_data);

                my_forum_count.setText(no_data);
                loved_forum_count.setText(no_data);
                my_bean_count.setText(no_data);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    userBean.setNikeName(jsonObject.getString(Property.forumUsername_key));
                    schoolBean.setSchoolName(jsonObject.getString(Property.SCHOOL_NAME));

                    nickname.setText(userBean.getNikeName());
                    school.setText(schoolBean.getSchoolName());

                    my_forum_count.setText(jsonObject.getString("tiezi"));
                    loved_forum_count.setText(jsonObject.getString("zanshu"));
                    my_bean_count.setText(jsonObject.getString("dou"));
                } catch (Exception e){e.printStackTrace();}
            }
        });
        OkHttpUtils.get().url(getResources().getString(R.string.image_url)+"?phone="+new UserInfo(getActivity()).getLongInfo(Property.NO)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "服务器出错了", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    if(response.trim().equals("0")){
                        return;
                    }
                    String imageUrl =getResources().getString(R.string.base_image_url)+response;
                    userBean.setPhoto(imageUrl);
                    Log.i(TAG,imageUrl);
                    Glide.with(getActivity()).load(imageUrl).transform(new GlideCircleTransform(getActivity())).into(head);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
