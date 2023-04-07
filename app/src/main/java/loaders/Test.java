package loaders;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import t.f.recyclerimage.R;
import utils.HttpTask;

/**
 * Created by Friday on 2018/7/15.
 */

public class Test extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mime);
        HttpTask httpTask=new HttpTask();
        httpTask.execute("http://192.168.0.105:8081/MyProject/Login?id=4&pass=123456");
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                Toast.makeText(Test.this,json,Toast.LENGTH_LONG).show();
                Log.i("Test:",json);
            }

            @Override
            public void taskFailed() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
