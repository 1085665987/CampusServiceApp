package views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import t.f.recyclerimage.R;

/**
 * Created by Friday on 2018/7/17.
 */

public class LoadMoreView extends LinearLayout implements SwipeLoadMoreTrigger,SwipeTrigger {

    private Context context;
    private TextView textView;
    private ProgressBar progressBar;
    private boolean isRelease;

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
    }

    @Override
    public void onPrepare() {
        isRelease = false;
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
            // 当上拉到一定位置之后 显示刷新
        if (!b){
            if (i<getHeight()) {
                textView.setText("上拉加载");
            }else {
                textView.setText("松开加载更多");
            }
        }
    }

    @Override
    public void onRelease() {
        isRelease = true;
    }

    @Override
    public void onComplete() {
        textView.setText("加载完成");
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onReset() {

    }


    private void initView(){
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pullrefrefh_recyclerview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        progressBar = (ProgressBar)moreView.findViewById(R.id.pullrefrefh_footer_ProgressBar);
        textView = (TextView) moreView.findViewById(R.id.pullrefrefh_footer_hint_TextView);

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoadMore() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("正在加载");
    }
}
