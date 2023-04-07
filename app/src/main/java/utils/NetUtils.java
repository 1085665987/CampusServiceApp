package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Friday on 2018/7/29.
 */

public class NetUtils {

    public static boolean netIsConnected(Context context){
        boolean CONNECTION_STATE=false;
        ConnectivityManager connmanager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connmanager.getActiveNetworkInfo();
        if(networkinfo!=null){
            if(networkinfo.isConnected()){
                if(networkinfo.getType()==ConnectivityManager.TYPE_MOBILE){
                    CONNECTION_STATE=true;
                    Toast.makeText(context, "已经连接数据网络", Toast.LENGTH_LONG).show();
                }else if(networkinfo.getType()==ConnectivityManager.TYPE_WIFI){
                    CONNECTION_STATE=true;
                    Toast.makeText(context, "已经连接Wi-Fi网络", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            CONNECTION_STATE=false;
            Toast.makeText(context, "无网络连接,请检查网络", Toast.LENGTH_LONG).show();
        }
        return CONNECTION_STATE;
    }
}
