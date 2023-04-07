package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import infos.UserInfo;
import utils.Property;
import utils.StatusBarLightModeUtil;

public class My_Data_Activity extends Activity implements OnRefreshListener,OnLoadMoreListener{
    private SwipeToLoadLayout swipeToLoadLayout;
    //可以下拉涮新的控件

    private RecyclerView recyclerView;
    //我的帖子

    private ImageView head;
    //头像
    private TextView name;
    //昵称
    private TextView school;
    //学校

    private ImageView sex_img;
    //性别

    private ImageView back;
    //返回键

    private TextView changeData;
    //修改资料

    private ImageView backgroundImage;
    //头部背景图

    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_data);
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        //魅族手机设置标题栏颜色白色，字黑色
        initView();
        setOnClick();
    }

    private void initView(){
        userInfo=new UserInfo(this);

        swipeToLoadLayout=(SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        head=(ImageView)findViewById(R.id.head);
        name=(TextView)findViewById(R.id.name);
        back=(ImageView)findViewById(R.id.cancel);
        backgroundImage=(ImageView)findViewById(R.id.image);
        changeData=(TextView)findViewById(R.id.change_data);

        school=(TextView)findViewById(R.id.school);
        sex_img=(ImageView)findViewById(R.id.sex);

        school.setText(userInfo.getStringInfo(Property.SCHOOL_NAME));
        name.setText(userInfo.getStringInfo(Property.STUDENT_NAME));
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    private void setOnClick(){
        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(My_Data_Activity.this,Change_Data_Activity.class);
                startActivity(intent);
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更换头像
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                My_Data_Activity.this.finish();
            }
        });
        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更换背景图片
            }
        });
    }

    private void getData(){

    }
}
