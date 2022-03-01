package com.example.familymapclient.Activities.Utility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymapclient.Models.DataCache;
import com.example.familymapclient.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private ArrayList<String> filterTitles = new ArrayList<>();
    private ArrayList<String> filterKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        filterKeys = new ArrayList<>(DataCache.getInstance().getFilterTitles());
        for (String str : filterKeys) {
            str = StringUtils.capitalize(str) + " Events";
            filterTitles.add(str);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.filter_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(filterTitles, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private ArrayList<String> filterNames;
        private Context context;

        public RecyclerViewAdapter(ArrayList<String> filterNames, Context context) {
            this.filterNames = filterNames;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_filter_list_item, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            viewHolder.filterTitle.setText(filterNames.get(i));
            viewHolder.filterSwitch.setChecked(DataCache.getInstance().getFilter(filterKeys.get(i)));
            viewHolder.filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setFilter(filterKeys.get(i), isChecked);
                    dataCache.setClickedMarker(null);
                    dataCache.setMasterClickedMarker(null);
                }
            });
        }

        @Override
        public int getItemCount() {
            return filterNames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView filterTitle;
            Switch filterSwitch;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                filterTitle = itemView.findViewById(R.id.filter_text);
                filterSwitch = itemView.findViewById(R.id.filter_switch);

            }
        }
    }

}

