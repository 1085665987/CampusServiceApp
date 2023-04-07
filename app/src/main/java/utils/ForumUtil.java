package utils;

import android.content.Context;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import JavaBeans.ForumBean;
import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.R;

/**
 * Created by Friday on 2018/7/29.
 */

public class ForumUtil {
    public static String TAG="ForumUtil";

    public static void doDeleteForum(Context context,int forum_id,long phone){
        String url=context.getResources().getString(R.string.forum_delete)+"?dongtai_id="+forum_id+"&phone="+phone;
        Log.i(TAG,url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {}
            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);
            }
        });
    }
    public static void doAttentionSB(Context context,int forum_id,long phone){
        String url=context.getResources().getString(R.string.dongtai_do_attention)+"?dongtai_id="+forum_id+"&phone="+phone;
        Log.i(TAG,url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);
            }
        });
    }

    public static void doCancelAttentionSB(Context context,int forum_id,long phone){
        String url=context.getResources().getString(R.string.dongtai_cancel_attention)+"?dongtai_id="+forum_id+"&phone="+phone;
        Log.i(TAG,url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {}
            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);
            }
        });
    }

    public ForumBean doLove(ForumBean forumBean,Context context){
        final long phone=new UserInfo(context).getLongInfo(Property.NO);
        if(!forumBean.isLoved()) {
            String url = context.getResources().getString(R.string.forum_love) + "?dongtai_id=" + forumBean.getForumId() + "&phone=" + phone;
            Log.e(TAG,url);
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Log.i(TAG, response);
                }
            });
            forumBean.setLoved(!forumBean.isLoved());
            forumBean.setLoveCount(forumBean.getLoveCount()+1);
        }else{
            String url = context.getResources().getString(R.string.forum_love_cancel) + "?dongtai_id=" + forumBean.getForumId() + "&phone=" + phone;
            Log.e(TAG,url);

            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }
                @Override
                public void onResponse(String response, int id) {
                    Log.i(TAG, response);
                }
            });
            forumBean.setLoveCount(forumBean.getLoveCount()-1);
            forumBean.setLoved(!forumBean.isLoved());
        }
        return forumBean;
    }
}
