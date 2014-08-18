package com.itech.bookagoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Artem on 10.08.14.
 */
public class WallFragment2 extends Fragment implements MainActivity.IContentFragment, View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wall2, container, false);
        assert rootView != null;
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.fragmentWall_RecyclerView_wall);
        String itemsData[] = {"1", "2", "3", "4", "5", "6", "7"};

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter mAdapter = new Adapter(itemsData);
        rv.setAdapter(mAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public int getIdTitle() {
        return R.string.title_wall;
    }

    @Override
    public String getNameTitle() {
        return App.getContext().getString(R.string.title_wall);
    }

    @Override
    public int getIdIco() {
        return R.drawable.ic_menu1;
    }

    @Override
    public int getIdIcoTop() {
        return R.drawable.ic_menu1_tap;
    }

    @Override
    public String getUrlIco() {
        return null;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private String[] itemsData;

        public Adapter(String[] itemsData) {
            this.itemsData = itemsData;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, null);

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            viewHolder.txt.setText(itemsData[position]);
            viewHolder.txt.setMinHeight(100 * position);

        }

        // inner class to hold a reference to each item of RecyclerView
        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                txt = (TextView) itemLayoutView.findViewById(R.id.txt);
            }
        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return itemsData.length;
        }
    }
}
