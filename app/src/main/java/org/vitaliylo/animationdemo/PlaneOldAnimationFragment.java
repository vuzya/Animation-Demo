package org.vitaliylo.animationdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by vitaliylo on 3/18/15.
 */
public class PlaneOldAnimationFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plane_old_animation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup vg = (ViewGroup) view;
        for (int i = 0; i < vg.getChildCount(); i++) {
            vg.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Animation animation = null;
        switch (v.getId()) {
            case R.id.button1:
                animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(1000);
                break;
            case R.id.button2:
                animation = new RotateAnimation(0f, 360f, 0.2f, 0.2f);
                animation.setInterpolator(new AnticipateOvershootInterpolator());
                animation.setDuration(1000);
                break;
            case R.id.button3:
                animation = new ScaleAnimation(.2f, 1f, .2f, 1f);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setDuration(1000);
                break;
            case R.id.button4:
                animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -2f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                animation.setInterpolator(new BounceInterpolator());
                animation.setDuration(1000);
                break;
            case R.id.button5:
                AnimationSet as = new AnimationSet(false);
                animation = as;

                AlphaAnimation a1 = new AlphaAnimation(0f, 1f);
                a1.setDuration(200);
                as.addAnimation(a1);

                RotateAnimation a2 = new RotateAnimation(0f, 360f, 0.2f, 0.2f);
                a2.setDuration(1000);
                a2.setInterpolator(new BounceInterpolator());
                as.addAnimation(a2);

                TranslateAnimation a3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 3f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f);
                a3.setInterpolator(new AnticipateOvershootInterpolator());
                a3.setDuration(1000);
                a3.setStartOffset(1200);
                as.addAnimation(a3);

                break;
            case R.id.button6:
                break;
        }
        v.startAnimation(animation);
    }
}
