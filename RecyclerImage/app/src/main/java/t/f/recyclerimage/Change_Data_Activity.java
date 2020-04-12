package t.f.recyclerimage;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import JavaBeans.PersonBean;
import JavaBeans.SchoolBean;
import JavaBeans.StudentBean;
import JavaBeans.UserBean;
import infos.UserInfo;
import okhttp3.Call;
import utils.DialogUtils;
import utils.FileUtil;
import utils.GlideCircleTransform;
import utils.PermissionsUtil;
import utils.Property;
import utils.StatusBarLightModeUtil;
import utils.UploadFiles;
import views.CommomDialog;

public class Change_Data_Activity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CAMEAL_CODE = 100;
    private static final int REQUEST_CHANGE_NAME_CODE = 101;
    private static final int REQUEST_CHANGE_SIGNATURE_CODE = 102;


    public static final String CHANGENAME_EXTRA_KEY = "name";
    public static final String CHANGESIGNATURE_EXTRA_KEY = "signature";


    private static final String TAG = "Change_Data_Activity";

    private RelativeLayout change_head;         //更改头像
    private RelativeLayout change_sex;          //更改性别
    private RelativeLayout change_nickname;     //更改昵称
    private RelativeLayout change_signature;    //更改个性签名
    private RelativeLayout change_name;         //更改真实姓名
    private RelativeLayout change_student_no;   //更改学号
    private RelativeLayout change_school;       //更改学校

    private ImageView head;                     //用来显示头像
    private TextView sex;                       //用来显示性别
    private TextView nickname;                  //用来显示昵称
    private TextView name;                      //用来显示真实姓名
    private TextView student_no;                //用来显示学号
    private TextView school;                    //用来显示学校
    private TextView signature;                 //用来显示个性签名


    private LinearLayout layout;                //显示dialog
    private ProgressBar progressBar;

    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_data);
        initView();
        StatusBarLightModeUtil.FlymeSetStatusBarLightMode(getWindow(), true);

        getData();
    }

    private void initView() {
        PersonBean.schoolBean = new SchoolBean();
        PersonBean.studentBean = new StudentBean();
        PersonBean.userBean = new UserBean();

        change_head = (RelativeLayout) findViewById(R.id.change_head);
        change_sex = (RelativeLayout) findViewById(R.id.change_sex);
        change_nickname = (RelativeLayout) findViewById(R.id.change_nickname);
        change_name = (RelativeLayout) findViewById(R.id.change_name);
        change_student_no = (RelativeLayout) findViewById(R.id.change_student_no);
        change_school = (RelativeLayout) findViewById(R.id.change_school);
        change_signature = (RelativeLayout) findViewById(R.id.change_signature);

        head = (ImageView) findViewById(R.id.head);
        sex = (TextView) findViewById(R.id.sex);
        nickname = (TextView) findViewById(R.id.nickname);
        name = (TextView) findViewById(R.id.name);
        student_no = (TextView) findViewById(R.id.student_no);
        school = (TextView) findViewById(R.id.school);
        signature = (TextView) findViewById(R.id.signature);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout = (LinearLayout) findViewById(R.id.progressBar_parent);
        layout.setVisibility(View.VISIBLE);
        back = (ImageView) findViewById(R.id.cancer);

        change_head.setOnClickListener(this);
        change_sex.setOnClickListener(this);
        change_nickname.setOnClickListener(this);
        change_name.setOnClickListener(this);
        change_student_no.setOnClickListener(this);
        change_school.setOnClickListener(this);

        head.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.change_head:
                showChoosePictureResourceDialog();
                break;
            case R.id.change_sex:
                //显示选择性别的dialog
                showChooseSexDialog(Change_Data_Activity.this);
                break;
            case R.id.change_nickname:
                intent.setClass(this, Change_Nickname_Activity.class);
                startActivityForResult(intent, REQUEST_CHANGE_NAME_CODE);
                break;
            case R.id.change_name:
                Toast.makeText(this, "无法修改真实姓名", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_student_no:
                Toast.makeText(this, "无法修改学号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_school:
                showChooseSchoolDialog(intent);
                break;
            case R.id.cancer:
                this.finish();
                break;
            case R.id.signature:
                intent.setClass(this, Change_Signature_Activity.class);
                startActivityForResult(intent, REQUEST_CHANGE_NAME_CODE);
                break;
        }
    }

    private void getData() {
        String url = getResources().getString(R.string.edit_down_url) + "?phone=" + new UserInfo(this).getLongInfo(Property.NO);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                layout.setVisibility(View.GONE);
                Toast.makeText(Change_Data_Activity.this, "加载异常，\n请稍后重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.trim().equals("0")) {
                    layout.setVisibility(View.GONE);
                    Toast.makeText(Change_Data_Activity.this, "加载异常，\n请稍后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    PersonBean.userBean.setPhoto(getResources().getString(R.string.base_image_url) + jsonObject.getString(Property.forumUserHeadxiang_key));
                    PersonBean.userBean.setSex(jsonObject.getString(Property.SEX));
                    PersonBean.userBean.setNikeName(jsonObject.getString(Property.forumUsername_key));
                    PersonBean.studentBean.setName(jsonObject.getString(Property.NAME));
                    PersonBean.studentBean.setSno(Long.parseLong(jsonObject.getString(Property.STUDENT_NO).trim()));
                    PersonBean.schoolBean.setSchoolName(jsonObject.getString(Property.SCHOOL_NAME));


                    Glide.with(Change_Data_Activity.this).
                            load(PersonBean.userBean.getPhoto()).
                            transform(new GlideCircleTransform(Change_Data_Activity.this)).
                            into(head);

                    sex.setText(PersonBean.userBean.getSex());
                    nickname.setText(PersonBean.userBean.getNikeName());
                    name.setText(PersonBean.studentBean.getName());
                    student_no.setText(PersonBean.studentBean.getSno() + "");
                    school.setText(PersonBean.schoolBean.getSchoolName());

                    layout.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        switch (requestCode) {
            case REQUEST_CAMEAL_CODE:                       //从相机获取图片
                uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = (Bitmap) bundle.get("data"); //get bitmap
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        Glide.with(Change_Data_Activity.this).load(bytes).transform(new GlideCircleTransform(Change_Data_Activity.this)).into(head);
                        ArrayList<File> files = new ArrayList<>();
                        files.add(FileUtil.getFile(bytes, Environment.getExternalStorageDirectory() + "/DCIM/xiaoyuanxing", new UserInfo(this).getLongInfo(Property.NO) + ".png"));
//                        UploadFiles.upLoadFileToServer(this,"",files);
                        FileUtil.doDeleteFile(files.get(0));
                    }
                }
                break;
            case REQUEST_CHANGE_SIGNATURE_CODE:
                if (resultCode == RESULT_OK) {
                    String bundle = data.getStringExtra(CHANGENAME_EXTRA_KEY);
                    Log.e(TAG, bundle);
                    nickname.setText(bundle);
                    PersonBean.userBean.setNikeName(bundle);
                }
                break;
            case REQUEST_CHANGE_NAME_CODE:
                if (resultCode == RESULT_OK) {
                    String bundle = data.getStringExtra(CHANGESIGNATURE_EXTRA_KEY);
                    Log.e(TAG, bundle);
                    nickname.setText(bundle);
                    PersonBean.userBean.setNikeName(bundle);
                }
                break;
        }
    }

    //显示是否确定更换学校的dialog
    private void showChooseSchoolDialog(final Intent intent) {
        CommomDialog chooseSchoolDialog = new CommomDialog(this, R.style.dialog, "更换学校后，校园卡信息将丢失\n您确定重新选择学校？", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    intent.setClass(Change_Data_Activity.this, Choose_School_Activity.class);
                    startActivity(intent);
                }
                dialog.cancel();
            }
        });
        chooseSchoolDialog.show();
    }

    //显示选择图片来源的dialog
    private void showChoosePictureResourceDialog() {
        final Dialog mDialog = new Dialog(Change_Data_Activity.this, R.style.BottomDialog);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(Change_Data_Activity.this).inflate(R.layout.picture_choose_dialog, null);
        linearLayout.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtil.cameraPermission(Change_Data_Activity.this) == 0) {
                    Toast.makeText(Change_Data_Activity.this, "权限不可用", Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    Toast.makeText(Change_Data_Activity.this, "权限可用", Toast.LENGTH_SHORT).show();
                    String path = null;
                    try {
                        path = FileUtil.getImageSavePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, path);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
                    startActivityForResult(intent, REQUEST_CAMEAL_CODE);
                }
                mDialog.cancel();
            }
        });
        linearLayout.findViewById(R.id.being_pictures).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.cancel();
            }
        });
        linearLayout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.cancel();
            }
        });
        mDialog.setContentView(linearLayout);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = Change_Data_Activity.this.getResources().getDisplayMetrics().widthPixels;
        linearLayout.measure(0, 0);
        lp.height = linearLayout.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

    //显示男女的dialog
    public void showChooseSexDialog(final Context context) {
        final Dialog mDialog = new Dialog(context, R.style.BottomDialog);
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.choice_sex, null);
        linearLayout.findViewById(R.id.male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex.setText("男");
                PersonBean.userBean.setSex("男");
                mDialog.cancel();
            }
        });
        linearLayout.findViewById(R.id.female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonBean.userBean.setSex("女");
                sex.setText("女");
                mDialog.cancel();
            }
        });
        linearLayout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.cancel();
            }
        });
        mDialog.setContentView(linearLayout);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        linearLayout.measure(0, 0);
        lp.height = linearLayout.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }


    //退出时更改资料
    private void update() {
        String url = "";

        Map<String, String> data = new HashMap<>();
        data.put(Property.SEX, PersonBean.userBean.getSex());
        data.put(Property.STUDENT_NAME, PersonBean.studentBean.getName());
        data.put(Property.STUDENT_NO, PersonBean.studentBean.getSno() + "");
        data.put(Property.SCHOOL_NAME, PersonBean.schoolBean.getSchoolName());
        ArrayList<String> uris = new ArrayList<>();
        uris.add(PersonBean.userBean.getPhoto());

        PersonBean.schoolBean = null;
        PersonBean.studentBean = null;
        PersonBean.userBean = null;

        UploadFiles.upLoadToServer(this, url, data, uris);
    }
}
