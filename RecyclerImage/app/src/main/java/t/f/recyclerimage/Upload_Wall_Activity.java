package t.f.recyclerimage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.othershe.calendarview.utils.CalendarUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.Wall_Adapter;
import JavaBeans.WallBean;
import infos.UserInfo;
import okhttp3.Call;
import utils.Property;
import utils.StatusBarLightModeUtil;
import views.CustomDialog;

public class Upload_Wall_Activity extends Activity implements View.OnClickListener,OnRefreshListener, OnLoadMoreListener,Wall_Adapter.OnItemClickListener{

    private TextView p_day_txt;         //显示前一天："前一天"
    private TextView n_day_txt;         //显示后一天："后一天"

    private TextView current_day;       //显示当天时间eg："2018-9-5"

    private LinearLayout n_day;         //按钮：下一天
    private LinearLayout p_day;         //按钮：前一天
    private LinearLayout date_picker;   //按钮：选择到某一天

    private ImageView back;             //返回按钮

    private RecyclerView recyclerView;  //这天的所有时间的列表
    private Wall_Adapter wall_adapter;  //适配器

    private ArrayList<WallBean> wallBeans;      //数据源

    private SwipeToLoadLayout swipeToLoadLayout;


    private int year,month,day,clock[];       //年月日，一共有24条，用于发送JSON数据
    private int[] currentDate;

    private TextView ok;

    public static final String TAG="Upload_Wall_Activity";

    public static final int DatePicker_Activity_REQUEST_CODE=101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall);

        initView();
        getData();

        setOnClick();
    }
    private void initView(){
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);

        p_day_txt=(TextView)findViewById(R.id.p_day_txt);
        n_day_txt=(TextView)findViewById(R.id.n_day_txt);
        current_day=(TextView)findViewById(R.id.current_day);

        n_day=(LinearLayout)findViewById(R.id.n_day);
        p_day=(LinearLayout)findViewById(R.id.p_day);
        date_picker=(LinearLayout)findViewById(R.id.datepicker);

        back=(ImageView)findViewById(R.id.cancer);
        ok=(TextView)findViewById(R.id.ok);

        recyclerView=(RecyclerView)findViewById(R.id.swipe_target);
        wallBeans=new ArrayList<>();
        wall_adapter=new Wall_Adapter(this,wallBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wall_adapter);

        swipeToLoadLayout=(SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setLoadMoreEnabled(false);

        currentDate = CalendarUtil.getCurrentDate();
        year=currentDate[0];
        month=currentDate[1];
        day=currentDate[2];
        clock=new int[24];

        current_day.setText(year+"-"+month+"-"+day);
        if (month<10)
            current_day.setText(year+"-0"+month);
        if (day<10)
            current_day.setText(current_day.getText().toString()+"-0"+day);

        for (int i=0;i<24;i++) {
            clock[i] = -1;
        }
    }

    private int count=0;
    @Override
    public void onItemClick(int tag, View view, int position) {
        if (tag==101){
//            LinearLayout linearLayout=(LinearLayout)view;
//            linearLayout.getCh

        }else if (tag==102){
            CompoundButton checkBox= (CompoundButton) view;
            if (checkBox.isChecked()){
                clock[position]=position;
                count++;
                ok.setText("确定时间");
            }else {
                clock[position]=-1;
                count--;
                if (count==0){
                    ok.setText("选择时间");
                }else{
                    ok.setText("确定时间");
                }
            }
        }
    }

    @Override
    public void onLoadMore() {}

    @Override
    public void onRefresh() {
        wallBeans.clear();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.n_day:
                if (year!=currentDate[0]+5&&month!=currentDate[1]&&day!=currentDate[2]) {
                    day++;
                    current_day.setText(year+"-"+month+"-"+day);
                }
                break;
            case R.id.p_day:
                if (year!=currentDate[0]&&month!=currentDate[1]&&day!=currentDate[2]) {
                    day--;
                    current_day.setText(year+"-"+month+"-"+day);
                }
                break;
            case R.id.datepicker:
                Intent intent=new Intent(Upload_Wall_Activity.this,DatePicker_Activity.class);
                startActivityForResult(intent,DatePicker_Activity_REQUEST_CODE);
                break;
            case R.id.cancer:
                break;
            case R.id.ok:
                showWriteCommentDialog();
                break;
        }
    }
    private void setOnClick(){
        n_day.setOnClickListener(this);
        p_day.setOnClickListener(this);
        date_picker.setOnClickListener(this);

        wall_adapter.setItemClickListener(this);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);

        swipeToLoadLayout.setOnRefreshListener(this);
    }

    /**
     * 停止加载或刷新
     */
    private void setSwipeToLoadLayout(){
        if (swipeToLoadLayout.isLoadingMore()){
            swipeToLoadLayout.setLoadingMore(false);
        }
        if (swipeToLoadLayout.isRefreshing()){
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    private void getData(){
        wallBeans.clear();
        String url=getResources().getString(R.string.wall_down)+"?date="+current_day.getText().toString();

        Log.i("第一次请求数据的URL",url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                setSwipeToLoadLayout();
                Toast.makeText(Upload_Wall_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("第一次请求数据：",response);
                if (response.trim().equals("{\"rs\":null}")){
                    setSwipeToLoadLayout();
                    String url=getResources().getString(R.string.wall_update)+"?date="+current_day.getText().toString();

                    Log.i("第2次请求数据：",url);
                    OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            setSwipeToLoadLayout();
                            Toast.makeText(Upload_Wall_Activity.this, "第二次服务器出错了", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("第2次请求数据：",response);
                            if (response.trim().equals("ok")){

                                swipeToLoadLayout.setRefreshing(true);
                                return;
                            }else{
                                setSwipeToLoadLayout();
                                Toast.makeText(Upload_Wall_Activity.this, "服务器出错了", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("rs");
                    for (int i=0;i<24;i++){
                        WallBean wallBean=new WallBean();
                        JSONObject wall_item=jsonArray.getJSONObject(i);
                        wallBean.setPicket(wall_item.getInt("surplus"));
                        wallBean.setStartTime(wall_item.getInt("time"));
                        wallBean.setStopTime(wallBean.getStartTime()+1);

                        wallBeans.add(wallBean);
                    }
                    setSwipeToLoadLayout();
                    wall_adapter.notifyItemRangeChanged(0,24);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==DatePicker_Activity_REQUEST_CODE&&resultCode==RESULT_OK){
            year=data.getIntExtra(DatePicker_Activity.YEAR_KEY,-1);
            month=data.getIntExtra(DatePicker_Activity.MONTH_KEY,-1);
            day=data.getIntExtra(DatePicker_Activity.DAY_KEY,-1);

            if (year==-1||month==-1||day==-1){
                year=currentDate[0];
                month=currentDate[1];
                day=currentDate[2];
            }

            current_day.setText(year+"-"+month+"-"+day);
            if (month<10)
                current_day.setText(year+"-0"+month);
            if (day<10)
                current_day.setText(current_day.getText().toString()+"-0"+day);

            getData();
        }
    }
    private void showWriteCommentDialog(){
        int ids[]=new int[]{R.id.cancel,R.id.send_comment,R.id.comment_context,R.id.biaoqing,R.id.pictures};
        CustomDialog dialog = new CustomDialog(this,ids);
        dialog.setOnClick(new CustomDialog.OnClick() {
            @Override
            public void onClick(Dialog dialog, View view) {
                switch (view.getId()){
                    case R.id.cancel:
                        dialog.cancel();
                        break;
                    case R.id.send_comment:
                        String txt_comment=((EditText)dialog.findViewById(R.id.comment_context)).getText().toString();
                        if(txt_comment.trim().equals("")){
                            Toast.makeText(Upload_Wall_Activity.this, "内容不能为空，呦", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(txt_comment.trim().length()>30){
                            Toast.makeText(Upload_Wall_Activity.this, "内容不能超过30个字，呦", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String url=getResources().getString(R.string.wall_insert)+"?phone="+new UserInfo(Upload_Wall_Activity.this).getLongInfo(Property.NO);
                        for (int i=0;i<24;i++){
                            if (clock[i]!=-1){
                                Log.i("已选中",""+clock[i]);
                                url=url+"&time"+clock[i]+"="+i;
                            }
                        }

                        url=url+"&content="+txt_comment+"&date="+current_day.getText().toString();

                        Log.i("发表URL",url);
                        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e(TAG,response);

                                if (response.trim().equals("ok")){
                                    Toast.makeText(getApplicationContext(),"发表成功",Toast.LENGTH_SHORT).show();
                                    Upload_Wall_Activity.this.finish();
                                }else if(response.trim().equals("1")){
                                    Toast.makeText(getApplicationContext(),"学豆不足\n请做任赚取学豆或充值",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.cancel();
                        break;
                    case R.id.biaoqing:
                        break;
                    case R.id.pictures:
                        break;
                }
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

}
