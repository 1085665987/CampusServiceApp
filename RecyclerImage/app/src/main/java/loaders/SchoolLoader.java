package loaders;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import JavaBeans.SchoolBean;
import okhttp3.Call;
import t.f.recyclerimage.R;
import utils.Property;

/**
 * Created by Friday on 2018/7/14.
 */

public class SchoolLoader {


    public SchoolLoader(int schoolId,Context context ){
        this.schoolId=schoolId;
        this.context=context;
    }
    private static Context context;

    private static SchoolBean schoolBean;

    private static int schoolId;
    private static String url;

    static {
        String url=context.getResources().getString(R.string.login_url)+schoolId;
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
    }
    public SchoolBean getSchool(){

        return schoolBean;
    }
}
