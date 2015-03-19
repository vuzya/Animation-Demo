package org.vitaliylo.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.PatternPathMotion;
import android.util.FloatMath;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zIrochka on 14.03.2015.
 */
public class PropertyAnimatorFragment extends Fragment {
    List<View> images = new ArrayList<View>();
    private FrameLayout canvas;
    private View animate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_property_animator, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.canvas = (FrameLayout) view.findViewById(R.id.canvas);

        for (int i = 0; i < 10; i++) {
            ImageView iv;
            images.add(iv = new ImageView(getActivity()));
            iv.setImageResource(R.drawable.ic_launcher);
            canvas.addView(iv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        this.animate = view.findViewById(R.id.animate);
        animate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
//        PatternPathMotion ppm = new PatternPathMotion(mPatternPath);

        List<Animator> animators = new ArrayList<Animator>(images.size());
        AnimatorSet set = new AnimatorSet();

        int endX = canvas.getWidth() / 2;
        int endY = canvas.getHeight() / 2;
        long delay = 200;

        for (View view : images) {
            Path path = getPath(0, 0, endX, endY);
//            PropertyValuesHolder.ofObject(POSITION_PROPERTY, null, path);
//            ObjectAnimator animator = ObjectAnimator.ofObject(view, POSITION_PROPERTY, null, path);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofObject(POSITION_PROPERTY, null, path),
                    PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f),
                    PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f),
                    PropertyValuesHolder.ofFloat("rotationY", 45f, -45f, 15f, 0f),
                    PropertyValuesHolder.ofFloat("rotationX", -45f, 55f, -15f, 0f));
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(400);
            animator.setStartDelay(delay);

//            animators.add(animator);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(view,
                    PropertyValuesHolder.ofFloat("alpha", 1f, 0f),
                    PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f),
                    PropertyValuesHolder.ofFloat("scaleY", 1f, 1.5f)
            );
            animator1.setDuration(100);
            animatorSet.playSequentially(animator, animator1);
            animators.add(animatorSet);
            delay+=animator.getDuration();
        }

        set.playTogether(animators);
        set.start();
    }

    public Path getPath(float startX, float startY, float endX, float endY) {
        Path mPatternPath = new Path();
//        mPatternPath.lineTo(1f, 1f);
        mPatternPath.cubicTo(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), 1f, 1f);
//        mPatternPath.cubicTo(.96f, .17f, .1f, .84f, 1f, 1f);

        float dx = endX - startX;
        float dy = endY - startY;
//        float length = distance(dx, dy);
        double angle = Math.atan2(dy, dx);

        float scale = Math.min(dx, dy);
//        mTempMatrix.setScale(scale, scale);
        mTempMatrix.setScale(dx, dy);
//        mTempMatrix.postRotate((float) Math.toDegrees(angle));
        mTempMatrix.postTranslate(startX, startY);
        Path path = new Path();
        mPatternPath.transform(mTempMatrix, path);
        return path;
    }

    private static final Property<View, PointF> POSITION_PROPERTY =
            new Property<View, PointF>(PointF.class, "position") {
                @Override
                public void set(View view, PointF topLeft) {
//                    int left = Math.round(topLeft.x);
//                    int top = Math.round(topLeft.y);
//                    int right = left + view.getWidth();
//                    int bottom = top + view.getHeight();
//                    view.setLeft(left);
//                    view.setTop(top);
//                    view.setRight(right);
//                    view.setBottom(bottom);
                    Log.i("POSITION", "x"+topLeft.x+" y"+topLeft.y);
                    view.setTranslationX(topLeft.x);
                    view.setTranslationY(topLeft.y);
                }

                @Override
                public PointF get(View view) {
                    return null;
                }
            };

//    public static class ViewMover {
//        private final View target;
//
//        public ViewMover(View target){
//            this.target = target;
//        }
//
//        void moveTo(PathPoint)
//    }

    private final Random r = new Random();
    private final Matrix mTempMatrix = new Matrix();

    private static float distance(float x, float y) {
        return FloatMath.sqrt((x * x) + (y * y));
    }
}
