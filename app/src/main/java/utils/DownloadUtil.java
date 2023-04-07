package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Friday on 2018/7/9.
 */

public class DownloadUtil {

    public static String getImageUrl(String url, HashMap<String ,String >params, final Context context) {
        final String[] imageUrl = new String[1];
        Log.i("DownloadUtil:getImageUrl",url+"?id="+params.get("id"));
        OkHttpUtils.get().url(url+"?id="+params.get("id")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context,"服务器错了",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    imageUrl[0] =jsonObject.getString("image_url");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Log.i("DownloadUtil:getImageUrl",imageUrl[0]);
        return imageUrl[0];
    }
}
