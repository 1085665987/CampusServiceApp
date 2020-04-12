package t.f.recyclerimage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import t.f.recyclerimage.image_fragments.PhotoViewAttacher;
import utils.StatusBarLightModeUtil;

/**
 * Created by Friday on 2018/8/12.
 */

public class PhotoView_Activity extends Activity {
    private PhotoView photoView;

    public static final String EXTRA_IMAGE_URL = "image_url";

    private String image_url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.out_actitity, R.anim.in_actitity);// 淡出淡入动画效果
        setContentView(R.layout.one_image);
        StatusBarLightModeUtil.FlymeSetStatusBarBlackMode(getWindow(),true);
        photoView=(PhotoView)findViewById(R.id.iv_photo);

        image_url=getIntent().getStringExtra(EXTRA_IMAGE_URL);

        Glide.with(this).load(image_url).into(photoView);

        //加载本地图片缩放处理
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               setOnLongClick(PhotoView_Activity.this);
                return false;
            }
        });
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
               PhotoView_Activity. this.finish();
            }
        });
        photoView.setEnabled(true);
    }
    //长按图片是  弹出底部功能栏
    private void setOnLongClick(final Context context){
        final Dialog mCameraDialog = new Dialog(context, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bottom_menu, null);
        root.findViewById(R.id.share_by_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现QQ分享
                Toast.makeText(context.getApplicationContext(),"QQ分享",Toast.LENGTH_SHORT).show();
            }
        });
        root.findViewById(R.id.share_by_wecahr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现微信分享
                Toast.makeText(context.getApplicationContext(),"微信分享",Toast.LENGTH_SHORT).show();
            }
        });
        root.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现保存到本地
                Toast.makeText(context.getApplicationContext(),"保存到本地",Toast.LENGTH_SHORT).show();
            }
        });
        root.findViewById(R.id.cencel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraDialog.cancel();
            }
        });
        mCameraDialog.setContentView(root);

        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

}
