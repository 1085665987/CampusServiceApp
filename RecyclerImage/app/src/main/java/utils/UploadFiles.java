package utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Friday on 2018/7/29.
 */

public class UploadFiles {
    public static String TAG="UploadFiles";

    public static void upLoadToServer(final Context context, final String url, final Map<String, String> params, final ArrayList<String> list) {
        if (NetUtils.netIsConnected(context)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Disposition", "form-data;filename=enctype");
            PostFormBuilder builder = OkHttpUtils.post();
            builder.url(url);
            if(list!=null) {
                 builder .headers(headers);
                for (int i = 0; i < list.size(); i++) {
                    File file = new File(list.get(i));
                    if (!file.exists()) {
//                    MyToast.showMessage("文件不存在，请修改文件路径");
                        return;
                    }
                    String filename = file.getName();
                    builder.addFile("mFile" + i, filename, file);
                }
            }

            builder.params(params)
//                    .headers(headers)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i(TAG,response);
                            if (response.trim().equals("1")){
                                if (context instanceof Activity){
                                    ((Activity) context).finish();
                                }
                            }
                        }
                    });
        }
    }
    public static void upLoadFileToServer(final Context context, final String url, final ArrayList<File> list) {
        if (NetUtils.netIsConnected(context)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Disposition", "form-data;filename=enctype");
            PostFormBuilder builder = OkHttpUtils.post();
            builder.url(url);
            if (list != null) {
                builder.headers(headers);
                for (int i = 0; i < list.size(); i++) {
                    String filename = "head_image";
                    builder.addFile("mFile" + i, filename, list.get(i));
                }
            }

            builder.build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i(TAG, response);
                        }
                    });
        }
    }
}
