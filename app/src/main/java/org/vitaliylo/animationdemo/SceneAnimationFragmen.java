package org.vitaliylo.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

/**
 * Created by vitaliylo on 3/19/15.
 */
public class SceneAnimationFragmen extends Fragment implements View.OnClickListener {
    private ViewGroup container;
    private Transition transition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene_container, container, false);
        this.container = (ViewGroup) view.findViewById(R.id.transition_container);
        inflater.inflate(R.layout.scene1, this.container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View button = view.findViewById(R.id.animate);
        button.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 3, 3, "Make transitions awesome");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 3: {

                TransitionSet set = new TransitionSet();
                ChangeBounds changeBounds = new ChangeBounds() {
                    @Override
                    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
                        Animator base = super.createAnimator(sceneRoot, startValues, endValues);

                        if (endValues == null || startValues == null || endValues.view == null) {
                            return base;
                        }

                        AnimatorSet set = new AnimatorSet();

//                        Rect epicenter = getEpicenter();
//                        if(epicenter == null){
//                            return base;
//                        }

                        endValues.view.setCameraDistance(3000);

                        Rect startRect = (Rect) startValues.values.get("android:changeBounds:bounds");
                        Rect endRect = (Rect) endValues.values.get("android:changeBounds:bounds");
                        int deltaLeft = endRect.left - startRect.left;
                        int deltaRight = endRect.right - startRect.right;
                        int deltaTop = endRect.top - startRect.top;
                        int deltaBottom = endRect.bottom - startRect.bottom;

                        float rotationXdest = 80f;
                        float rotationYdest = 80f;
                        if (deltaLeft * deltaRight > 0) {
                            float dist = (deltaLeft + deltaRight) / 2f;
                            rotationYdest = dist / sceneRoot.getWidth() * 90f;
                        }
                        if (deltaTop * deltaBottom > 0) {
                            float dist = (deltaTop + deltaBottom) / 2f;
                            rotationXdest = dist / sceneRoot.getHeight() * 90f;
                        }
                        AnimatorSet as = new AnimatorSet();
                        ObjectAnimator rotateTo = ObjectAnimator.ofPropertyValuesHolder(endValues.view,
                                PropertyValuesHolder.ofFloat("translationZ", 0f, 200f, 0f),
                                PropertyValuesHolder.ofFloat("rotationX", 0f, rotationXdest),
                                PropertyValuesHolder.ofFloat("rotationY", 0f, rotationYdest)
                        );
//                        rotateTo.setDuration(base.getDuration());
                        rotateTo.setInterpolator(new AnticipateInterpolator());
                        ObjectAnimator bounce = ObjectAnimator.ofPropertyValuesHolder(endValues.view,
                                PropertyValuesHolder.ofFloat("rotationX", rotationXdest, 0),
                                PropertyValuesHolder.ofFloat("rotationY", rotationYdest, 0)
                        );
                        bounce.setDuration(500);
                        bounce.setInterpolator(new BounceInterpolator());
                        as.playSequentially(rotateTo, bounce);
                        set.playTogether(base, as);

                        return set;
                    }
                };
//                changeBounds.setDuration(500);
                set.addTransition(changeBounds);
                Fade fade = new Fade(Fade.IN) {
                    @Override
                    public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
                        if ((getMode() & MODE_IN) != MODE_IN || endValues == null) {
                            return null;
                        }

                        AnimatorSet appear = new AnimatorSet();
                        {
                            ObjectAnimator fall = ObjectAnimator.ofPropertyValuesHolder(endValues.view,
                                    PropertyValuesHolder.ofFloat("alpha", 0f, 1f, 1f, 1f, 1f), // more points to finish faster
                                    PropertyValuesHolder.ofFloat("scaleX", 1.25f, 1f),
                                    PropertyValuesHolder.ofFloat("scaleY", 1.25f, 1f),
                                    PropertyValuesHolder.ofFloat("translationZ", 500f, 0f),
                                    PropertyValuesHolder.ofFloat("translationY", -150f, 0f),
//                            PropertyValuesHolder.ofFloat("cameraDistance", 500f, 0f),
                                    PropertyValuesHolder.ofFloat("rotationY", 45f, -45f, 15f, 0f),
                                    PropertyValuesHolder.ofFloat("rotationX", -45f, 55f, -15f, 0f)
                            );
                            fall.setInterpolator(new AccelerateInterpolator());
                            fall.setDuration(500);

                            ObjectAnimator shake = ObjectAnimator.ofFloat(endValues.view, new LayoutTransitionFragment.ParentPropertyWrapper(new Property<View, Float>(float.class, "scale") {
                                @Override
                                public Float get(View object) {
                                    return object.getScaleX();
                                }

                                @Override
                                public void set(View object, Float value) {
                                    object.setScaleX(value);
                                    object.setScaleY(value);
                                }
                            }), 0f, 1f);
                            shake.setInterpolator(new TimeInterpolator() {

                                @Override
                                public float getInterpolation(float input) {
                                    if (input == 0f || input == 1f) {
                                        return 1f;
                                    }

                                    return (float) Math.random() * .05f + 0.97f;
                                }
                            });
                            shake.setDuration(100);

                            appear.playSequentially(fall, shake);
                        }
                        return appear;
                    }
                };
                set.addTransition(fade);

                this.transition = set;


                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    int sceneIndex;
    int[] scenes = new int[]{
            R.layout.scene1,
            R.layout.scene2,
            R.layout.scene3
    };

    @Override
    public void onClick(View v) {
        sceneIndex = (sceneIndex + 1) % scenes.length;
//        Scene scene1 = new Scene(container);
        Scene scene = Scene.getSceneForLayout(container, scenes[sceneIndex], getActivity());
        if (transition == null) {
            TransitionManager.go(scene);
        } else {
            TransitionManager.go(scene, transition);
        }
    }
}
