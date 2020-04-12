package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.Picutres_Adapter;
import infos.UserInfo;
import utils.MultEditListenerUtil;
import utils.PermissionsUtil;
import utils.Property;
import utils.StatusBarLightModeUtil;
import utils.UploadFiles;


/**
 * Created by Friday on 2018/8/13.
 */

public class Fix_Activity extends Activity {
    private ImageView back;
    //返回键

    private EditText fix_thing;
    //报修的东西是什么

    private EditText address;
    //地址

    private EditText content;
    //报修情况

    private RecyclerView picturesRecyclerView;
    //实景图片
    private Picutres_Adapter picutres_adapter;
    private ArrayList<String> imageUris;

    private EditText words_to_worder;
    //要对维修工人说的话

    private EditText name;
    //联系人姓名

    private EditText department;
    //要报修的公寓楼号

    private EditText phone;
    //联系电话

    private Button btn_fix;

    public static final int CAMERA_REQUEST_CODE=101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fix);

        initView();

        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);

        setOnClick();
    }
    private  void initView(){
        fix_thing=(EditText)findViewById(R.id.thing);
        address=(EditText)findViewById(R.id.address);
        content=(EditText)findViewById(R.id.content);
        department=(EditText)findViewById(R.id.partment);
        words_to_worder=(EditText)findViewById(R.id.word_to_worker);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);

        picturesRecyclerView=(RecyclerView)findViewById(R.id.pictures);
        picturesRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        imageUris=new ArrayList<>();
        imageUris.add("http://eggplant1-blog.stor.sinaapp.com/head_picture/h_m.jpg");
//        imageUris.add("http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffceee78e5eb5014a90f703ea09.jpg");

        picutres_adapter=new Picutres_Adapter(this,imageUris);
        picturesRecyclerView.setAdapter(picutres_adapter);

        btn_fix=(Button)findViewById(R.id.btn_fix);

        name.setText(new UserInfo(this).getStringInfo(Property.STUDENT_NAME));
        phone.setText(new UserInfo(this).getLongInfo(Property.NO)+"");

        back=(ImageView)findViewById(R.id.cancer);
    }

    private void setOnClick(){

        //下面是对按钮的可用监听，当必填项都填上时。。。。。。，当有空值的时候，按钮不发挥作用
        MultEditListenerUtil.MultEditListener multEditListener=new MultEditListenerUtil.MultEditListener();
        multEditListener.addEditTexts(fix_thing,address,name,phone,department);//添加上所有的EditText控件

        MultEditListenerUtil.setMultEditListener(new MultEditListenerUtil.IMultEditListener() {
            @Override
            public void textChange(boolean isFull) {
                if (isFull){
                    btn_fix.setBackgroundColor(Fix_Activity.this.getResources().getColor(R.color.cursor_color));
                }else{
                    btn_fix.setBackgroundColor(Fix_Activity.this.getResources().getColor(R.color.no_selected_color));
                    //设置按钮不可用颜色，实际按钮可用，并弹出提示
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fix_Activity.this.finish();
            }
        });


        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fix_thing.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "请填写报修的物品", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (address.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "地址是必填项", Toast.LENGTH_SHORT).show();
                    return;
                }if (department.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "请填写公寓楼号", Toast.LENGTH_SHORT).show();
                    return;
                }if (name.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "请填写联系人姓名", Toast.LENGTH_SHORT).show();
                    return;
                }if (phone.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "联系方式是必填项", Toast.LENGTH_SHORT).show();
                    return;
                }if (content.getText().toString().trim().length()==0){
                    Toast.makeText(Fix_Activity.this, "请填写损坏详情", Toast.LENGTH_SHORT).show();
                    return;
                }if (imageUris.size()<=1){
                    Toast.makeText(Fix_Activity.this, "图片是必填项", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url=getResources().getString(R.string.fix_url);
                Map<String,String> data=new HashMap<>();
                data.put("phone",new UserInfo(Fix_Activity.this).getLongInfo(Property.NO)+""); //id
                data.put("goods",fix_thing.getText().toString().trim());        //需要修的东西
                data.put("address",address.getText().toString().trim());         //地址
                data.put("name",name.getText().toString().trim());               //联系人名称
                data.put("desribe",content.getText().toString().trim());       //损坏详情
                data.put("area",department.getText().toString().trim());        //公寓楼号
                data.put("contact_phone",phone.getText().toString().trim());        //联系电话

                if (words_to_worder.getText().toString().trim().length()!=0){
                    data.put("hope",words_to_worder.getText().toString().trim());
                }else{
                    data.put("hope","null");
                }    //对维修师傅说的话，可以是空，是空的时候，传过去null

                imageUris.remove(0);
                UploadFiles.upLoadToServer(Fix_Activity.this,url,data,imageUris);

                Fix_Activity.this.finish();
            }
        });

        picutres_adapter.setOnItemClickListener(new Picutres_Adapter.OnClickListener() {
            @Override
            public void onItemClick(int tag, View v, int position) {
                if (PermissionsUtil.cameraPermission(Fix_Activity.this)==0){
                    Toast.makeText(Fix_Activity.this,"权限不可用",Toast.LENGTH_SHORT).show();
                    return;
                }
                //当tag是100的时候，添加图片
                //当时101的时候，删除position所在的图片

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if(imageUris.size()==1){
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);
                    return;
                }

                if (tag==100){
                    if (imageUris.size()==4){
                        Toast.makeText(Fix_Activity.this,"最多可选3张图片",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position==0){
                        startActivityForResult(intent,CAMERA_REQUEST_CODE);
                        return;
                    }else{
                        Toast.makeText(Fix_Activity.this,"加载"+position,Toast.LENGTH_SHORT).show();
                    }
                }else if (tag==101){
                    picutres_adapter.notifyItemRemoved(position);
                    imageUris.remove(position);
                    if (position != imageUris.size()) {
                        picutres_adapter.notifyItemRangeChanged(position, imageUris.size() - position);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                //返回图片在手机的绝对地址
                if ( resultCode == Activity.RESULT_OK &&data!=null){
                    Uri uri=data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    imageUris.add(imagePath);
                    picutres_adapter.notifyItemRangeInserted(imageUris.size()-1,1);
                    c.close();
                }
                break;
        }
    }
}
