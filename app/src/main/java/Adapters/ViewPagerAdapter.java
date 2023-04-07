package Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import utils.ZoomTutorial;

public class ViewPagerAdapter extends PagerAdapter {

    private String[] sDrawables;
    private Context mContext;
    private ZoomTutorial mZoomTutorial; 
    
    public ViewPagerAdapter(Context context , String[] imgIds, ZoomTutorial zoomTutorial) {
        this.sDrawables = imgIds;
        this.mContext = context;
        this.mZoomTutorial = zoomTutorial;
    }

    @Override
    public int getCount() {
        return sDrawables.length;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        final ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(sDrawables[position]);
        Glide.with(mContext).load(sDrawables[position]).into(imageView);
        container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                mZoomTutorial.closeZoomAnim(position);
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}