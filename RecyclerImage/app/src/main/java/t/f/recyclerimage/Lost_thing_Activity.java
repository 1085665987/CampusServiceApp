package t.f.recyclerimage;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import Adapters.LostForum_Adapter;
import JavaBeans.LostForumBean;
import infos.UserInfo;
import okhttp3.Call;
import utils.Property;

/**
 * Created by Friday on 2018/8/11.
 */

public class Lost_thing_Activity extends Activity {
    private ImageView back,mine,write;
                    //分别是返回键，我发布的失物招领帖子，写失物招领帖子
    private RecyclerView recyclerView;      //诸多的失物招领的帖子

    private LostForum_Adapter adapter;        //给那些帖子添加适配器
    private List<LostForumBean> lostForumBeans;//数据源，从网络获得
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_things);

        initView();
        onClick();
    }



    private void initView() {
        back=(ImageView)findViewById(R.id.back);
        mine=(ImageView)findViewById(R.id.my_lost) ;
        write=(ImageView)findViewById(R.id.write);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        adapter=new LostForum_Adapter(lostForumBeans,this);
    }
    private void onClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        adapter.setOnItemClickListener(new LostForum_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int tag, View view, int position) {
                switch (tag){
                    case 102:               //查看头像
                        break;
                    case 100:               //查看帖子主人
                        break;
                    case 105:               //分享
                        break;
                    case 104:               //联系此人
                        break;
                }
            }
        });
    }
    private void getData(){
        String url=getResources().getString(R.string.lost_found_down)+"?phone="+new UserInfo(this).getLongInfo(Property.NO);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }
}
