package org.vitaliylo.animationdemo;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by zIrochka on 21.03.2015.
 */
public class RecyclerViewFragment extends android.support.v4.app.Fragment {

    private RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = (RecyclerView) view;
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if(state.isPreLayout()){
                    //recycler.convertPreLayoutPositionToPostLayout() HERE!!!! HERE HERE HERE!!!
                }
            }
        });
        recycler.getItemAnimator().setAddDuration(1000);
        recycler.getItemAnimator().setChangeDuration(1000);
        recycler.getItemAnimator().setRemoveDuration(1000);
        recycler.getItemAnimator().setMoveDuration(1000);
        recycler.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                Button itemView = new Button(getActivity());
                RecyclerView.LayoutParams params = recycler.getLayoutManager().generateDefaultLayoutParams();
                params.height = 500;
                itemView.setLayoutParams(params);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
                ((Button) viewHolder.itemView).setText(Integer.toString(i));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean b = recycler.getLayoutManager().supportsPredictiveItemAnimations();
                        Toast.makeText(getActivity(), String.valueOf(b), Toast.LENGTH_LONG).show();
                        recycler.getAdapter().notifyItemMoved(i, 20);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
    }
}
