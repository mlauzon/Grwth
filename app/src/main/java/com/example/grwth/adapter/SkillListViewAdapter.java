package com.example.grwth.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Skill;

import java.util.ArrayList;
import java.util.List;
import com.example.grwth.R;

public class SkillListViewAdapter extends BaseAdapter
{
    private List<Skill> skill_list;
    private Context context;
    DatabaseHelper database;

    public SkillListViewAdapter(Context context, List<Skill> skills, DatabaseHelper database)
    {
        this.skill_list = skills;
        this.context = context;
        this.database = database;
    }

    @Override
    public int getCount() {
        return skill_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return skill_list.get(position);
    }

    public static class SkillViewHolder
    {
        public TextView skillDescription;
        public EditText editDescription;
        public ProgressBar skillLevel;
        public Button deleteSkill;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        SkillViewHolder holder = null;
        View row = convertView;
        Skill skill = skill_list.get(position);

        if (row != null)
        {
            holder = (SkillViewHolder)row.getTag();
        }
        if (holder == null)
        {
            row = LayoutInflater.from(context).inflate(R.layout.listviewrow_skills, parent, false);

            holder = new SkillViewHolder();
            holder.skillDescription = (TextView)row.findViewById(R.id.txtSkillDescription);
            holder.editDescription = (EditText)row.findViewById(R.id.editSkillDescription);
            holder.skillLevel = (ProgressBar)row.findViewById(R.id.skillHorizontalProgressBar);
            holder.deleteSkill = (Button)row.findViewById(R.id.btnDeleteSkill);

            final ViewSwitcher switcher = (ViewSwitcher)row.findViewById(R.id.skillViewSwitcher);

            holder.skillDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switcher.showNext();
                }
            });

            holder.editDescription.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        switcher.showNext();

                        EditText editText = (EditText)switcher.findViewById(R.id.editSkillDescription);
                        if (editText.getText().toString() == "") {
                            editText.setText("Enter Skill...");
                        }

                        int pos = (int)v.getTag(R.id.editSkillDescription);
                        updateSkill(editText.getText().toString(), pos);
                    }
                    return false;
                }
            });

            holder.deleteSkill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag(R.id.btnDeleteSkill);
                    deleteSkill(pos);
                }
            });

            row.setTag(holder);
        }

        holder.skillDescription.setTag(R.id.txtSkillDescription, position);
        holder.editDescription.setTag(R.id.editSkillDescription, position);
        holder.skillLevel.setTag(R.id.skillHorizontalProgressBar, position);
        holder.deleteSkill.setTag(R.id.btnDeleteSkill, position);

        holder.skillLevel.setProgress(skill.getLevel());
        holder.skillDescription.setText(skill.getDescription());
        holder.editDescription.setText(skill.getDescription());

        return row;
    }

    public void addSkill()
    {
        int skillId;
        if(skill_list.isEmpty()) {
            skillId = 1;
        } else {
            skillId = skill_list.get(skill_list.size()-1).getId();
            skillId += 1;
        }

        Skill newSkill = new Skill();
        newSkill.setId(skillId);
        newSkill.setDescription("Enter Skill...");
        newSkill.setLevel(0);

        skill_list.add(newSkill);
        boolean dbResult = database.addSkillData(newSkill);

        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Inserting Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void updateSkill(String text, int position)
    {
        Skill newSkill = skill_list.get(position);
        newSkill.setDescription(text);

        skill_list.set(position, newSkill);

        boolean dbResult = database.updateSkillData(skill_list.get(position), skill_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void deleteSkill(int position)
    {

        boolean dbResult = database.deleteSkillData(skill_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Deleting Data!" , Toast.LENGTH_SHORT).show();
        }

        skill_list.remove(position);
        this.notifyDataSetChanged();
    }
}