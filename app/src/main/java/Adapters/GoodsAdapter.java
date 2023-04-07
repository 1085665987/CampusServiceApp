package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;

import JavaBeans.GoodsBean;
import t.f.recyclerimage.R;
import utils.GlideRoundTransform;
import utils.ScreenUtil;

/**
 * Created by Friday on 2018/7/7.
 */

public class GoodsAdapter extends RecyclerView.Adapter {

//    protected ImageLoader imageLoader = ImageLoader.getInstance();
//    protected DisplayImageOptions options;

    private Context context;
    private List<GoodsBean> goodsList;
    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView goodsImg;
        TextView goodsDesc;
        TextView goodsPostage;
        TextView goodsPrice;
        TextView goodsPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            goodsImg=(ImageView) itemView.findViewById(R.id.goods_img);
            goodsDesc=(TextView)itemView.findViewById(R.id.goods_desc);
            goodsPlace=(TextView)itemView.findViewById(R.id.goods_place);
            goodsPostage=(TextView)itemView.findViewById(R.id.goods_postage);
            goodsPrice=(TextView)itemView.findViewById(R.id.goods_price);
        }
    }
    public GoodsAdapter(List<GoodsBean> list, Context context){
        this.goodsList=list;
        this.context=context;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_launcher_background)
//                .showImageForEmptyUri(R.drawable.ic_launcher_background)
//                .showImageOnFail(R.drawable.ic_launcher_background).cacheInMemory(true)
//                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//        //初始化ImageLoader
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//        ImageLoader.getInstance().init(config.build());
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsBean goods=goodsList.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).goodsDesc.setText(goods.getGoodsDescribe());
            ((ViewHolder) holder).goodsPlace.setText(goods.getGoodsPlace());
            ((ViewHolder) holder).goodsPostage.setText(goods.getGoodsPostage());
            ((ViewHolder) holder).goodsPrice.setText(goods.getGoodsPrice()+"");
            //imageLoader.displayImage(goods.getGoodsImgId(),((ViewHolder) holder).goodsImg, options);

            ViewGroup.LayoutParams layoutParams=  ((ViewHolder) holder).goodsImg.getLayoutParams();
            float itemWidth=(ScreenUtil.getScreenWidth(context)-80)/2;
            layoutParams.width=(int)itemWidth;
            layoutParams.height=700;
            ((ViewHolder) holder).goodsImg.setLayoutParams(layoutParams);
            Glide.with(context).load(R.mipmap.image0)
                    .transform(new CenterCrop(context), new GlideRoundTransform(context,10))
                    .override(layoutParams.width,layoutParams.height)
                    .into(((ViewHolder) holder).goodsImg);
        }
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }
}
