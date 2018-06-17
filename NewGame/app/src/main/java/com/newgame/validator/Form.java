package com.newgame.validator;

import android.app.Activity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class Form {

    private List<Field> mFields = new ArrayList<Field>();
    private Activity mActivity;

    public Form(Activity activity) {
        this.mActivity = activity;
    }

    public void addField(Field field) {
        mFields.add(field);
    }

    public boolean isValid() {
        boolean result = true;
        try {
            for (Field field : mFields) {
                result &= field.isValid();
            }
        } catch (FieldValidationException e) {
            result = false;

            EditText textView = e.getTextView();
            textView.requestFocus();
            textView.selectAll();

            FormUtils.showKeyboard(mActivity, textView);

            showErrorMessage(e.getMessage(),textView);
        }
        return result;
    }

    protected void showErrorMessage(String message,    EditText textView) {
       // Crouton.makeText(mActivity, message, Style.ALERT).show();
//    	Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    	textView.setError(message);
    }


}