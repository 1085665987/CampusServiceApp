package t.f.recyclerimage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import Adapters.GoodsAdapter;
import JavaBeans.GoodsBean;

/**
 * Created by Friday on 2018/7/17.
 */

public class LoaderMore_Refresf_Activity extends Activity implements OnRefreshListener, OnLoadMoreListener {
    private RecyclerView mRecycleView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private LinearLayoutManager layoutManager;

    private List<GoodsBean> goodsList=new ArrayList<>();
    private GoodsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadmore_refresh);

        swipeToLoadLayout = ((SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout));
        mRecycleView = ((RecyclerView) findViewById(R.id.swipe_target));
        layoutManager = new LinearLayoutManager(this);//创建线性布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//垂直方向
        mRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置布局管理器
        mRecycleView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画

        setGoodsList();

        //设置Adapter
        adapter = new GoodsAdapter(goodsList,this);
        mRecycleView.setAdapter(adapter);

        swipeToLoadLayout.setOnRefreshListener(this);//下拉
        swipeToLoadLayout.setOnLoadMoreListener(this);//上拉
    }

    @Override
    public void onLoadMore() {



        new  Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for(int i=12;i<24 ;i++){
                    GoodsBean goods=new GoodsBean();
                    goods.setGoodsId(i);
                    goods.setGoodsPrice(50+i*3);
                    goods.setGoodsDescribe("这是第"+i+"件商品");
                    goods.setGoodsImgId("http://192.168.1.3:8080/image/"+i+".jpg");
                    goods.setGoodsPlace("杭州");
                    goods.setGoodsPostage(i+5+"");
                    goodsList.add(goods);

                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);
                }
            }
        }.start();



    }

    @Override
    public void onRefresh() {
        new  Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for(int i=12;i<24 ;i++){
                    GoodsBean goods=new GoodsBean();
                    goods.setGoodsId(i);
                    goods.setGoodsPrice(50+i*3);
                    goods.setGoodsDescribe("这是刷新出来的第"+i+"件商品");
                    goods.setGoodsImgId("http://192.168.1.3:8080/image/"+i+".jpg");
                    goods.setGoodsPlace("杭州");
                    goods.setGoodsPostage(i+5+"");
                    goodsList.add(goods);

                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        }.start();



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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1) {
                adapter.notifyDataSetChanged();
                swipeToLoadLayout.setRefreshing(false);

            }else if(msg.what==2){
                adapter.notifyDataSetChanged();
                swipeToLoadLayout.setLoadingMore(false);

            }
        }
    };
}
