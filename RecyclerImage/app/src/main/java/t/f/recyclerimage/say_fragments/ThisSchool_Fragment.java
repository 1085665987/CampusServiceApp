package t.f.recyclerimage.say_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.ForumAdapter;
import JavaBeans.ForumBean;
import infos.UserInfo;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import okhttp3.Call;
import utils.ForumUtil;
import utils.NineGridTestModel;
import utils.Property;
import t.f.recyclerimage.R;
/**
 * Created by Friday on 2018/7/17.
 */

public class ThisSchool_Fragment extends Fragment implements OnRefreshListener, OnLoadMoreListener ,ForumAdapter.OnItemClickListener{
    public static String TAG="ThisSchool_Fragment";

    private  View view;
    private RecyclerView mRecycleView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private LinearLayoutManager layoutManager;

    private ForumAdapter forumAdapter;
    private AnimationAdapter alphaAdapter;
    private List<ForumBean> forumList=new ArrayList<>();

    private int count=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.say_fragment, null);
            initView();
//            swipeToLoadLayout.setRefreshing(true);
            getFriendCircleData();//数据请求
            setOnClickListener();
        }
        return view;
    }

    @Override
    public void onLoadMore() {
        count+=1;
        UserInfo userInfo=new UserInfo(getActivity());
        String url=getActivity().getResources().getString(R.string.dongtai_down)+ "?num="+count+"&school_id="+userInfo.getIntInfo(Property.SCHOOL_ID)+"&phone="+userInfo.getLongInfo(Property.NO);
        Log.e(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"服务器出错了",Toast.LENGTH_SHORT).show();
                swipeToLoadLayout.setLoadingMore(false);
            }
            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG,"onLoadMore>>>>"+response);
                addData(response);
                forumAdapter.notifyDataSetChanged();
                swipeToLoadLayout.setLoadingMore(false);
            }
        });

    }

    @Override
    public void onRefresh() {
        count=0;
        forumList=new ArrayList<>();
        UserInfo userInfo=new UserInfo(getActivity());
        String url=getActivity().getResources().getString(R.string.dongtai_down)+ "?num="+count+"&school_id="+userInfo.getIntInfo(Property.SCHOOL_ID)+"&phone="+userInfo.getLongInfo(Property.NO);
        Log.e(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"服务器出错了",Toast.LENGTH_SHORT).show();
                swipeToLoadLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response, int id) {
                addData(response);
                forumAdapter.notifyItemRangeChanged(0,forumList.size());
                swipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    private void initView(){
        swipeToLoadLayout = ((SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout));
        mRecycleView = ((RecyclerView) view.findViewById(R.id.swipe_target));
        layoutManager = new LinearLayoutManager(getActivity());//创建线性布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直方向
        mRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器

        FadeInDownAnimator animation=new FadeInDownAnimator();
        animation.setChangeDuration(500);
        mRecycleView.setItemAnimator(animation);//设置Item增加、移除动画

        forumAdapter=new ForumAdapter(forumList,getActivity());
        mRecycleView.setAdapter(forumAdapter);
    }

    private void setOnClickListener(){
        swipeToLoadLayout.setOnRefreshListener(this);//下拉
        swipeToLoadLayout.setOnLoadMoreListener(this);//上拉

        forumAdapter.setItemClickListener(this);
    }

    private void getFriendCircleData(){
        UserInfo userInfo=new UserInfo(getActivity());
        String url=getActivity().getResources().getString(R.string.dongtai_down)+ "?num="+0+"&school_id="+userInfo.getIntInfo(Property.SCHOOL_ID)+"&phone="+userInfo.getLongInfo(Property.NO);
        Log.e(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(),"服务器出错了",Toast.LENGTH_SHORT).show();
                swipeToLoadLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response, int id) {
                addData(response);
                forumAdapter.notifyDataSetChanged();
                swipeToLoadLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(int tag, View view, int position) {
        switch (tag){
            case 101:               //显示dialog   （删除、屏蔽帖子等功能）
                showDialog(view,position);
                break;
            case 103:
                doLove(position);
                break;
        }
    }
    public void showDialog(View v, final int position) {
        final ForumBean forumBean=forumList.get(position);
        final long phone=new UserInfo(getActivity()).getLongInfo(Property.NO);

        int[] coo = new int[2];// 获取小三角view位置，用来设置dialog的显示位置
        v.getLocationInWindow(coo);
    /* 先实例化dialog并进行相关设置 */
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        final LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.forum_dialog, null);
        ll.findViewById(R.id.share_by_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"通过QQ分享",Toast.LENGTH_SHORT).show();
            }
        });
        ll.findViewById(R.id.share_by_wecahr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"通过微信分享",Toast.LENGTH_SHORT).show();
            }
        });
        ll.findViewById(R.id.share_by_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"通过微博分享",Toast.LENGTH_SHORT).show();
            }
        });

        //删除帖子，先判断是不是本人的帖子，如果是则删除，不是的话，就不显示本行
        if(forumBean.getIsMine()) {
            ll.findViewById(R.id.forum_line_1).setVisibility(View.VISIBLE);

            LinearLayout delete_forum=(LinearLayout) ll.findViewById(R.id.forum_delete);
            delete_forum.setVisibility(View.VISIBLE);
            delete_forum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ForumUtil.doDeleteForum(getActivity(),forumBean.getForumId(),phone);
                    forumAdapter.notifyItemRemoved(position);
                    forumList.remove(position);
                    forumAdapter.notifyItemRangeChanged(position, forumList.size()+1-position);
                    dialog.cancel();
                }
            });
        }
        //关注此人，先判断是不是本人的帖子，如果是 不显示本行，不是的话，再判断是否已关注此人 若没有，就关注此人，若已关注，就显示取消关注
        Log.i(TAG,"是不是本人的帖子"+forumBean.getIsMine());
        if(forumBean.getIsMine()) {
            ll.findViewById(R.id.forum_line_2).setVisibility(View.GONE);
            ll.findViewById(R.id.interest).setVisibility(View.GONE);
        }else{
            String url=getActivity().getApplicationContext().getResources().getString(R.string.dongtai_is_attention)+"?dongtai_id="+forumBean.getForumId()+"&phone="+phone;
            Log.i(TAG,url);

            final LinearLayout interest=(LinearLayout)ll.findViewById(R.id.interest);
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {           //判断是否已关注此帖子的主人
                @Override
                public void onError(Call call, Exception e, int id) {}
                @Override
                public void onResponse(String response, int id) {
                    if(response.toString().trim().equals("N")){
                        interest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ForumUtil.doAttentionSB(getActivity(),forumBean.getForumId(),phone);        //关注此帖子的主人
                                Toast.makeText(getActivity(),"不是本人的帖子",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{                                                                      //若已关注
                        TextView txt=(TextView)ll.findViewById(R.id.interest_txt);          //文字显示“取消关注”
                        txt.setText("取消关注");
                        interest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ForumUtil.doCancelAttentionSB(getActivity(),forumBean.getForumId(),phone);  //取消关注
                            }
                        });
                    }
                }
            });
            dialog.cancel();
        }

        //屏蔽此人，先判断是不是本人的帖子，如果是则不显示本行，不是的话，就屏蔽此人
        if(forumBean.getIsMine()) {
            ll.findViewById(R.id.forum_line_3).setVisibility(View.GONE);
            LinearLayout block =(LinearLayout) ll.findViewById(R.id.block);
            block.setVisibility(View.GONE);
            block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "屏蔽此人", Toast.LENGTH_SHORT).show();

                    dialog.cancel();
                }
            });
        }
        ll.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setContentView(ll);
        //计算dialog显示的view的高度
        ll.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int sw = wm.getDefaultDisplay().getWidth();
        int sh = wm.getDefaultDisplay().getHeight();
        // 获取window并进行相关设置
        Window window = dialog.getWindow();
        if (coo[1] > sh * 0.65) {//sh指屏幕高度，sw指屏幕宽度，但小三角的位置超过屏幕的65%时，弹窗就变为在小三角上面显示
            window.setWindowAnimations(R.style.dialog); // 添加动画
            //coo[1] -= sh * 0.2;//y轴减去sh*0.2是因为dialog的高度为sh*0.2
        } else {
            window.setWindowAnimations(R.style.dialog); // 添加动画
        }
        window.setGravity(Gravity.TOP);
        /*一定要设置为TOP，因为lp中的设置的x,y都是基于这个位置的，如果为TOP，lp设置的y为0时，就在最上面显示，如果为CENTER，y为0时，dialog就在中间显示*/
        /* window的宽高，为window的一部分 */
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = sw;
        //lp.height =ll.getMeasuredHeight();
        lp.x=0;
        lp.y = coo[1];
        window.setAttributes(lp);
    }

    private void addData(String response){
        try {
            ForumBean forumBean;
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.get("rs")==null||jsonObject.get("rs").toString().trim().equals("null")){
                if(swipeToLoadLayout.isLoadingMore()) {
                    swipeToLoadLayout.setLoadingMore(false);
                    Toast.makeText(getActivity(), "没有更多辣", Toast.LENGTH_SHORT).show();
                }
                if(swipeToLoadLayout.isRefreshing())
                    swipeToLoadLayout.setRefreshing(false);
                return;
            }
            JSONArray jsonArray=jsonObject.getJSONArray("rs");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject mJsonObject=jsonArray.getJSONObject(i);
                forumBean=new ForumBean();                  //初始化“帖子实体”
                forumBean.setForumId(mJsonObject.getInt(Property.forum_id_key));
                forumBean.setIsMine(false);                                                         //是不是我的帖子，默认不是
                if(mJsonObject.getString(Property.forum_isOrNoMine_key).trim().equals(Property.forum_isMine_key))
                    forumBean.setIsMine(true);
                forumBean.setUserNickname(mJsonObject.getString(Property.forumUsername_key));       //设置发帖人
                forumBean.setUserImg(getResources().getString(R.string.base_image_url)+mJsonObject.getString(Property.forumUserHeadxiang_key));       //设置发帖人头像
                forumBean.setForumDate(mJsonObject.getString(Property.forum_time_key));             //设置发帖日期
                forumBean.setUserSchool(mJsonObject.getString(Property.SCHOOL_NAME));               //设置发帖人学校名称
                forumBean.setForumContent(mJsonObject.getString(Property.forum_content_key));       //设置帖子正文
                forumBean.setUserSex(mJsonObject.getString(Property.forumUserSex_key));             //设置发帖人性别

                forumBean.setLoved(false);                                           //赞没赞过帖子，默认没有
                if(mJsonObject.getString(Property.forum_isOrNoLoved_key).trim().equals(Property.forum_isLoved_key))
                    forumBean.setLoved(true);

                JSONArray pictureJsons = mJsonObject.getJSONArray(Property.forum_pictures_key);
                String [] pictures=new String[pictureJsons.length()];
                NineGridTestModel model1 = new NineGridTestModel();
                for(int j=0;j<pictureJsons.length();j++){
                    Log.i(TAG,pictureJsons.getString(j));
                    if(pictureJsons.getString(j)!=null&&!pictureJsons.getString(j).equals("")) {
                        pictures[j] = getResources().getString(R.string.base_image_url)+pictureJsons.getString(j);
                        model1.urlList.add(pictures[j]);                                 //设置帖子的图片
                    }
                }
                forumBean.setImageList(model1);
                forumBean.setLoveCount(mJsonObject.getInt(Property.forum_love_count_key));          //设置帖子的点赞人数
                forumBean.setCommentCount(mJsonObject.getInt(Property.forum_comment_count_key));       //设置帖子的评论人数

                forumList.add(forumBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doLove(int position){
        final ForumBean forumBean=forumList.get(position);
        final long phone=new UserInfo(getActivity()).getLongInfo(Property.NO);
        if(!forumBean.isLoved()) {
            String url = getActivity().getResources().getString(R.string.forum_love) + "?dongtai_id=" + forumBean.getForumId() + "&phone=" + phone;
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Log.i(TAG, response);
                }
            });
            forumBean.setLoved(!forumBean.isLoved());
            forumBean.setLoveCount(forumBean.getLoveCount()+1);
            forumAdapter.notifyItemChanged(position);
        }else{
            String url = getActivity().getResources().getString(R.string.forum_love_cancel) + "?dongtai_id=" + forumBean.getForumId() + "&phone=" + phone;
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }
                @Override
                public void onResponse(String response, int id) {
                    Log.i(TAG, response);
                }
            });
            forumBean.setLoveCount(forumBean.getLoveCount()-1);
            forumBean.setLoved(!forumBean.isLoved());
            forumAdapter.notifyItemChanged(position);
        }
    }
}
