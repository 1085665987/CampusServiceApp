package t.f.recyclerimage.lost_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import Adapters.LostForum_Adapter;
import JavaBeans.LostForumBean;
import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.My_Lost_Activity;
import t.f.recyclerimage.R;
import utils.NineGridTestModel;
import utils.PermissionsUtil;
import utils.Property;
import utils.StatusBarLightModeUtil;

public class Found_Fragment extends Fragment implements OnLoadMoreListener,OnRefreshListener{

    private View view;
    private ImageView back,mine,write;
    //分别是返回键，我发布的失物招领帖子，写失物招领帖子
    private RecyclerView recyclerView;      //诸多的失物招领的帖子

    private TextView title_txt;             //是失物还是招领的title

    private LostForum_Adapter adapter;        //给那些帖子添加适配器
    private List<LostForumBean> lostForumBeans=new ArrayList<>();//数据源，从网络获得

    private SwipeToLoadLayout swipeToLoadLayout;

    private int count=0;

    private static String TAG="Lost_Fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.lost_things,null);

        initView();
        onClick();

        getData();

        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getActivity().getWindow(),true);
        return view;
    }
    private void initView() {
        back=(ImageView)view.findViewById(R.id.back);
        mine=(ImageView)view.findViewById(R.id.my_lost) ;
        write=(ImageView)view.findViewById(R.id.write);

        title_txt=(TextView)view.findViewById(R.id.top_txt);
        title_txt.setText("失物");

        recyclerView=(RecyclerView)view.findViewById(R.id.swipe_target);

        swipeToLoadLayout = ((SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout));

        adapter=new LostForum_Adapter(lostForumBeans,getActivity());
        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void onClick(){

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), My_Lost_Activity.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new LostForum_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag, View view, int position) {
                LostForumBean lostForumBean=lostForumBeans.get(position);
                switch (tag){
                    case 102:               //查看头像
                        break;
                    case 100:               //查看帖子主人
                        break;
                    case 105:               //分享
                        break;
                    case 104:               //联系此人
                        if (PermissionsUtil.callPhonePermission(getActivity())==1) {
                            String phone = lostForumBean.getUserId() + "";
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });

        swipeToLoadLayout.setOnRefreshListener(this);//下拉
        swipeToLoadLayout.setOnLoadMoreListener(this);//上拉

    }
    private void getData(){
        count=0;
        lostForumBeans.clear();
        String url=getResources().getString(R.string.lost_found_down)+"?phone="+new UserInfo(getActivity()).getLongInfo(Property.NO)+"&num=0&type=found";

        Log.i(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "服务器出错了哦", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("rs")==null||(jsonObject.getString("rs").equals("null"))){
                        Toast.makeText(getActivity(), "还没有人发帖，哦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("rs");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);

                        LostForumBean lostForumBean=new LostForumBean();
                        lostForumBean.setUserNickname(object.getString(Property.JSON_COMMENT_USERNAME_KEY));
                        lostForumBean.setUserSex(object.getString(Property.forumUserSex_key));
                        lostForumBean.setUserImg(object.getString(Property.JSON_COMMENT_HEAD_KEY));
                        lostForumBean.setForumDate(object.getString(Property.JSON_COMMENT_TIME_KEY));
                        lostForumBean.setUserSchool(object.getString(Property.SCHOOL_NAME));
                        lostForumBean.setForumContent(object.getString(Property.JSON_COMMENT_CONTENT_KEY));
                        lostForumBean.setLost(false);

                        /**
                         * 添加实景图片
                         * 最多有三张
                         */
                        String picture_1=object.getString("picture1");
                        String picture_2=object.getString("picture2");
                        String picture_3=object.getString("picture3");
                        NineGridTestModel model1 = new NineGridTestModel();
                        if (picture_1!=null&&!(picture_1.trim().equals("null"))&&!(picture_1.trim().equals(""))){
                            model1.urlList.add(getResources().getString(R.string.base_image_url)+picture_1);//设置帖子的图片
                        }
                        if (picture_2!=null&&!(picture_2.trim().equals("null"))&&!(picture_2.trim().equals(""))){
                            model1.urlList.add(getResources().getString(R.string.base_image_url)+picture_2);//设置帖子的图片
                        }
                        if (picture_3!=null&&!(picture_3.trim().equals("null"))&&!(picture_3.trim().equals(""))){
                            model1.urlList.add(getResources().getString(R.string.base_image_url)+picture_3);//设置帖子的图片
                        }
                        lostForumBean.setImageList(model1);

                        lostForumBeans.add(lostForumBean);
                    }
                    adapter.notifyItemChanged(0);
//                    adapter.notifyItemRangeChanged(0,lostForumBeans.size());

                    Log.i(TAG,"getData()"+lostForumBeans.size()+"条数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onLoadMore() {
        count++;

        String url=getResources().getString(R.string.lost_found_down)+"?phone="+new UserInfo(getActivity()).getLongInfo(Property.NO)+"&num="+count+"&type=lost";

        Log.i(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "服务器出错了哦", Toast.LENGTH_SHORT).show();
                swipeToLoadLayout.setLoadingMore(false);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("rs")==null||(jsonObject.getString("rs").equals("null"))){
                        Toast.makeText(getActivity(), "还没有人发帖，哦", Toast.LENGTH_SHORT).show();
                        swipeToLoadLayout.setLoadingMore(false);
                        return;
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("rs");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);

                        LostForumBean lostForumBean=new LostForumBean();
                        lostForumBean.setUserNickname(object.getString(Property.JSON_COMMENT_USERNAME_KEY));
                        lostForumBean.setUserImg(object.getString(Property.JSON_COMMENT_HEAD_KEY));
                        lostForumBean.setForumDate(object.getString(Property.JSON_COMMENT_TIME_KEY));
                        lostForumBean.setUserSchool(object.getString(Property.SCHOOL_NAME));
                        lostForumBean.setForumContent(object.getString(Property.JSON_COMMENT_CONTENT_KEY));
                        lostForumBean.setUserSex(object.getString(Property.forumUserSex_key));
                        lostForumBean.setLost(true);

                        lostForumBeans.add(lostForumBean);
                    }
                    adapter.notifyItemRangeInserted(count*10,20);

                    swipeToLoadLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        count=0;
        lostForumBeans=new ArrayList<>();
        String url=getResources().getString(R.string.lost_found_down)+"?phone="+new UserInfo(getActivity()).getLongInfo(Property.NO)+"&num=0&type=lost";

        Log.i(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "服务器出错了哦", Toast.LENGTH_SHORT).show();
                swipeToLoadLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("rs")==null||(jsonObject.getString("rs").equals("null"))){
                        Toast.makeText(getActivity(), "还没有人发帖，哦", Toast.LENGTH_SHORT).show();
                        swipeToLoadLayout.setRefreshing(false);
                        return;
                    }
                    JSONArray jsonArray=jsonObject.getJSONArray("rs");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);

                        LostForumBean lostForumBean=new LostForumBean();
                        lostForumBean.setUserSex(object.getString(Property.forumUserSex_key));
                        lostForumBean.setUserNickname(object.getString(Property.JSON_COMMENT_USERNAME_KEY));
                        lostForumBean.setUserImg(object.getString(Property.JSON_COMMENT_HEAD_KEY));
                        lostForumBean.setForumDate(object.getString(Property.JSON_COMMENT_TIME_KEY));
                        lostForumBean.setUserSchool(object.getString(Property.SCHOOL_NAME));
                        lostForumBean.setForumContent(object.getString(Property.JSON_COMMENT_CONTENT_KEY));
                        lostForumBean.setLost(false);

                        lostForumBeans.add(lostForumBean);
                    }
//                    adapter.notifyItemChanged(0);
                    adapter.notifyItemChanged(0,lostForumBeans.size());
//                    adapter./
                    swipeToLoadLayout.setRefreshing(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
