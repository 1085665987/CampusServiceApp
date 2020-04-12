package views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import t.f.recyclerimage.R;

/**

 * Created by 刘龙 on 2017/8/9.

 * 公司:北京华星成汇文化发展有限公司

 * 描述:

 */

 

public class RecyclerViewFooter extends LinearLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context context;
    private View contentView;
    private ProgressBar progressBar;
    private TextView hintView;

    public RecyclerViewFooter(Context context) {
        this(context, null);
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pullrefrefh_recyclerview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        contentView = moreView.findViewById(R.id.pullrefrefh_footer_content);
        progressBar = (ProgressBar)moreView.findViewById(R.id.pullrefrefh_footer_ProgressBar);
        hintView = (TextView) moreView.findViewById(R.id.pullrefrefh_footer_hint_TextView);
    }

    /**
     * 设置状态
     * @param state
     */
    public void setState(int state) {
        hintView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        hintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            hintView.setVisibility(View.VISIBLE);
            hintView.setText("松开载入更多");
        } else if (state == STATE_LOADING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            hintView.setVisibility(View.VISIBLE);
            hintView.setText("查看更多");
        }
    }

    /**
     * 设置距离下边的BottomMargin
     * @param height
     */
    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.bottomMargin = height;
        contentView.setLayoutParams(lp);
    }

    /**
     * 获取BottomMargin
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.height = 0;
        contentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        contentView.setLayoutParams(lp);
    }
}
