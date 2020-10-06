package com.example.grwth.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.grwth.R;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Reward;

import java.util.List;

public class RewardListViewAdapter extends BaseAdapter {
    private List<Reward> reward_list;
    private Context context;
    DatabaseHelper database;

    public RewardListViewAdapter(Context context, List<Reward> rewards, DatabaseHelper database)
    {
        this.reward_list = rewards;
        this.context = context;
        this.database = database;
    }

    @Override
    public int getCount() {
        return reward_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return reward_list.get(position);
    }

    public static class RewardViewHolder
    {
        public TextView rewardTextDescription;
        public EditText rewardEditDescription;
        public Button deleteReward;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        RewardViewHolder holder = null;
        View row = convertView;
        Reward reward = reward_list.get(position);

        if (row != null)
        {
            holder = (RewardViewHolder) row.getTag();
        }
        if (holder == null)
        {
            row = LayoutInflater.from(context).inflate(R.layout.listviewrow_reward, parent, false);

            holder = new RewardViewHolder();
            holder.rewardTextDescription = (TextView)row.findViewById(R.id.txtRewardDescription);
            holder.rewardEditDescription = (EditText)row.findViewById(R.id.editRewardDescription);
            holder.deleteReward = (Button)row.findViewById(R.id.btnDeleteReward);

            final ViewSwitcher switcher = (ViewSwitcher)row.findViewById(R.id.rewardViewSwitcher);

            holder.rewardTextDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switcher.showNext();
                }
            });

            holder.rewardEditDescription.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        switcher.showNext();

                        EditText editText = (EditText)switcher.findViewById(R.id.editRewardDescription);
                        if (editText.getText().toString() == "") {
                            editText.setText("Enter Reward...");
                        }

                        int pos = (int)v.getTag(R.id.editRewardDescription);
                        updateReward(editText.getText().toString(), pos);
                    }
                    return false;
                }
            });

            holder.deleteReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag(R.id.btnDeleteReward);
                    deleteReward(pos);
                }
            });

            row.setTag(holder);
        }

        holder.rewardTextDescription.setTag(R.id.txtRewardDescription, position);
        holder.rewardEditDescription.setTag(R.id.editRewardDescription, position);
        holder.deleteReward.setTag(R.id.btnDeleteReward, position);

        holder.rewardTextDescription.setText(reward.getDescription());
        holder.rewardEditDescription.setText(reward.getDescription());

        return row;
    }

    public void addReward()
    {
        int rewardId;
        if(reward_list.isEmpty()) {
            rewardId = 1;
        } else {
            rewardId = reward_list.get(reward_list.size()-1).getId();
            rewardId += 1;
        }

        Reward newReward = new Reward();
        newReward.setId(rewardId);
        newReward.setDescription("Enter Reward...");

        reward_list.add(newReward);
        boolean dbResult = database.addRewardData(newReward);

        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Inserting Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void updateReward(String text, int position)
    {
        Reward newReward = reward_list.get(position);
        newReward.setDescription(text);

        reward_list.set(position, newReward);

        boolean dbResult = database.updateRewardData(reward_list.get(position), reward_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void deleteReward(int position)
    {

        boolean dbResult = database.deleteRewardData(reward_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Deleting Data!" , Toast.LENGTH_SHORT).show();
        }

        reward_list.remove(position);
        this.notifyDataSetChanged();
    }
}