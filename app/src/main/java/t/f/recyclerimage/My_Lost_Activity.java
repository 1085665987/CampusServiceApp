package t.f.recyclerimage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.Picutres_Adapter;
import infos.UserInfo;
import t.f.recyclerimage.lost_fragment.Lost_Activity;
import utils.MultEditListenerUtil;
import utils.PermissionsUtil;
import utils.Property;
import utils.StatusBarLightModeUtil;
import utils.UploadFiles;

import static t.f.recyclerimage.Fix_Activity.CAMERA_REQUEST_CODE;

public class My_Lost_Activity extends Activity {
    /**
     * 用来发布丢失物品信息
     */

    private EditText content;           //物品的具体描述

    private RecyclerView pictures;      //物品的图片

    private EditText phone;             //联系人电话

    private Button btn;                 //确定发布按钮

    private ArrayList<String> uri;      //图片URI

    private Picutres_Adapter picutres_adapter;      //图片适配器

    private TextView lost,found;         //标记丢失还是捡到的标签
    private String lost_or_found="lost"; //标记丢失还是捡到,默认丢失

    private TextView first_show,no_first_show;      //是否优先显示
    private String is_first="N";          //是否优先

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lost);

        initView();
        onClick();
    }
    private void initView(){

        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(),true);
        content=(EditText) findViewById(R.id.content);
        pictures=(RecyclerView)findViewById(R.id.pictures);
        phone=(EditText)findViewById(R.id.phone);
        btn=(Button)findViewById(R.id.btn_fix);

        lost=(TextView)findViewById(R.id.lost);
        found=(TextView)findViewById(R.id.found);

        first_show=(TextView)findViewById(R.id.yes);
        no_first_show=(TextView)findViewById(R.id.no);

        uri=new ArrayList<>();
        uri.add("http://eggplant1-blog.stor.sinaapp.com/head_picture/h_m.jpg");
//        uri.add("http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffceee78e5eb5014a90f703ea09.jpg");

        picutres_adapter=new Picutres_Adapter(this,uri);
        pictures.setLayoutManager(new GridLayoutManager(this,3));
        pictures.setAdapter(picutres_adapter);

        phone.setText(new UserInfo(this).getLongInfo(Property.NO)+"");

        //下面是对按钮的可用监听，当必填项都填上时。。。。。。，当有空值的时候，按钮不发挥作用
        MultEditListenerUtil.MultEditListener multEditListener=new MultEditListenerUtil.MultEditListener();
        multEditListener.addEditTexts(content,phone);//添加上所有的EditText控件

        MultEditListenerUtil.setMultEditListener(new MultEditListenerUtil.IMultEditListener() {
            @Override
            public void textChange(boolean isFull) {
                if (isFull){
                    btn.setBackgroundColor(My_Lost_Activity.this.getResources().getColor(R.color.cursor_color));
                }else{
                    btn.setBackgroundColor(My_Lost_Activity.this.getResources().getColor(R.color.no_selected_color));
                    //设置按钮不可用颜色，实际按钮可用，并弹出提示
                }
            }
        });
    }

    private void onClick(){
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lost_or_found="lost";

                lost.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                lost.setTextColor(getResources().getColor(R.color.white));

                found.setTextColor(getResources().getColor(R.color.txt_defalt_color));
                found.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });
        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lost_or_found="found";

                found.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                found.setTextColor(getResources().getColor(R.color.white));

                lost.setTextColor(getResources().getColor(R.color.txt_defalt_color));
                lost.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });

        first_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_first="Y";

                first_show.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                first_show.setTextColor(getResources().getColor(R.color.white));

                no_first_show.setTextColor(getResources().getColor(R.color.txt_defalt_color));
                no_first_show.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });
        no_first_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_first="N";
                no_first_show.setBackground(getResources().getDrawable(R.drawable.seleted_txt));
                no_first_show.setTextColor(getResources().getColor(R.color.white));

                first_show.setTextColor(getResources().getColor(R.color.txt_defalt_color));
                first_show.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (content.getText().toString().trim().length()==0){
                    Toast.makeText(My_Lost_Activity.this, "请填写物品及其特征描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.getText().toString().trim().length()==0){
                    Toast.makeText(My_Lost_Activity.this, "联系电话不能是空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url=getResources().getString(R.string.lost_found_insert);

                Map<String,String> map=new HashMap<>();
                map.put("content",content.getText().toString().trim());
                map.put("contact",phone.getText().toString().trim());
                map.put("phone",new UserInfo(My_Lost_Activity.this).getLongInfo(Property.NO)+"");
                map.put("type",lost_or_found);
                map.put("first",is_first);


                if (uri.get(0).trim().equals("http://eggplant1-blog.stor.sinaapp.com/head_picture/h_m.jpg"))
                    uri.remove(0);
                UploadFiles.upLoadToServer(My_Lost_Activity.this,url,map,uri);

                My_Lost_Activity.this.finish();
            }
        });
        picutres_adapter.setOnItemClickListener(new Picutres_Adapter.OnClickListener() {
            @Override
            public void onItemClick(int tag, View v, int position) {
                if (PermissionsUtil.cameraPermission(My_Lost_Activity.this)==0){
                    Toast.makeText(My_Lost_Activity.this,"权限不可用",Toast.LENGTH_SHORT).show();
                    return;
                }
                //当tag是100的时候，添加图片
                //当时101的时候，删除position所在的图片

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if(uri.size()==1){
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);
                    return;
                }

                if (tag==100){
                    if (uri.size()==4){
                        Toast.makeText(My_Lost_Activity.this,"最多可选3张图片",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position==0){
                        startActivityForResult(intent,CAMERA_REQUEST_CODE);
                        return;
                    }else{
                        Toast.makeText(My_Lost_Activity.this,"加载"+position,Toast.LENGTH_SHORT).show();
                    }
                }else if (tag==101){
                    if (uri.size()==3){
                        picutres_adapter.notifyItemRemoved(position);
                        uri.remove(position);

                        uri.add(0,"http://a.hiphotos.baidu.com/image/pic/item/b64543a98226cffceee78e5eb5014a90f703ea09.jpg");
                        picutres_adapter.notifyItemRangeInserted(0,1);
                    }else {

                        picutres_adapter.notifyItemRemoved(position);
                        uri.remove(position);
                        if (position != uri.size()) {
                            picutres_adapter.notifyItemRangeChanged(position, uri.size() - position);
                        }
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
                    if (this.uri.size()==3){
                        this.uri.remove(0);
                        picutres_adapter.notifyItemRangeRemoved(0,1);
                    }
                    this.uri.add(imagePath);
                    picutres_adapter.notifyItemRangeInserted(this.uri.size()-1,1);
                    c.close();
                }
                break;
        }
    }
}
