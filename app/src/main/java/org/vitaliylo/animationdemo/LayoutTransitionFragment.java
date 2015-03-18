package org.vitaliylo.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

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
                transtionContainer.addView(btn, 0);
                break;
            case 3: {
                Toast.makeText(getActivity(), "Transitions are awesome now", Toast.LENGTH_LONG).show();
                LayoutTransition transition = new LayoutTransition();
                transition.enableTransitionType(LayoutTransition.APPEARING |
                        LayoutTransition.DISAPPEARING |
                        LayoutTransition.CHANGING |
                        LayoutTransition.CHANGE_APPEARING |
                        LayoutTransition.CHANGE_DISAPPEARING);

                PropertyValuesHolder pvhScrollX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
                PropertyValuesHolder pvhScrollY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
                Animator appear = ObjectAnimator.ofPropertyValuesHolder((Object) null, pvhScrollX, pvhScrollY);
                appear.setInterpolator(new OvershootInterpolator());

                {

                }


                /*AnimatorSet disappear = new AnimatorSet();
                {
                    PropertyValuesHolder pvhScrollX_ = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.1f);
                    PropertyValuesHolder pvhScrollY_ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.1f);
                    ObjectAnimator a1 = ObjectAnimator.ofPropertyValuesHolder((Object) null, pvhScrollY_);
                    ObjectAnimator a2 = ObjectAnimator.ofPropertyValuesHolder((Object) null, pvhScrollX_);

                    a1.setInterpolator(new AnticipateInterpolator());
                    a2.setInterpolator(new AnticipateInterpolator());
                    disappear.playSequentially(a1, a2);
                }*/


                ObjectAnimator disappear =
                        ObjectAnimator.ofPropertyValuesHolder((Object) null,
                        pvhScrollX, pvhScrollY);


                transition.setAnimator(LayoutTransition.APPEARING, appear);

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
         * @param type
         * @param name
         */
        public ParentPropertyWrapper(Class<Float> type, String name, Property<View, Float> prop) {
            super(type, name);
            this.prop = prop;
        }

        @Override
        public Float get(View object) {
            return null;
        }

        @Override
        public void set(View object, Float value) {
            super.set(object, value);
        }
    }
}
