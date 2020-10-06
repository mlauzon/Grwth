package com.example.grwth.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Skill;
import com.example.grwth.model.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.grwth.R;

public class TaskListViewAdapter extends BaseAdapter
{
    private String checklist_name;
    private List<Task> task_list;
    private Context context;
    DatabaseHelper database;

    public TaskListViewAdapter(Context context, String checklist, List<Task> tasks, DatabaseHelper database)
    {
        this.checklist_name = checklist;
        this.task_list = tasks;
        this.context = context;
        this.database = database;
    }

    @Override
    public int getCount() {
        return task_list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return task_list.get(position);
    }

    public static class TaskViewHolder
    {
        public CheckBox taskCheckBox;
        public TextView taskDescription;
        public EditText editDescription;
        public Button addTags;
        public Button deleteTask;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        TaskViewHolder holder = null;
        View row = convertView;

        if (row != null)
        {
            holder = (TaskViewHolder)row.getTag();
        }
        if (holder == null)
        {
            row = LayoutInflater.from(context).inflate(R.layout.listviewrow_task, parent, false);

            holder = new TaskViewHolder();
            holder.taskCheckBox = (CheckBox)row.findViewById(R.id.checkbox);
            holder.taskDescription = (TextView)row.findViewById(R.id.txtTaskDescription);
            holder.editDescription = (EditText)row.findViewById(R.id.editTaskDescription);
            holder.addTags = (Button)row.findViewById(R.id.btnAddTag);
            holder.deleteTask = (Button)row.findViewById(R.id.btnDeleteTask);

            final ViewSwitcher switcher = (ViewSwitcher)row.findViewById(R.id.taskTxtSwitcher);

            holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int)v.getTag(R.id.checkbox);
                    CheckBox checkbox = (CheckBox)v.findViewById(R.id.checkbox);
                    if (checkbox.isChecked()) {
                        updateCheckbox(true, pos);
                    } else {
                        updateCheckbox(false, pos);
                    }

                }
            });

            holder.taskDescription.setOnClickListener(new View.OnClickListener() {
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

                        EditText editText = (EditText)switcher.findViewById(R.id.editTaskDescription);
                        if (editText.getText().toString() == "") {
                            editText.setText("Enter Task...");
                        }

                        int pos = (int)v.getTag(R.id.editTaskDescription);
                        updateTask(editText.getText().toString(), pos);
                    }
                    return false;
                }
            });

            holder.addTags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = (int)v.getTag(R.id.btnAddTag);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    final List<String> skillList = getSkillTags();
                    CharSequence[] menuSequence = skillList.toArray(new CharSequence[skillList.size()]);

                    final ArrayList<Integer> selectedItems = new ArrayList<Integer>();

                    final boolean[] previouslySelected = getPrevSelectedSkills(skillList, getTaskSkillsTags(pos));

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

            holder.deleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);

                    int pos = (int)v.getTag(R.id.btnDeleteTask);
                    deleteTask(pos);
                }
            });

            row.setTag(holder);
        }

        holder.taskCheckBox.setTag(R.id.checkbox, position);
        holder.taskDescription.setTag(R.id.txtTaskDescription, position);
        holder.editDescription.setTag(R.id.editTaskDescription, position);
        holder.addTags.setTag(R.id.btnAddTag, position);
        holder.deleteTask.setTag(R.id.btnDeleteTask, position);

        holder.taskCheckBox.setChecked(task_list.get(position).getStatus());
        holder.taskDescription.setText(task_list.get(position).getDescription());
        holder.editDescription.setText(task_list.get(position).getDescription());

        return row;
    }

    public void addTask()
    {
        int taskId;
        if(task_list.isEmpty()) {
            taskId = 1;
        } else {
            taskId = task_list.get(task_list.size()-1).getId();
            taskId += 1;
        }
        Task newTask = new Task();
        newTask.setId(taskId);
        newTask.setChecklist(checklist_name);
        newTask.setDescription("Enter Task...");
        newTask.setSkills("");
        newTask.setStatus(false);

        task_list.add(newTask);
        boolean dbResult = database.addTaskData(newTask);

        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Inserting Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void updateCheckbox(Boolean status, int position)
    {
        Task newTask = task_list.get(position);
        newTask.setStatus(status);

        task_list.set(position, newTask);

        boolean dbResult = database.updateTaskData(task_list.get(position), task_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void updateSkillTags(ArrayList<Integer> list, List<String> skillList, int position) {
        String skillTag = "";
        for(Integer i : list) {
            skillTag += skillList.get(i);
            skillTag += " ";
        }

        Task newTask = task_list.get(position);
        newTask.setSkills(skillTag);

        task_list.set(position, newTask);
        boolean dbResult = database.updateTaskData(task_list.get(position), task_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void updateTask(String text, int position)
    {
        Task newTask = task_list.get(position);
        newTask.setDescription(text);

        task_list.set(position, newTask);

        boolean dbResult = database.updateTaskData(task_list.get(position), task_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Updating Data!" , Toast.LENGTH_SHORT).show();
        }

        this.notifyDataSetChanged();
    }

    public void deleteTask(int position)
    {

        boolean dbResult = database.deleteTaskData(task_list.get(position).getId());
        if (!dbResult) {
            Toast.makeText(context, "[DEBUG]: Error Deleting Data!" , Toast.LENGTH_SHORT).show();
        }

        task_list.remove(position);
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

    private String getTaskSkillsTags(int position) {
        int taskId = task_list.get(position).getId();
        String query = "SELECT Skills FROM tasks WHERE ID = " + Integer.toString(taskId);
        Cursor data = database.queryTable(query);
        data.moveToFirst();
        return data.getString(0);
    }

    private boolean[] getPrevSelectedSkills(List<String> allSkills, String taskSkillTags) {
        boolean[] previousSelection = new boolean[allSkills.size()];

        if (taskSkillTags.isEmpty()) {
            // No skills were tagged previously
            for (int i = 0; i < allSkills.size(); i++) {
                previousSelection[i] = false;
            }
        } else {
            // Skills were tagged previously
            String[] taggedSkills = taskSkillTags.split(" ");

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