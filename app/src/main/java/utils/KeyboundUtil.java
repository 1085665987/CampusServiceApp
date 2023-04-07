package utils;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import t.f.recyclerimage.Msg_Login_Activity;
import t.f.recyclerimage.R;
/**
 * Created by Friday on 2018/7/10.
 */

public class KeyboundUtil {
    private Context mContext;//上下文对象
    private KeyboardView mKeyboardView;//这个主角怎么能丢？
    private Keyboard mKeyboard;//好吧，其实他也是主角
    private EditText editText;
    private StringBuilder sb=new StringBuilder();
    int maxCount;
    /** * 必须activity作为上下文对像 * @param context */
    public KeyboundUtil(Context context, final EditText editText,int keyboardId,int maxText) {
        this.editText=editText;
        this.maxCount=maxText;
        mContext = context;
        //初始化键盘布局，下面在放进 KeyBoardView里面去。
        mKeyboard = new Keyboard(mContext,R.xml.keybound);
        //配置keyBoardView
        try{
            mKeyboardView = (KeyboardView) ((Activity)context).findViewById(keyboardId);
            mKeyboardView.setKeyboard(mKeyboard);
            //装甲激活~ 咳咳…
            mKeyboardView.setPreviewEnabled(false);
            //这个是，效果图按住是出来的预览图。
            // 设置监听，不设置的话会报错。监听放下面了。
            mKeyboardView.setOnKeyboardActionListener(mListener);
        }catch (Exception e){
            Log.e("sun","keyview初始化失败");
        }
        editText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    KeyboundUtil.this.editText.setText(sb.toString());
                    sb.delete(0,sb.length());
                    mKeyboardView.setVisibility(View.VISIBLE);
                    Msg_Login_Activity.HideKeyboard(editText);
                } else {
                    // 此处为失去焦点时的处理内容
                    sb.delete(0,sb.length());
                    sb.append(editText.getText());
                    mKeyboardView.setVisibility(View.INVISIBLE);
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int inType = editText.getInputType(); // backup the input type//
                editText.setInputType(InputType.TYPE_NULL); // disable soft input
                editText.onTouchEvent(event); // call native handler
                editText.setInputType(inType); // restore input type
                return true;
            }
        });
    }

    //监听
    private KeyboardView.OnKeyboardActionListener mListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            Log.e("sun","onPress=======:"+primaryCode);

            if(primaryCode>=49&&primaryCode<=57) {
                int cha = primaryCode - 49;
                if(sb.toString().length()<maxCount)
                    KeyboundUtil.this.sb.append((1 + cha) + "");
                editText.setText(sb.toString());
                editText.setSelection(sb.length());
            }else{
                if(primaryCode==60&&sb.toString().length()>0){
                    sb.delete(sb.length()-1,sb.length());
                    editText.setText(sb.toString());
                    editText.setSelection(sb.length());
                }else if(primaryCode==59){
                    if(sb.toString().length()<maxCount)
                        KeyboundUtil.this.sb.append(0+ "");
                    editText.setText(sb.toString());
                    editText.setSelection(sb.length());
                }
            }
        }
        @Override
        public void onRelease(int primaryCode) {
            Log.e("sun","onRelease====:"+primaryCode);
        }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Log.e("sun","onkey=====primaryCode:"+primaryCode+"");
        }

        @Override
        public void onText(CharSequence charSequence) {

        }
        @Override
        public void swipeLeft() {
            Log.e("sun","swipeLeft");
        }
        @Override public void swipeRight() {
            Log.e("sun","swipeRight");
        }
        @Override public void swipeDown() {
            Log.e("sun","swipeDown");
        }
        @Override public void swipeUp() {
            Log.e("sun","swipeUp");
        }
    };
    public void setVisiable(boolean showOrNot){
        if(showOrNot){
            mKeyboardView.setVisibility(View.VISIBLE);
        }else{
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }
    public int isKetboardVisiable(){
        return mKeyboardView.getVisibility();
    }
}
