package utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MultEditListenerUtil {

    public interface IMultEditListener{
        void textChange(boolean isChanged);
    }

    static IMultEditListener iMultEditListener;

    public static void setMultEditListener(IMultEditListener mIMultEditListener){
        iMultEditListener=mIMultEditListener;
    }


    public static class MultEditListener{
        private EditText[] editTexts;

        public MultEditListener addEditTexts(EditText ...editTexts){
            this.editTexts=editTexts;
            setOnEditListener();
            return this;
        }

        private void setOnEditListener(){
            for (EditText editText:editTexts){
                editText.addTextChangedListener(new TextChange());
            }
        }

        private class TextChange implements TextWatcher{

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isAllFull()){
                    if (iMultEditListener!=null){
                        iMultEditListener.textChange(true);
                    }
                }else {
                    if (iMultEditListener!=null){
                        iMultEditListener.textChange(false);
                    }
                }
            }

            private boolean isAllFull(){
                for (EditText editText:editTexts){
                    if (editText.getText().toString().length()==0){
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
