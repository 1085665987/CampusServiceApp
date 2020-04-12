package t.f.recyclerimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import JavaBeans.UserBean;
import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.fragments.Main_Activity;
import utils.DownloadUtil;
import utils.GlideCircleTransform;
import utils.Property;
import utils.StatusBarLightModeUtil;


/**
 * Created by Friday on 2018/7/9.
 */

public class Login_Activity extends Activity {

    public static String TAG="Login_Activity";
    private EditText txtNo;
    private EditText txtpass;
    private Button btnLogin;
    private UserBean user;
    private TextView changeLoginway;
    private ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }

    private void init() {
        StatusBarLightModeUtil.MIUISetStatusBarLightMode(this,true);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);

        txtNo=(EditText)findViewById(R.id.id);
        txtpass=(EditText)findViewById(R.id.password);
        txtNo.addTextChangedListener(mTextWatcher);

        image=(ImageView)findViewById(R.id.touxiang_image);
        btnLogin=(Button)findViewById(R.id.login);

        changeLoginway=(TextView)findViewById(R.id.change_login_ways);

        changeLoginway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Activity.this,Msg_Login_Activity.class);

                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String no=txtNo.getText().toString();
                final String pass=txtpass.getText().toString();
                if(no.length()==0){
                    Toast.makeText(Login_Activity.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()==0){
                    Toast.makeText(Login_Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
//                String url="http://192.168.1.164:8081/MyProject/Login";
                String url=getResources().getString(R.string.login_url)+"?phone="+no+"&password="+pass;
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(Login_Activity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        int code=processData(response);
                        if(code==2){
                            Intent intent=new Intent(Login_Activity.this, Main_Activity.class);
                            Login_Activity.this.startActivity(intent);
                            finish();
                        }else if(code==1){
                            Toast.makeText(Login_Activity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login_Activity.this,"没有此账号",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private int processData(String response){
        Log.i("Login_Activity:JSON数据",response);
            if(response.trim().equals("2")){
                user=new UserBean();
                user.setNo(Long.parseLong(txtNo.getText().toString().trim()));

                UserInfo userInfo=new UserInfo(Login_Activity.this);
                userInfo.setUserInfo(Property.NO,user.getNo());
                return 2;
            }else if(response.trim().equals("0".trim())){
                return 0;
            }else {
                return 1;
            }
    }
    private TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            // TODO Auto-generated method stub
            //mTextView.setText(s);//将输入的内容实时显示
        }
        @Override
        public void afterTextChanged(Editable s) {
            editStart = txtNo.getSelectionStart();
            editEnd = txtNo.getSelectionEnd();
            if (temp.length() > 11) {
                Log.i(TAG, editStart+"\t"+editEnd);
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                txtNo.setText(s);
                txtNo.setSelection(tempSelection);
            }
            if(temp.length()==11){
                Log.i(TAG,getResources().getString(R.string.image_url)+"?phone="+txtNo.getText().toString());
                OkHttpUtils.get().url(getResources().getString(R.string.image_url)+"?phone="+txtNo.getText().toString()).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(Login_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if(response.equals("0".trim())){
                                return;
                            }
                            String imageUrl =getResources().getString(R.string.base_image_url)+response;
                            Log.i(TAG,imageUrl);
                            Glide.with(Login_Activity.this).load(imageUrl).transform(new GlideCircleTransform(Login_Activity.this)).into(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };
}
