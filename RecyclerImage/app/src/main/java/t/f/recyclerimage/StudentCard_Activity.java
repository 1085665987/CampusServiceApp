package t.f.recyclerimage;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.StudentCardAdapter;
import infos.UserInfo;
import utils.Property;
import utils.StatusBarLightModeUtil;

/**
 * Created by Friday on 2018/7/16.
 */

public class StudentCard_Activity extends Activity {
    private ImageView back;
    private TextView top,university,sno,balance;
    private RecyclerView recyclerView;
    private StudentCardAdapter studentCardAdapter;
    public static String TAG="StudentCard_Activity";
    public static String ACTIVITY="校园卡";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_card);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        initView();
        setOnClick();
    }

    private  void initView(){
        back=(ImageView)findViewById(R.id.cancer);
        top=(TextView)findViewById(R.id.top);
        university=(TextView)findViewById(R.id.university);
        sno=(TextView)findViewById(R.id.university);
        balance=(TextView)findViewById(R.id.balance);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

        top.setText(ACTIVITY);

        //创建LinearLayoutManager 对象 这里使用 LinearLayoutManager 是线性布局
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutManager);

        String []txt_items=this.getResources().getStringArray(R.array.function_student_card_txt);
//        int[] img_items=this.getResources().getIntArray(R.array.function_student_card_img);
        int[] img_items=new int[]{R.mipmap.zhangdan,R.mipmap.jiaoyi_mingxi,R.mipmap.guashi,
                R.mipmap.jianka_dengji,R.mipmap.change_pass};
        List<String> txtList=new ArrayList<>();
        List<Integer> imgList=new ArrayList<>();
        for(int i=0; i<txt_items.length;i++){
            txtList.add(txt_items[i]);
            imgList.add(img_items[i]);
            Log.i(TAG,img_items[i]+"");
        }
        studentCardAdapter=new StudentCardAdapter(txtList,imgList,this,R.layout.student_card_item);
        recyclerView.setAdapter(studentCardAdapter);
    }

    private void setOnClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        studentCardAdapter.setItemClickListener(new StudentCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case 0:
                        Toast.makeText(StudentCard_Activity.this,"点击了第"+position+"项",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getCardInfo(){
        String url=this.getResources().getString(R.string.school_url)+"?id="+new UserInfo(this).getIntInfo(Property.NO);
    }
}
