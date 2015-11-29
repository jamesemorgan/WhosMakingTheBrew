package com.morgan.design.ui;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class DefaultEditTextOnFocusListener implements OnFocusChangeListener {
    private static final String EMPTY_STRING = "";

    private final String defaultText;

    public DefaultEditTextOnFocusListener(final String defaultText) {
        this.defaultText = defaultText;
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (view instanceof EditText) {
            final EditText focusedEditText = (EditText) view;
            // handle obtaining focus
            if (hasFocus) {
                if (focusedEditText.getText().toString().equals(this.defaultText)) {
                    focusedEditText.setText(EMPTY_STRING);
                }
            }
            // handle losing focus
            else {
                if (focusedEditText.getText().toString().equals(EMPTY_STRING)) {
                    focusedEditText.setText(this.defaultText);
                }
            }
        }
    }

}
