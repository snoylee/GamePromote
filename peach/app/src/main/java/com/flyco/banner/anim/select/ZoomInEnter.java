package com.flyco.banner.anim.select;

import android.view.View;

import com.flyco.banner.anim.BaseAnimator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class ZoomInEnter extends BaseAnimator {
    public ZoomInEnter() {
        this.duration = 200;
    }

    public void setAnimation(View view) {
        this.animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.0F, 1.5F),
                ObjectAnimator.ofFloat(view, "scaleY", 1.0F, 1.5F));
    }
}
