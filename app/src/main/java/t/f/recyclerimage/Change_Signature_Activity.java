package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import infos.UserInfo;
import okhttp3.Call;
import utils.Property;
import utils.StatusBarLightModeUtil;

import static t.f.recyclerimage.Change_Data_Activity.CHANGENAME_EXTRA_KEY;
import static t.f.recyclerimage.Change_Data_Activity.CHANGESIGNATURE_EXTRA_KEY;

public class Change_Signature_Activity extends Activity {
    private EditText editText;

    private ImageView back;

    private Button ok;

    private ProgressBar progressBar;
    private LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_signature);

        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);

        editText=(EditText)findViewById(R.id.edit);
        back=(ImageView)findViewById(R.id.cancer);
        ok=(Button)findViewById(R.id.ok);
        layout=(LinearLayout)findViewById(R.id.progressBar_parent);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ok.setClickable(true);
                ok.setBackgroundColor(getResources().getColor(R.color.cursor_color));

                if (editText.getText().toString().trim().length()==0){
                    ok.setClickable(false);
                    ok.setBackgroundColor(getResources().getColor(R.color.no_selected_color));
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.VISIBLE);

                String url=getResources().getString(R.string.change_nickname_url);
                url=url+"?phone="+new UserInfo(Change_Signature_Activity.this).getLongInfo(Property.NO)+
                        "&user_name="+editText.getText().toString().trim();
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                handler.sendEmptyMessage(-1);
                            }
                        }.start();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        layout.setVisibility(View.GONE);
                        if(response.trim().equals("ok")){
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(1);
                                }
                            }.start();
                        }else{
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            }.start();
                        }
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change_Signature_Activity.this.finish();
            }
        });

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==-1){
                Toast.makeText(Change_Signature_Activity.this,"服务器失效了",Toast.LENGTH_SHORT).show();
            }
            if (msg.what==0){
                Toast.makeText(Change_Signature_Activity.this,"请求超时",Toast.LENGTH_SHORT).show();
            }
            if (msg.what==1){
                Toast.makeText(Change_Signature_Activity.this,"修改成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra(CHANGESIGNATURE_EXTRA_KEY,editText.getText().toString().trim());
                setResult(RESULT_OK,intent);
                Change_Signature_Activity.this.finish();
            }
            layout.setVisibility(View.GONE);
        }
    };

}
