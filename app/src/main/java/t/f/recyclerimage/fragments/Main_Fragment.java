package t.f.recyclerimage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.utils.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.FunctionGridViewAdapter;
import JavaBeans.SchoolBean;
import JavaBeans.StudentBean;
import infos.UserInfo;
import okhttp3.Call;
import t.f.recyclerimage.DatePicker_Activity;
import t.f.recyclerimage.Fix_Activity;
import t.f.recyclerimage.R;
import t.f.recyclerimage.School_WebSite_Activity;
import t.f.recyclerimage.StudentCard_Activity;
import t.f.recyclerimage.Train_tickets_Activity;
import t.f.recyclerimage.Upload_Wall_Activity;
import t.f.recyclerimage.lost_fragment.Lost_Activity;
import utils.GlideCircleTransform;
import utils.Property;
import views.MZBannerView;

/**
 * Created by Friday on 2018/7/11.
 */

public class Main_Fragment extends Fragment implements AdapterView.OnItemClickListener{
    public static String TAG="Main_Fragment";
    protected boolean isCreated = false;

    private RelativeLayout topPicture;
    private ImageView xiaohui;                        //主界面显示的校徽
    private TextView schoolName;                        //主界面显示的学校名称
    private TextView name;                              //主界面显示的用户的真实姓名
    private TextView studentNumber;                     //主界面显示的用户的学号
    private GridView functionGridView;                  //主界面显示的应用功能的九宫格

    private String resource[];                          //功能的字的资源
//    private int resourceImg[]=new int[]{R.mipmap.school_card_1,R.mipmap.train_1,R.mipmap.dianfei,
//                                        R.mipmap.dianfei,R.mipmap.school,R.mipmap.lost_or_found,
//                                        R.mipmap.fix,R.mipmap.tongzhi,R.mipmap.fujin};               //功能的图片的资源

    private int resourceImg[]=new int[]{R.mipmap.school_card_1,R.mipmap.train_1,R.mipmap.dianfei,
            R.mipmap.dianfei,R.mipmap.school,R.mipmap.lost_or_found,
            R.mipmap.fix,R.mipmap.tongzhi,R.mipmap.fujin};
    private View view;
    private SchoolBean schoolBean=null;             //本界面需要用的学校实体
    private StudentBean studentBean=null;           //本界面需要用到的学生实体
    private MZBannerView mMZBanner;

    private ViewFlipper viewFlipper;                //表白墙滚动显示
    private TextView fliper_item_1,fliper_item_2;   //用来显示滚动
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view!=null){}else {
            view = inflater.inflate(R.layout.main_fragment, null);
            initViews();
        }
        return view;
    }
    private void initViews(){

        topPicture=(RelativeLayout)view.findViewById(R.id.campus);
        xiaohui=(ImageView)view.findViewById(R.id.xiaohui);
        schoolName=(TextView)view.findViewById(R.id.university);
        name=(TextView)view.findViewById(R.id.name);
        studentNumber=(TextView)view.findViewById(R.id.sno);
        functionGridView=(GridView)view.findViewById(R.id.mainly_function);

        resource=getActivity().getResources().getStringArray(R.array.function_resource);
        for(int i=0;i<resource.length;i++){
            resourceImg[i]=R.mipmap.ic_launcher_round;
        }
        functionGridView.setAdapter(new FunctionGridViewAdapter(resource,resourceImg,getActivity()));
        functionGridView.setOnItemClickListener(this);

        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewFlipper);

        mMZBanner = (MZBannerView)view.findViewById(R.id.banner);
        getPersonData(new UserInfo(getActivity()).getLongInfo(Property.NO));

        addFliperView();

        setOnClick();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMZBanner.start();//开始轮播
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //获得个人信息资料
    public void getPersonData(long id){

        String url=getActivity().getResources().getString(R.string.school_url)+"?phone="+id;
        Log.e(TAG,url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG,response);
                schoolBean=new SchoolBean();
                studentBean=new StudentBean();
                List<String> list;
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    schoolBean.setSchoolName(jsonObject.getString(Property.SCHOOL_NAME));
                    schoolBean.setSchoolId(jsonObject.getInt(Property.SCHOOL_ID));
                    schoolBean.setSchoolXiaohui(getResources().getString(R.string.base_image_url)+jsonObject.getString(Property.SCHOOL_XIAOHUI));
                    JSONArray pictures=jsonObject.getJSONArray(Property.SCHOOL_PICTURES);
                    if(pictures.length()>0) {
                        list = new ArrayList<>();
                        for (int i = 0; i < pictures.length(); i++) {
                            list.add(getResources().getString(R.string.base_image_url)+pictures.getString(i));
                        }
                        schoolBean.setSchoolPicture(list);
                    }
                    UserInfo userInfo=new UserInfo(getActivity());
                    if(jsonObject.getString(Property.STUDENT_NAME)!=null&&(!jsonObject.getString(Property.STUDENT_NAME).trim().equals("null"))) {
                        studentBean.setName(jsonObject.getString(Property.STUDENT_NAME));
                        studentBean.setSno(jsonObject.getLong(Property.STUDENT_NO));

                        name.setText(studentBean.getName());
                        studentNumber.setText(studentBean.getSno()+"");

                        userInfo.setUserInfo(Property.STUDENT_NAME,studentBean.getName());
                        userInfo.setUserInfo(Property.STUDENT_NO,studentBean.getSno());
                    }

                    userInfo.setUserInfo(Property.SCHOOL_NAME,schoolBean.getSchoolName());
                    userInfo.setUserInfo(Property.SCHOOL_ID,schoolBean.getSchoolId());
                    userInfo.setUserInfo(Property.SCHOOL_XIAOHUI,schoolBean.getSchoolXiaohui());



                    schoolName.setText(schoolBean.getSchoolName());
                    mMZBanner.setPages(schoolBean.getSchoolPicture(), new holder.MZHolderCreator<holder.BannerViewHolder>() {
                        @Override
                        public holder.BannerViewHolder createViewHolder() {
                            return new holder.BannerViewHolder();
                        }
                    });
                    Glide.with(getActivity()).load(schoolBean.getSchoolXiaohui()).transform(new GlideCircleTransform(getActivity())).into(xiaohui);
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void setOnClick(){
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Upload_Wall_Activity.class);
                getActivity().startActivity(intent);
            }
        });
    }


    private void addFliperView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_fliper_item,null);
        fliper_item_1=(TextView)view.findViewById(R.id.flipper_item_1);
        fliper_item_2=(TextView)view.findViewById(R.id.flipper_item_2);

        viewFlipper.addView(view);

        final ArrayList <String>itemtexts=new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                itemtexts.clear();
                String url=getActivity().getResources().getString(R.string.wall_show);
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("rs");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject item=jsonArray.getJSONObject(i);
                                String username=item.getString(Property.forumUsername_key);
                                String content=item.getString(Property.forum_content_key);

                                itemtexts.add(username+"："+content);

                                Log.i(TAG,"表白墙 ："+username+"："+content);
                            }

                            if (itemtexts.size()%2!=0){
                                itemtexts.add(itemtexts.get(0));
                            }

                            /**
                             * 每隔6秒
                             * 发送轮播的消息
                             */
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    int i=0;
                                    while (true){
                                        Message message=new Message();
                                        message.obj=new String[]{itemtexts.get(i),itemtexts.get(i+1)};
                                        message.what=1;
                                        mHandler.sendMessage(message);
                                        try {
                                            Thread.sleep(6*1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        i++;
                                        if (i==itemtexts.size()-1){
                                            i=0;
                                        }
                                    }
                                }
                            }.start();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                try {
                    Thread.sleep(30*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                String [] data= (String[]) msg.obj;

                fliper_item_1.setText(data[0]);
                fliper_item_2.setText(data[1]);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Intent intent=new Intent();
        switch (i){
            case 0:
                intent.setClass(getActivity(), StudentCard_Activity.class);
                getActivity().startActivity(intent);
                break;
            case 1 :
                intent.setClass(getActivity(), Train_tickets_Activity.class);
                getActivity().startActivity(intent);
                break;
            case 3:
                String url=getActivity().getResources().getString(R.string.school_website_url)+"?school_id="+schoolBean.getSchoolId();

                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "查询出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.trim().equals("0")) {
                            Toast.makeText(getActivity(), "查询出错", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.trim().equals("1")) {
                            Toast.makeText(getActivity(), "没有查询到该学校消息", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        intent.putExtra("WEBSITE",response);
                        intent.setClass(getActivity(), School_WebSite_Activity.class);
                        getActivity().startActivity(intent);
                    }
                });

                break;
            case 4:
                intent.setClass(getActivity(), Lost_Activity.class);
                getActivity().startActivity(intent);
                break;
            case 5:
                intent.setClass(getActivity(), Fix_Activity.class);
                getActivity().startActivity(intent);
                break;
            case 7:
                intent.setClass(getActivity(), DatePicker_Activity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

}
