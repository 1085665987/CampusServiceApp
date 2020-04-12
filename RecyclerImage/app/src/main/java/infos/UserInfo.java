package infos;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import JavaBeans.UserBean;

public class UserInfo {

     private String USER_INFO = "userInfo";
     private SharedPreferences sp;
     private Context context;
     private SharedPreferences.Editor editor;

     public UserInfo() {
     }

     public UserInfo(Context context) {

         this.context = context;
         this.sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
         this.editor=sp.edit();
     }
     // 存放字符串型的值
     public void setUserInfo(String key, String value) {
         editor.remove(key);
         editor.putString(key, value);
         editor.commit();
     }

    // 存放多组字符串型的值
    public void setUserInfo(HashMap<String,String> params,String...keys) {
         for(int i=0;i<keys.length;i++){
             editor.remove(keys[i]);
             editor.putString(keys[i],params.get(keys[i]));
         }
        editor.commit();
    }

     // 存放整形的值
     public void setUserInfo(String key, int value) {
         editor.remove(key);
         editor.putInt(key, value);
         editor.commit();
    }

     // 存放长整形值
     public void setUserInfo(String key, Long value) {
         editor.remove(key);
         editor.putLong(key, value);
         editor.commit();
     }

     // 存放布尔型值
     public void setUserInfo(String key, Boolean value) {
         editor.remove(key);
         editor.putBoolean(key, value);
         editor.commit();
     }

    // 清空记录
     public void clear() {
         editor.clear();
         editor.commit();
     }

      //注销用户时清空用户名和密码
//      public void logOut(UserBean user) {
//      SharedPreferences sp = context.getSharedPreferences(USER_INFO,
//      Context.MODE_PRIVATE);
//      SharedPreferences.Editor editor = sp.edit();
//      editor.remove(ACCOUNT);
//      editor.remove(PASSWORD);
//      editor.commit();
//      }

     // 获得用户信息中某项字符串型的值
     public String getStringInfo(String key) {
         return sp.getString(key, "");
     }

     // 获得用户息中某项整形参数的值
     public int getIntInfo(String key) {
         return sp.getInt(key, -1);
    }

     // 获得用户信息中某项长整形参数的值
     public Long getLongInfo(String key) {
         return sp.getLong(key, -1);
     }

     // 获得用户信息中某项布尔型参数的值
     public boolean getBooleanInfo(String key) {
         return sp.getBoolean(key, false);
     }
 }