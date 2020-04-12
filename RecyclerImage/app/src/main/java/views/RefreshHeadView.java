package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import t.f.recyclerimage.R;

/**
 * Created by Friday on 2018/7/17.
 */

public class RefreshHeadView extends LinearLayout implements SwipeRefreshTrigger,SwipeTrigger {

    private Context context;

    /**
     * 动画执行时间
     */
    private final int ROTATE_ANIM_DURATION = 60;
    private boolean isRelease;

    private ImageView img;
    private ProgressBar progressBar;
    private TextView textView,title;

    private LinearLayout mContainer;
    private RotateAnimation mRotateDownAnim;


    private void initView(){
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pullrefrefh_recyclerview_header,null);
        //添加到改容器
        addView(mContainer);
        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

//        mRealityContent = (RelativeLayout) mContainer.findViewById(R.id.pullRefresh_reality_content);
        img=(ImageView) mContainer.findViewById(R.id.pullRefresh_arrow);
        textView= (TextView) mContainer.findViewById(R.id.pullRefresh_text);
        progressBar=(ProgressBar) findViewById(R.id.pullRefresh_progressbar);
        title=(TextView) mContainer.findViewById(R.id.pullRefresh_title);

        //初始化动画
        mRotateDownAnim = new RotateAnimation(360.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        mRotateDownAnim.setDuration(1000);
        mRotateDownAnim.setFillAfter(true);
        mRotateDownAnim.setRepeatMode(Animation.RESTART);
        mRotateDownAnim.setRepeatCount(Animation.INFINITE);
        progressBar.setVisibility(GONE);
    }


    public RefreshHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        initView();
    }

    @Override
    public void onRefresh() {
        mRotateDownAnim.setDuration(1000);
        progressBar.setVisibility(GONE);
        img.startAnimation(mRotateDownAnim);
        title.setText("正在加载");
    }

    @Override
    public void onPrepare() {

        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        isRelease=false;

        title.setVisibility(VISIBLE);
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        if (!b) {
            if (i < getHeight()) {//Y轴与下拉控件
                title.setText("下拉刷新");
            } else {
                title.setText("松开刷新更多");
            }
        }
    }

    @Override
    public void onRelease() {
        isRelease = true;
    }

    @Override
    public void onComplete() {
        img.clearAnimation();
        title.setText("加载完成");
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onReset() {

    }


    /**
     * 设置显示的图片
     * @param imagePath
     */
    public void setPullImage(String imagePath) {
        Drawable fromPath = Drawable.createFromPath(imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        img.setBackground(fromPath);
        img.setImageBitmap(bitmap);
    }
    /**
     * 设置显示的文字
     * @param text
     */
    public void setPullContent(String text) {
        textView.setText(text);
    }
}
