package com.flyco.animation.FadeExit;

import android.view.View;

import com.flyco.animation.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class FadeExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration));
	}
}
