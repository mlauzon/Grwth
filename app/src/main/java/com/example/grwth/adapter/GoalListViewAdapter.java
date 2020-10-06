package com.example.grwth.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grwth.R;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Goal;
import com.example.grwth.model.Reward;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GoalListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Goal> goal_list;
    DatabaseHelper database;

    public GoalListViewAdapter(Context context, List<Goal> goals, DatabaseHelper databaseHelper) {
        this.context = context;
        this.goal_list = goals;
        this.database = databaseHelper;
    }

    @Override
    public int getCount() {
        return goal_list.size();
    }

    @Override
    public Object getItem(int position) {
        return goal_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class GoalViewHolder
    {
        public TextView goalStatus;
        public Button goalSkills;
        public TextView goalDate;
        public Spinner goalReward;
        public Button deleteGoal;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent)
    {
        GoalViewHolder holder = null;
        View row = convertView;
        final List<Reward> rewardList = getRewards();

        if (row != null)
        {
            holder = (GoalViewHolder)row.getTag();
        }
        if (holder == null)
        {
            row = LayoutInflater.from(context).inflate(R.layout.listviewrow_goal, parent, false);

            holder = new GoalViewHolder();
            holder.goalStatus = (TextView)row.findViewById(R.id.textGoalStatus);
            holder.goalSkills = (Button)row.findViewById(R.id.btnChooseSkills);
            holder.goalDate = (TextView)row.findViewById(R.id.txtSetGoalDate);
            holder.goalReward = (Spinner)row.findViewById(R.id.spinnerGoalReward);
            holder.deleteGoal = (Button)row.findViewById(R.id.btnDeleteGoal);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getRewardDescriptions(rewardList));
            holder.goalReward.setAdapter(adapter);

//            updateStatus(position);
//            holder.goalStatus.setText(getStatus(position));

            // Select Skills for each Goal Item in ListView
            holder.goalSkills.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = (int)v.getTag(R.id.btnChooseSkills);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    final List<String> skillList = getSkillTags();
                    CharSequence[] menuSequence = skillList.toArray(new CharSequence[skillList.size()]);

                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

                    final boolean[] previouslySelected = getPrevSelectedSkills(skillList, getGoalSkillTags(pos));
                    builder.setMultiChoiceItems(menuSequence, previouslySelected, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // [TODO]: fix bug where checked items do not stay checked
                            if (isChecked) {
                                selectedItems.add(which);
                            } else if (selectedItems.contains(which)) {
                                selectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateSkillTags(selectedItems, skillList, pos);
                        }
                    });

                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            // Select Goal Date for each Goal Item in ListVieW
            holder.goalDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = (int)v.getTag(R.id.txtSetGoalDate);
                    final TextView dateText = (TextView) v.findViewById(R.id.txtSetGoalDate);

                    final Calendar myCalendar = Calendar.getInstance();
                    int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                    int month = myCalendar.get(Calendar.MONTH);
                    int year = myCalendar.get(Calendar.YEAR);

                    String myFormat = "MM/dd/yy"; //In which you need put here
                    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    dateText.setText(sdf.format(myCalendar.getTime()));

                    DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, month);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            dateText.setText(sdf.format(myCalendar.getTime()));
                            updateGoalDate(myCalendar, pos);
                        }
                    }, year, month, day);
                    datePicker.show();
                }
            });

            // Get Selected Reward from Spinner in ListView
//            holder.goalReward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                    String rewardDescription = parent.getItemAtPosition(pos).toString();
//                    System.out.println(rewardDescription);
//                    updateGoalReward(rewardDescription, position);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    // Do nothing
//                }
//            });

            // Delete Goal Item in ListView
            holder.deleteGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag(R.id.btnDeleteGoal);
                    deleteGoal(pos);
                }
            });

            row.setTag(holder);
        }

        holder.goalStatus.setTag(R.id.textGoalStatus, position);
        holder.goalSkills.setTag(R.id.btnChooseSkills, position);
        holder.goalDate.setTag(R.id.txtSetGoalDate, position);
        holder.goalReward.setTag(R.id.spinnerGoalReward, position);
        holder.deleteGoal.setTag(R.id.btnDeleteGoal, position);

        holder.goalStatus.setText(getStatus(position));
        holder.goalDate.setText(goal_list.get(position).getGoalDate());
        holder.goalReward.setSelection(goal_list.get(position).getRewardId());

        return row;
    }

    public void addGoal()
    {
        int goalId;
        if(goal_list.isEmpty()) {
            goalId = 1;
        } else {
            goalId = goal_list.get(goal_list.size()-1).getId();
            goalId += 1;
        }

        Goal newGoal = new Goal();
        newGoal.setId(goalId);
        newGoal.setStartDate("");
        newGoal.setCompletionDate("In Progress");
        newGoal.setGoalDate("");
        newGoal.setSkills("");
        newGoal.setRewardId(0);

        goal_list.add(newGoal);
        boolean dbResult = database.addGoalData(newGoal);

        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Inserting Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }
    private String getStatus(int position) {
        // Get Current Date
        Calendar currentDate = Calendar.getInstance();
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        Goal newGoal = goal_list.get(position);

        if(newGoal.getGoalDate().isEmpty() || sdf.format(currentDate.getTime()).compareTo(newGoal.getGoalDate()) < 0) {
            return "In Progress";
        }

        return "Completed";
    }


    private void updateStatus(int position) {
        // Get Current Date
        Calendar currentDate = Calendar.getInstance();
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        Goal newGoal = goal_list.get(position);

        // If Goal Date does not match current date, return;
        if(newGoal.getGoalDate().contains(sdf.format(currentDate.getTime()))) {
            System.out.println("DEBUG: " + newGoal.getGoalDate() + "!="+  sdf.format(currentDate.getTime()));
            return;
        } else {
            System.out.println("DEBUG: " + newGoal.getGoalDate() + "=="+  sdf.format(currentDate.getTime()));
            newGoal.setCompletionDate("Completed " + sdf.format(currentDate.getTime()));
        }

        goal_list.set(position, newGoal);
        boolean dbResult = database.updateGoalData(goal_list.get(position), goal_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }
        database.printGoalTable();
        this.notifyDataSetChanged();
    }


    private void updateSkillTags(ArrayList<Integer> list, List<String> skillList, int position) {
        String skillTag = "";
        for(Integer i : list) {
            skillTag += skillList.get(i);
            skillTag += " ";
        }

        Goal newGoal = goal_list.get(position);
        newGoal.setSkills(skillTag);

        goal_list.set(position, newGoal);
        boolean dbResult = database.updateGoalData(goal_list.get(position), goal_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }
        database.printGoalTable();
        this.notifyDataSetChanged();
    }

    private void updateGoalDate(Calendar calendar, int position)
    {
        Goal newGoal = goal_list.get(position);
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        newGoal.setGoalDate(sdf.format(calendar.getTime()));

        goal_list.set(position, newGoal);
        boolean dbResult = database.updateGoalData(goal_list.get(position), goal_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }
        database.printGoalTable();
        this.notifyDataSetChanged();
    }

    private void updateGoalReward(String rewardDescription, int position) {
        Goal newGoal = new Goal();
        List<Reward> rewardList = getRewards();
        for (Reward reward: rewardList) {
            if (reward.getDescription().contains(rewardDescription)) {
                newGoal.setId(reward.getId());
                break;
            }
        }

        goal_list.set(position, newGoal);

        boolean dbResult = database.updateGoalData(goal_list.get(position), goal_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }
        database.printGoalTable();
        this.notifyDataSetChanged();
    }

    private void deleteGoal(int position)
    {
        boolean dbResult = database.deleteGoalData(goal_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Deleting Data!" , Toast.LENGTH_SHORT).show();
        }

        goal_list.remove(position);
        this.notifyDataSetChanged();
    }

    private List<String> getSkillTags() {
        Cursor data = database.getSkillData();
        List<String> skillList = new ArrayList<>();

        while(data.moveToNext()) {
            skillList.add(data.getString(1));
        }

        return skillList;
    }

    private List<Reward> getRewards() {
        Cursor data = database.getRewardData();
        List<Reward> rewardList = new ArrayList<>();
        while(data.moveToNext()) {
            Reward reward = new Reward();

            reward.setId(data.getInt(0));
            reward.setDescription(data.getString(1));

            rewardList.add(reward);
        }

        return rewardList;
    }

    private List<String> getRewardDescriptions(List<Reward> rewardList) {
        List<String> rewardDescriptions = new ArrayList<>();
        for(Reward reward : rewardList) {
            rewardDescriptions.add(reward.getDescription());
        }

        return rewardDescriptions;
    }

    private String getGoalSkillTags(int position) {
        int goalId = goal_list.get(position).getId();
        String query = "SELECT Skills FROM goals WHERE ID = " + Integer.toString(goalId);
        Cursor data = database.queryTable(query);
        data.moveToFirst();
        return data.getString(0);
    }

    private boolean[] getPrevSelectedSkills(List<String> allSkills, String goalSkillTags) {
        boolean[] previousSelection = new boolean[allSkills.size()];

        if (goalSkillTags.isEmpty()) {
            // No skills were tagged previously
            for (int i = 0; i < allSkills.size(); i++) {
                previousSelection[i] = false;
            }
        } else {
            // Skills were tagged previously
            String[] taggedSkills = goalSkillTags.split(" ");

            for (int i = 0; i < allSkills.size(); i++) {
                for (String skill : taggedSkills) {
                    if (allSkills.get(i).contains(skill)) {
                        previousSelection[i] = true;
                        break;
                    } else {
                        previousSelection[i] = false;
                    }
                }
            }
        }

        return previousSelection;
    }
}
