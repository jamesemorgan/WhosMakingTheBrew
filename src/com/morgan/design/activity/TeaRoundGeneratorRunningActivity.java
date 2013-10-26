package com.morgan.design.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.morgan.design.R;

public class TeaRoundGeneratorRunningActivity extends BaseBrewActivity {

	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f *
															// 32.0f;
	private static final int welcomeScreenDisplay = 2000;

	private ImageView teaPot;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running_round);

		findAllViewsById();
		rotateTeaPot();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				setResult(RESULT_OK);
				finish();
			}

		}, welcomeScreenDisplay);
	}

	private void findAllViewsById() {
		teaPot = (ImageView) findViewById(R.id.rotating_tea_pot);
		teaPot.setImageResource(R.drawable.teapot_spinning_icon);
		teaPot.setAdjustViewBounds(true);
		teaPot.setPadding(2, 2, 2, 2);
	}

	private void rotateTeaPot() {
		final RotateAnimation rotation = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotation.setDuration((long) 2 * 1500);
		rotation.setRepeatCount(5);
		teaPot.startAnimation(rotation);
	}

}
