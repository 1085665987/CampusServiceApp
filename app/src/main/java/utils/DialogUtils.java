package utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;

import t.f.recyclerimage.R;

public class DialogUtils {

    public static String showChooseSexDialog(final Context context) {
        final Dialog mDialog = new Dialog(context, R.style.BottomDialog);
        final String[] sex = new String[1];
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.choice_sex, null);
        linearLayout.findViewById(R.id.male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "男", Toast.LENGTH_SHORT).show();
                sex[0] =((TextView)linearLayout.findViewById(R.id.male)).getText().toString().trim();
                mDialog.cancel();
            }
        });
        linearLayout.findViewById(R.id.female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex[0] =((TextView)linearLayout.findViewById(R.id.female)).getText().toString().trim();
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
        return sex[0];
    }

    public static String showChoosePictureResourceDialog(final Context context)throws IOException {
        final Dialog mDialog = new Dialog(context, R.style.BottomDialog);
        final String[] imgPath = {null};
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.picture_choose_dialog, null);
        linearLayout.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path []= null;
                if (PermissionsUtil.cameraPermission(context) == 0) {
                    Toast.makeText(context, "权限不可用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "权限可用", Toast.LENGTH_SHORT).show();
                    try {
                        path=new String[1];
                        path[0]=FileUtil.getImageSavePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                imgPath[0] =path[0];
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
        lp.width = context.getResources().getDisplayMetrics().widthPixels;
        linearLayout.measure(0, 0);
        lp.height = linearLayout.getMeasuredHeight();
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mDialog.show();
        return imgPath[0];
    }
}
