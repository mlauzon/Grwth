package com.example.grwth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grwth.R;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Skill;

import java.util.List;

public class StatsGridViewAdapter extends BaseAdapter {
    private final Context context;
    private List<Skill> skillLists;
    private DatabaseHelper database;

    public StatsGridViewAdapter(Context context, List<Skill> skills, DatabaseHelper db) {
        this.context = context;
        this.skillLists = skills;
        this.database = db;
    }

    @Override
    public int getCount() { return skillLists.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public Object getItem(int position) { return skillLists.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Skill skill = skillLists.get(position);

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.gridview_stats, parent, false);
        }

        ProgressBar skillProgress = (ProgressBar)row.findViewById(R.id.skillCircleProgressBar);
        TextView skillName = (TextView)row.findViewById(R.id.skillCircleProgressName);

        skillName.setText(skill.getDescription());
        skillProgress.setProgress(skill.getLevel());

        return row;
    }

}
