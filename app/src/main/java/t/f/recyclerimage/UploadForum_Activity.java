package t.f.recyclerimage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Adapters.ImgSeleted_Adapter;
import infos.UserInfo;
import okhttp3.Call;
import utils.GlideLoader;
import utils.Property;
import utils.ScreenUtil;
import utils.StatusBarLightModeUtil;
import utils.UploadFiles;

/**
 * Created by Friday on 2018/7/28.
 */

public class UploadForum_Activity extends Activity {
    private ImageView back,ok;              //两个图片，用来显示 退出和发送
    private EditText content;               //帖子的正文
    private RecyclerView imgs;              //帖子的图片
    private TextView this_school,all_school;    //帖子的可见范围（本校或者全国）
    private boolean isAll=true;                      //是否全国可见

    private ImgSeleted_Adapter imgSeleted_adapter;  //帖子图片的适配器

    private ArrayList<String> imgUris;               //图片的资源路径

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_forum);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        initView();
        setOnClick();
    }

    private void initView(){
        back=(ImageView)findViewById(R.id.back);
        ok=(ImageView)findViewById(R.id.ok);
        content=(EditText)findViewById(R.id.forum_context);
        imgs=(RecyclerView)findViewById(R.id.recycler_images);
        float itemWidth=(ScreenUtil.getScreenWidth(this)-90)/3;
//        int sumItems= (int) (itemWidth*3.2);
//        ViewGroup.LayoutParams lp = imgs.getLayoutParams();
//        lp.height=sumItems;

        imgs.setLayoutManager(new GridLayoutManager(this,3));

        this_school=(TextView)findViewById(R.id.this_school);
        all_school=(TextView)findViewById(R.id.all_school);

        all_school.setClickable(false);

        imgUris=new ArrayList<>();
        imgUris.add("http://eggplant1-blog.stor.sinaapp.com/head_picture/h_m.jpg");
//        imgUris.add("http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffceee78e5eb5014a90f703ea09.jpg");
        imgSeleted_adapter=new ImgSeleted_Adapter(this,imgUris);

        imgs.setAdapter(imgSeleted_adapter);
    }
    private void setOnClick(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (content.getText().toString().trim().length() == 0)
                    if (content.getText().toString().trim().length() == 0 && imgUris.size() == 1) {
                        Toast.makeText(UploadForum_Activity.this, "你在逗我？\n哼！不给你上传", Toast.LENGTH_SHORT).show();
                        return;
                    }
                if (imgUris.size() == 1)
                    if (content.getText().toString().trim().length() == 0 && imgUris.size() == 1) {
                        Toast.makeText(UploadForum_Activity.this, "你在逗我？\n哼！不给你上传", Toast.LENGTH_SHORT).show();
                        return;
                    }
                String url=getResources().getString(R.string.forum_upload);
                HashMap<String,String> params=new HashMap<>();
                params.put("phone",String.valueOf(new UserInfo(UploadForum_Activity.this).getLongInfo(Property.NO)));   //账号
                params.put("content",content.getText().toString().trim());                                                      //帖子正文
                if(!isAll)                                                                                                        //帖子可见范围
                    params.put("fanwei",String.valueOf(1));
                else{
                    params.put("fanwei",String.valueOf(0));
                }
                if(imgUris.size()==1)
                    imgUris.clear();
                else{
                    imgUris.remove(imgUris.size()-1);
                }
                UploadFiles.upLoadToServer(UploadForum_Activity.this,url,params,imgUris);
            }
        });

        imgSeleted_adapter.setItemClickListener(new ImgSeleted_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ImageConfig imageConfig = new ImageConfig.Builder(UploadForum_Activity.this , new GlideLoader())

                        // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                        //.steepToolBarColor(getResources().getColor(R.color.blue))
                        // 标题的背景颜色 （默认黑色）
                        //.titleBgColor(getResources().getColor(R.color.blue))
                        // 提交按钮字体的颜色 （默认白色）
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色 （默认白色）
                        .titleTextColor(getResources().getColor(R.color.white))
                        // 开启多选 （默认为多选） (单选 为 singleSelect)
                        .mutiSelect()
                        // 多选时的最大数量 （默认 9 张）
                        .mutiSelectMaxSize(9)
                        // 已选择的图片路径
                        .pathList(imgUris)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                        .filePath("/Xiaoyuanxing/Pictures")
                        // 开启拍照功能 （默认关闭）
                        .showCamera()
                        .build();
                if(imgUris.size()==1){
                    imgUris.clear();
                    ImageSelector.open(imageConfig); // 开启图片选择器
                    return;
                }
                if(imgUris.size()-1==position&&imgUris.size()<9){
                    imgUris.remove(imgUris.size()-1);
                    ImageSelector.open(imageConfig); // 开启图片选择器
                }else{
                    Toast.makeText(UploadForum_Activity.this,"加载"+imgUris.get(position),Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all_school.setClickable(true);
                this_school.setClickable(false);

                this_school.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                this_school.setTextColor(getResources().getColor(R.color.white));

                all_school.setTextColor(getResources().getColor(R.color.no_selected_color));
                all_school.setBackgroundColor(getResources().getColor(R.color.white));

                isAll=false;
            }
        });
        all_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all_school.setClickable(false);
                this_school.setClickable(true);

                all_school.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                all_school.setTextColor(getResources().getColor(R.color.white));

                this_school.setTextColor(getResources().getColor(R.color.no_selected_color));
                this_school.setBackgroundColor(getResources().getColor(R.color.white));

                isAll=true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            ArrayList<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            imgUris.clear();
            for (String path : pathList) {
                imgUris.add(path);
            }
            imgUris.add("http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffceee78e5eb5014a90f703ea09.jpg");
            if(imgUris.size()==10){
                imgUris.remove(imgUris.size()-1);
            }
            imgSeleted_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if(imgUris.size()>3){
                imgs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //设置recyclerView高度
                        ViewGroup.LayoutParams layoutParams = imgs.getLayoutParams();
                        if (Build.VERSION.SDK_INT >= 16) {
                            imgs.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            imgs.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        int height = wm.getDefaultDisplay().getWidth() / 2;
                        if (imgs.getHeight() < height && imgs.getHeight() > wm.getDefaultDisplay().getWidth() / 3) {
                            layoutParams.height = imgs.getHeight();
                        } else {
                            layoutParams.height = height;
                        }
                        imgs.setLayoutParams(layoutParams);
                    }
                });
            }
        }
    }
}
