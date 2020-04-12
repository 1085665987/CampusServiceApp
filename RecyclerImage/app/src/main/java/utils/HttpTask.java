package utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Friday on 2018/7/15.
 */

public class HttpTask extends AsyncTask<String,Object,String> {
    private static final String TAG = "HTTPTASK";

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... objects) {
       try{
           URL mHttpURL = new URL(objects[0].toString());
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
                   return result;
               }
           }
       }catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        if (json != null && json != "") {
            Log.d(TAG, "taskSuccessful");
            int i1 = json.indexOf("["), i2 = json.indexOf("{"), i = i1 > -1 && i1 < i2 ? i1 : i2;
            if (i > -1) {
                json = json.substring(i);
                taskHandler.taskSuccessful(json);
            } else {
                Log.d(TAG, "taskFailed");
                taskHandler.taskFailed();
            }
        } else {
            Log.d(TAG, "taskFailed");
            taskHandler.taskFailed();
        }
    }

    public interface HttpTaskHandler {
        void taskSuccessful(String json);

        void taskFailed();
    }

    private HttpTaskHandler taskHandler;

    public void setTaskHandler(HttpTaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

}