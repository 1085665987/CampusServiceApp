package views;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import t.f.recyclerimage.R;

public class VerifyCodeView extends RelativeLayout {
    private EditText editText;
    private TextView[] textViews;
    private static int MAX = 6;
    private String inputContent;
    private View[] views;

    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_verify_code, this);

        textViews = new TextView[MAX];
        textViews[0] = (TextView) findViewById(R.id.item_code_iv0);
        textViews[1] = (TextView) findViewById(R.id.item_code_iv1);
        textViews[2] = (TextView) findViewById(R.id.item_code_iv2);
        textViews[3] = (TextView) findViewById(R.id.item_code_iv3);
        textViews[4] = (TextView) findViewById(R.id.item_code_iv4);
        textViews[5] = (TextView) findViewById(R.id.item_code_iv5);
        editText = (EditText) findViewById(R.id.item_edittext);

        views=new View[MAX];
        views[0] =findViewById(R.id.item_view_0);
        views[1] =findViewById(R.id.item_view_1);
        views[2] =findViewById(R.id.item_view_2);
        views[3] =findViewById(R.id.item_view_3);
        views[4] =findViewById(R.id.item_view_4);
        views[5] =findViewById(R.id.item_view_5);

        editText.setCursorVisible(false);//隐藏光标

        for(int i=0;i<1;i++){
            textViews[i].setCursorVisible(true);
        }
        setEditTextListener();
    }

    private void setEditTextListener() {

        editText.addTextChangedListener(new TextWatcher() {
            CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp=charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputContent = editText.getText().toString();

                if (inputCompleteListener != null) {
                    if (inputContent.length() >= MAX) {
                        inputCompleteListener.inputComplete();
                    } else {
                        inputCompleteListener.invalidContent();
                    }
                }

                for (int i = 0; i < MAX; i++) {
                    if (i < inputContent.length()) {
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    } else {
                        textViews[i].setText("");
                    }
                }
                for(int i=0;i<6;i++){

                }
                CharSequence cs;
                switch (temp.length()){
                    case 0:
                        for(int i=0;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        }
                        cs = textViews[0].getText();
                        if(cs != null && cs instanceof Spannable){
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        break;
                    case 1:
                        views[0].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        for(int i=1;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        }
                        cs = textViews[1].getText();
                        if(cs != null && cs instanceof Spannable) {
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        break;
                    case 2:
                        for(int i=0;i<2;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        }
                        for(int i=2;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        }
                        cs = textViews[2].getText();
                        if(cs != null && cs instanceof Spannable) {
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        break;
                    case 3:
                        cs = textViews[3].getText();
                        if(cs != null && cs instanceof Spannable) {
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        for(int i=0;i<3;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        }
                        for(int i=3;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        }
                        break;
                    case 4:
                        cs = textViews[4].getText();
                        if(cs != null && cs instanceof Spannable) {
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        for(int i=0;i<4;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        }
                        for(int i=4;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        }
                        break;
                    case 5:
                        cs = textViews[5].getText();
                        if(cs != null && cs instanceof Spannable) {
                            Selection.setSelection((Spannable) cs, cs.length());
                        }
                        for(int i=0;i<5;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        }
                        views[5].setBackgroundColor(getResources().getColor(R.color.translucent_nodata));
                        break;
                    case 6:
                        cs = textViews[5].getText();
                        if(cs != null && cs instanceof Spannable) {
                            //Selection.setSelection((Spannable) cs, cs.length());
                        }
                        for(int i=0;i<6;i++){
                            views[i].setBackgroundColor(getResources().getColor(R.color.translucent_yesdata));
                        }
                        break;
                }
            }
        });
    }


    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {

        void inputComplete();

        void invalidContent();
    }

    public String getEditContent() {
        return inputContent;
    }

    public EditText getEditText() {
        return editText;
    }
}