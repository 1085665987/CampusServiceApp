package utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import t.f.recyclerimage.Msg_Login_Activity;

/**
 * Created by Friday on 2018/7/11.
 */

public class LimitEdit implements TextWatcher {
    private CharSequence temp;
    private int editStart ;
    private int editEnd ;
    private EditText editText;
    private int count;

    public static String TAG="LimitEdit";

    public LimitEdit(EditText editText,int count){
        this.editText=editText;
        this.count=count;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        temp = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        editStart = editText.getSelectionStart();
        editEnd = editText.getSelectionEnd();
        if (temp.length() > count) {
            if(onClickAfterTextChanged!=null){
                onClickAfterTextChanged.passLimitCount(editStart,editEnd,s);
            }
//            Log.i(TAG, "你输入的字数已经超过了限制！");
//            Log.i(TAG, editStart+"\t"+editEnd);
//            s.delete(editStart-1, editEnd);
//            int tempSelection = editStart;
//            editText.setText(s);
//            editText.setSelection(tempSelection);
        }else if(temp.length()==count){
            if(onClickAfterTextChanged!=null){
                onClickAfterTextChanged.onLimitCount();
            }
        }
    }


    //暴露外部接口，提供调用方法

    public interface OnClickAfterTextChanged{
        void onLimitCount();
        void passLimitCount(int editStart,int editEnd,Editable s);
    }
    private OnClickAfterTextChanged onClickAfterTextChanged;
    public void setOnClickAfterTextChanged(OnClickAfterTextChanged onClickAfterTextChanged){
        this.onClickAfterTextChanged=onClickAfterTextChanged;
    }
}
