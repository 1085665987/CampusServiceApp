package t.f.recyclerimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.fragments.Main_Activity;
import utils.GlideCircleTransform;
import utils.KeyboundUtil;
import utils.LimitEdit;
import utils.Property;
import utils.StatusBarLightModeUtil;
import views.VerifyCodeView;

/**
 * Created by Friday on 2018/7/10.
 */

public class Msg_Login_Activity extends Activity {
    public static String TAG="Msg_Login_Activity";

    private VerifyCodeView verifyCodeView;
    private EditText editText;
    private EditText txtphone;
    private ImageView image;
    private KeyboundUtil phoneKeyboard,YZMKeyboard;
    private TextView getYZM,changeLoginWay;
    private  String YZM=null,isFirst=null;                  //得到的验证码  与   是否是第一次登录（是：“Y”）
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        initViews();
    }
    private void initViews(){
        image=(ImageView)findViewById(R.id.touxiang_image);

        verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);

        //当验证码输入框输入完成时Do
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
//                Toast.makeText(Msg_Login_Activity.this, "inputComplete: " + verifyCodeView.getEditContent(), Toast.LENGTH_SHORT).show();
                if(YZM==null){
                    return;
                }
                if(YZM.equals(verifyCodeView.getEditContent().toString().trim())){
                    Log.i(TAG,"是否为第一次登录："+isFirst);
                    if(isFirst.equals("Y")){
                        //存下用户的手机号
                        UserInfo info=new UserInfo(Msg_Login_Activity.this);
                        info.setUserInfo(Property.NO,Long.parseLong(txtphone.getText().toString().trim()));

                        //跳转选择学校界面（第一次登录时要先选择学校）
//                        Toast.makeText(Msg_Login_Activity.this, "跳转选择学校界面（第一次登录时要先选择学校）", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Msg_Login_Activity.this,Choose_School_Activity.class);
                        startActivity(intent);
                        finish();
                    }else{

                        //存下用户的手机号
                        UserInfo info=new UserInfo(Msg_Login_Activity.this);
                        info.setUserInfo(Property.NO,Long.parseLong(txtphone.getText().toString().trim()));

                        //修改数据库登录状态
                        String update_login_state=getResources().getString(R.string.update_login_state_url)+"?phone="+txtphone.getText().toString();
                        OkHttpUtils.get().url(update_login_state).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                            }
                            @Override
                            public void onResponse(String response, int id) {}
                        });

                        //跳转主界面
                        Intent intent=new Intent();
                        intent.setClass(Msg_Login_Activity.this, Main_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(Msg_Login_Activity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void invalidContent() {
                
            }
        });
        editText=verifyCodeView.getEditText();
        //初始化自定义键盘
        YZMKeyboard=new KeyboundUtil(this,editText,R.id.keyboardview_phone,6);

        txtphone=(EditText) findViewById(R.id.id);

        //当手机号输入框输入11位完成时Do
        LimitEdit limitEdit=new LimitEdit(txtphone,11);
        limitEdit.setOnClickAfterTextChanged(new LimitEdit.OnClickAfterTextChanged() {
            @Override
            public void onLimitCount() {
                getYZM.setTextColor(getResources().getColor(R.color.cursor_color));
                getYZM.setClickable(true);

                Log.i(TAG,getResources().getString(R.string.image_url)+"?phone="+txtphone.getText().toString());
                OkHttpUtils.get().url(getResources().getString(R.string.image_url)+"?phone="+txtphone.getText().toString()).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(Msg_Login_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if(response.equals("0".trim())){
                                return;
                            }
                            String imageUrl =getResources().getString(R.string.base_image_url)+response;
                            Log.i(TAG,imageUrl);
                            Glide.with(Msg_Login_Activity.this).load(imageUrl).transform(new GlideCircleTransform(Msg_Login_Activity.this)).into(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void passLimitCount(int editStart,int editEnd,Editable s) {
                Log.i(TAG, "你输入的字数已经超过了限制！");
                int tempSelection = editStart;
                txtphone.setText(s);
                txtphone.setSelection(tempSelection);
            }
        });
        txtphone.addTextChangedListener(limitEdit);
        phoneKeyboard = new KeyboundUtil(this,txtphone,R.id.keyboardview_YZM,11);

        //隐藏手机软键盘
        HideKeyboard(editText);
        HideKeyboard(txtphone);

        getYZM=(TextView)findViewById(R.id.getYZM);
        //给“得到验证码”加监听
        getYZM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.requestFocusFromTouch();
                getYZM.setTextColor(getResources().getColor(R.color.no_selected_color));
                getYZM.setClickable(false);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message message=null;

                        //设置60s之内不可再点击，文本颜色变化
                        for(int i=60;i>0;i--){
                            message=new Message();
                            message.what=1;
                            message.arg1=i;
                            handler.sendMessage(message);
                            try {
                                Thread.sleep(1000);
                            }catch (Exception e){e.printStackTrace();}
                        }
                        message=new Message();
                        message.what=2;//终止循环
                        handler.sendMessage(message);
                    }
                }.start();

                //获得登录验证码并且去验证“验证码”是否正确，如果正确，登录；如果不正确就返回
                String get_login_YZM_url=getResources().getString(R.string.get_login_YZM)+"?phone="+txtphone.getText().toString().trim();
                OkHttpUtils.get().url(get_login_YZM_url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(Msg_Login_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            isFirst=jsonObject.getString("first");
                            YZM=jsonObject.getString("mes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        changeLoginWay=(TextView)findViewById(R.id.change_login_ways);
        changeLoginWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Msg_Login_Activity.this,Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(phoneKeyboard.isKetboardVisiable()==0){
            phoneKeyboard.setVisiable(false);
            return;
        }
        if(YZMKeyboard.isKetboardVisiable()==0){
            YZMKeyboard.setVisiable(false);
            return;
        }
        finish();
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                getYZM.setText("重发验证码\t"+msg.arg1);
            }else if(msg.what==2){
                getYZM.setText("重发验证码");
                getYZM.setTextColor(getResources().getColor(R.color.cursor_color));
                getYZM.setClickable(true);
            }
        }
    };
}
