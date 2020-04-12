package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.fragments.Main_Activity;
import utils.GlideCircleTransform;
import utils.Property;
import utils.StatusBarLightModeUtil;

/**
 * Created by Friday on 2018/7/27.
 */

public class Start_Activity extends Activity {
    private TextView textView;
    private ImageView imageView;
    public static String TAG="Start_Activity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        StatusBarLightModeUtil.MIUISetStatusBarLightMode(this,true);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        imageView=(ImageView) findViewById(R.id.start_img);
        textView=(TextView)findViewById(R.id.time);

        Glide.with(Start_Activity.this).load(R.mipmap.start_1).into(imageView);

        long phone=-1;
        try {
            phone = new UserInfo(this).getLongInfo(Property.NO);
        }catch (Exception e){
            e.printStackTrace();
        }

        String url=getResources().getString(R.string.start_url)+"?phone="+phone;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i(TAG,"请求失败");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);
                if(response.trim().equals("0")){
                    //重新登录
                    StartThread startThread=new StartThread(0);
                    startThread.start();
                }
                if(response.trim().equals("1")){
                    //直接跳转主界面
                    StartThread startThread=new StartThread(1);
                    startThread.start();
                }
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent=new Intent();
            if (msg.what == 0) {
                //登录
                intent.setClass(Start_Activity.this,Msg_Login_Activity.class);
            }else{
                //msg.what==1
                //跳转主页面
                intent.setClass(Start_Activity.this,Main_Activity.class);
            }
            startActivity(intent);
            finish();
        }
    };
    private class StartThread extends Thread implements Runnable{
        private int msg_code;

        public StartThread(int msg_code){
            this.msg_code=msg_code;
        }

        private Message message=new Message();
        @Override
        public void run() {
            super.run();
            message.what=this.msg_code;
            for(int i=0;i<3;i++){
                try {
                    message.arg1=i;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendMessage(message);
        }
    };
}
