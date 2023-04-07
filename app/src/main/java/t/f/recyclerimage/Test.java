package t.f.recyclerimage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import holder.MZHolderCreator;
import holder.MZViewHolder;
import utils.DataEntry;
import views.MZBannerView;

/**
 * Created by Friday on 2018/7/12.
 */

public class Test extends Activity {
    private MZBannerView mMZBanner;
    int []RES = new int[]{R.mipmap.image2,R.mipmap.image3,R.mipmap.image4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mMZBanner = (MZBannerView)findViewById(R.id.banner);

        // 设置数据
        List<Integer> list = new ArrayList<>();

        for(int i=0;i<RES.length;i++){

            list.add(RES[i]);

        }
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }
    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.pager_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }
    private List<DataEntry> mockData(){
        List<DataEntry> list = new ArrayList<>();
        DataEntry dataEntry = null;

        for(int i=0;i<RES.length;i++){
            dataEntry = new DataEntry();
            dataEntry.resId = RES[i];
            dataEntry.desc = "The time you enjoy wasting , is not wasted.";
            list.add(dataEntry);
        }
        return list;
    }
}
