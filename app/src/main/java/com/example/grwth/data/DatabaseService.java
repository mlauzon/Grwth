package com.example.grwth.data;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.grwth.model.Skill;
import com.example.grwth.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseService extends Service {
    final private String TAG = "DatabaseService";
    final private int LEVEL_INCREMENT_VAUE = 10;
    private boolean dbResult = false;
    private Context context = this;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceUpdate(context);
        return super.onStartCommand(intent, flags, startId);
    }

    private void serviceUpdate(Context context) {
        DatabaseHelper database = new DatabaseHelper(context);

        // Get all Tasks
        List<Task> tasks = new ArrayList<>();
        Cursor data = database.getTaskData();
        while(data.moveToNext()) {
            if(data.getString(4).contains("true")) {
                Task newTask = new Task();
                newTask.setId(data.getInt(0));
                newTask.setChecklist(data.getString(1));
                newTask.setDescription(data.getString(2));
                newTask.setSkills(data.getString(3));
                newTask.setStatus(true);
                tasks.add(newTask);
            }
        }

        // Get All Skills
        List<Skill> skills = new ArrayList<>();
        data = database.queryTable("SELECT * FROM skills");
        while(data.moveToNext()) {
            Skill newSkill = new Skill();
            newSkill.setId(data.getInt(0));
            newSkill.setDescription(data.getString(1));
            newSkill.setLevel(data.getInt(2));

            skills.add(newSkill);
        }

        // For each task, see tagged skills
        for (int i = 0; i < tasks.size(); i++) {
            incrementSkill(database, skills, tasks.get(i).getSkills());
            uncheckStatus(database, tasks.get(i));
        }

        Log.d(TAG, "DatabaseService completed!");
    }

    private void incrementSkill(DatabaseHelper databaseHelper, List<Skill> allSkills, String taskSkills) {
        Log.d(TAG, "Tagged Skills: " + taskSkills);
        String[] taggedSkills = taskSkills.split(" ");

        for (int i = 0; i < allSkills.size(); i++) {
            for(String skill : taggedSkills) {
                if (allSkills.get(i).getDescription().contains(skill)) {
                    allSkills.get(i).setLevel(allSkills.get(i).getLevel() + LEVEL_INCREMENT_VAUE);

                    dbResult = databaseHelper.updateSkillData(allSkills.get(i), allSkills.get(i).getId());
                    if (!dbResult) {
                        Log.d(TAG, "Failed to Increment Skill Level!");
                    }
                    break;
                }
            }
        }
    }

    private void uncheckStatus(DatabaseHelper databaseHelper, Task task) {
        task.setStatus(false);
        dbResult = databaseHelper.updateTaskData(task, task.getId());

        if (!dbResult) {
            Log.d(TAG, "Failed to Set Task Status to False!");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}