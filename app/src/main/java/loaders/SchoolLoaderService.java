package loaders;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import JavaBeans.SchoolBean;
import okhttp3.Call;
import t.f.recyclerimage.R;
import utils.Property;

/**
 * Created by Friday on 2018/7/14.
 */

public class SchoolLoaderService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SchoolLoaderBinder();
    }
    public class SchoolLoaderBinder extends Binder {
        public String TAG="SchoolLoader";
        private  SchoolBean schoolBean;

        public void loadSchool(int schoolId){
            try {
                String url = SchoolLoaderService.this.getResources().getString(R.string.school_url) + "?" + Property.SCHOOL_ID + "=" + schoolId;
                URL mHttpURL = new URL(url);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mHttpURL.openConnection();
                mHttpURLConnection.setConnectTimeout(5000);
                mHttpURLConnection.setReadTimeout(5000);
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.setDoOutput(false);
                mHttpURLConnection.setRequestProperty("Charset", "utf-8");
                mHttpURLConnection.setUseCaches(false);
                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.connect();
                int responseCopde = mHttpURLConnection.getResponseCode();
                if (responseCopde == 200) {
                    InputStream in = mHttpURLConnection.getInputStream();
                    byte buffer[] = new byte[1024];
                    int len = in.read(buffer, 0, 1024);
                    if (len != -1) {
                        String result = new String(buffer, 0, len);
                        schoolBean=new SchoolBean();
                        List<String> list=null;
                        JSONObject jsonObject=new JSONObject(result);
                        schoolBean.setSchoolId(jsonObject.getInt(Property.SCHOOL_ID));
                        schoolBean.setSchoolName(jsonObject.getString(Property.SCHOOL_NAME));
                        schoolBean.setSchoolXiaohui(jsonObject.getString(Property.SCHOOL_XIAOHUI));
                        JSONArray pictures=jsonObject.getJSONArray(Property.SCHOOL_PICTURES);
                        if(pictures.length()>0) {
                            list = new ArrayList<>();
                            for (int i = 0; i < pictures.length(); i++) {
                                list.add(pictures.getString(i));
                            }
                            schoolBean.setSchoolPicture(list);
                        }
                        Message message=new Message();
                        message.obj=schoolBean;
                        message.what=0x0001;
                        mHandler.sendMessage(message);
                    }
                }
            }catch (Exception e){e.printStackTrace();}
        }
        private Handler mHandler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x0001){

                }
            }
        };
        public SchoolBean getSchool(SchoolBean schoolBean){
            this.schoolBean=schoolBean;
            return this.schoolBean;
        }

        public SchoolBean getSchool(int schoolId){
            String url=SchoolLoaderService.this.getResources().getString(R.string.school_url)+"?"+Property.SCHOOL_ID+"="+schoolId;
            Log.d(TAG,url);
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(String response, int id) {
                    schoolBean=new SchoolBean();
                    List<String> list;
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        schoolBean.setSchoolId(jsonObject.getInt(Property.SCHOOL_ID));
                        schoolBean.setSchoolName(jsonObject.getString(Property.SCHOOL_NAME));
                        schoolBean.setSchoolXiaohui(jsonObject.getString(Property.SCHOOL_XIAOHUI));
                        JSONArray pictures=jsonObject.getJSONArray(Property.SCHOOL_PICTURES);
                        if(pictures.length()>0) {
                            list = new ArrayList<>();
                            for (int i = 0; i < pictures.length(); i++) {
                                list.add(pictures.getString(i));
                            }
                            schoolBean.setSchoolPicture(list);
                        }
                    }catch (Exception e){e.printStackTrace();}
                }
            });
            return schoolBean;
        }
    }
}
