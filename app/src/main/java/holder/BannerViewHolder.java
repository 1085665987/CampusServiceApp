package holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import t.f.recyclerimage.Login_Activity;
import t.f.recyclerimage.R;
import views.MZBannerView;

/**
 * Created by Friday on 2018/7/14.
 */

public class BannerViewHolder implements MZViewHolder<String> {
    private ImageView mImageView;
    /**
     * 创建View
     *
     * @param context
     * @return
     */
    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item,null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return view;
    }

    /**
     * 绑定数据
     *
     * @param context
     * @param position
     * @param data
     */
    @Override
    public void onBind(Context context, int position, String data) {
        Glide.with(context).load(data).into(mImageView);
    }
}
