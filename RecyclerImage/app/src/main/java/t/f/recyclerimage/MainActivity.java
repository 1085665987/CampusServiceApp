package t.f.recyclerimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Adapters.GoodsAdapter;
import JavaBeans.GoodsBean;
import views.PullRefreshRecyclerView;

public class MainActivity extends AppCompatActivity {
    private Button stop,stop_;

    private List<GoodsBean> goodsList=new ArrayList<>();
    private PullRefreshRecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setGoodsList();

        stop=(Button)findViewById(R.id.stop);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.stopRefresh();
            }
        });

        stop_=(Button)findViewById(R.id.stop_);
        stop_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.stopLoadMore();
            }
        });

        recyclerView=(PullRefreshRecyclerView)findViewById(R.id.recyclerview);
        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局的意思
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //设置RecyclerView 布局
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        GoodsAdapter adapter = new GoodsAdapter(goodsList,this);
        recyclerView.setAdapter(adapter);
    }
    private void setGoodsList(){
        for(int i=0;i<12 ;i++){
            GoodsBean goods=new GoodsBean();
            goods.setGoodsId(i);
            goods.setGoodsPrice(50+i*3);
            goods.setGoodsDescribe("这是第"+i+"件商品");
            goods.setGoodsImgId("http://192.168.1.3:8080/image/"+i+".jpg");
            goods.setGoodsPlace("杭州");
            goods.setGoodsPostage(i+5+"");
            goodsList.add(goods);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.out_actitity, R.anim.in_actitity);// 淡出淡入动画效果
    }
}
