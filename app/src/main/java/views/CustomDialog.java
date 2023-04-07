package views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import t.f.recyclerimage.R;
import utils.Property;

/**
 * Dialog 2016年7月30日
 */
public class CustomDialog extends Dialog {

    private int ids[];
    Context mContext;

    public interface OnClick{
        void onClick(Dialog dialog, View view);
    }
    private OnClick mOnClick;
    public void setOnClick(OnClick onClick) {
        mOnClick = onClick;
    }

    public CustomDialog(Context context,int[] ids) {
        super(context, R.style.quick_option_dialog);
        this.mContext=context;
        this.ids=ids;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog);

        if(ids!=null){
            for(int i=0;i<ids.length;i++){
                findViewById(ids[i]).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnClick.onClick(CustomDialog.this,v);
                    }
                });
            }
        }
    }
    
    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }
    
}