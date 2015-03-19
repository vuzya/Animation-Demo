package org.vitaliylo.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by vitaliylo on 3/18/15.
 */
public class LayoutTransitionFragment extends android.support.v4.app.Fragment {

    private ViewGroup transtionContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_transitions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transtionContainer = (ViewGroup) view.findViewById(R.id.transition_container);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, 1, 1, "Add item");
//        menu.add(0, 2, 2, "Remove item");
        menu.add(0, 3, 3, "Make transitions awesome");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Button btn = new Button(getActivity());
                btn.setText(btn.toString());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transtionContainer.removeView(v);
                    }
                });
                btn.setCameraDistance(3000);
//                btn.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                transtionContainer.addView(btn, 0, new ViewGroup.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;
            case 3: {
                Toast.makeText(getActivity(), "Transitions are awesome now", Toast.LENGTH_LONG).show();
                LayoutTransition transition = new LayoutTransition();
                transition.enableTransitionType(LayoutTransition.APPEARING |
                        LayoutTransition.DISAPPEARING |
                        LayoutTransition.CHANGING |
                        LayoutTransition.CHANGE_APPEARING |
                        LayoutTransition.CHANGE_DISAPPEARING);

                AnimatorSet appear = new AnimatorSet();
                {
                    ObjectAnimator fall = ObjectAnimator.ofPropertyValuesHolder((Object) null,
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


                    ObjectAnimator shake = ObjectAnimator.ofFloat((View) null, new ParentPropertyWrapper(new Property<View, Float>(float.class, "scale") {
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
                            if (input <= 0.95f || input == 1f) {
                                return 1f;
                            }

                            return (float) Math.random() * .05f + 0.97f;
                        }
                    });
//                    shake.setDuration(100);
//                    shake.setStartDelay(1900);

                    appear.playTogether(fall, shake);
                }


                AnimatorSet disappear = new AnimatorSet();
                {
                    PropertyValuesHolder pvhScrollX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.1f);
                    PropertyValuesHolder pvhScrollY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.1f);
                    ObjectAnimator a1 = ObjectAnimator.ofPropertyValuesHolder((Object) null, pvhScrollY_);
                    ObjectAnimator a2 = ObjectAnimator.ofPropertyValuesHolder((Object) null, pvhScrollX_);

                    a1.setInterpolator(new AnticipateInterpolator());
                    a2.setInterpolator(new AnticipateInterpolator());
                    disappear.playSequentially(a1, a2);
                }


//                ObjectAnimator disappear =
//                        ObjectAnimator.ofPropertyValuesHolder((Object) null,
//                        pvhScrollX, pvhScrollY);


                transition.setAnimator(LayoutTransition.APPEARING, appear);
                transition.setDuration(LayoutTransition.APPEARING, 2000);

                transition.setAnimator(LayoutTransition.DISAPPEARING, disappear);
                transition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 2 * transition.getDuration(LayoutTransition.DISAPPEARING));

                transition.getAnimator(LayoutTransition.CHANGE_APPEARING).setInterpolator(new AnticipateOvershootInterpolator());
                transition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING).setInterpolator(new BounceInterpolator());
                transition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 50);


                transtionContainer.setLayoutTransition(transition);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ParentPropertyWrapper extends Property<View, Float> {

        private final Property<View, Float> prop;

        /**
         * A constructor that takes an identifying name and {@link #getType() type} for the property.
         *
         * @param prop
         */
        public ParentPropertyWrapper(Property<View, Float> prop) {
            super(float.class, prop.getName());
            this.prop = prop;
        }

        @Override
        public Float get(View object) {
            return prop.get(getParent(object));
        }

        @Override
        public void set(View object, Float value) {
            prop.set(getParent(object), value);
        }

        private View getParent(View object) {
            final View parent = (View) object.getParent();
            if (parent != null) {
                return parent;
            } else {
                return object;
            }
        }
    }
}
