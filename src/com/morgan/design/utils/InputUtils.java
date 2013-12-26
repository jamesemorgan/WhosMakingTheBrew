package com.morgan.design.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {

	public static void hideKeyBoard(final View view, final Context context) {
		final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showKeyBoard(final View view, final Context context) {
		final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

}
