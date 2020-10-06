package com.example.grwth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.grwth.model.Checklist;
import com.example.grwth.model.Goal;
import com.example.grwth.model.Reward;
import com.example.grwth.model.Skill;
import com.example.grwth.model.Task;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TASK_TABLE = "tasks";
    private static final String TASK_COL1 = "ID";
    private static final String TASK_COL2 = "Checklist";
    private static final String TASK_COL3 = "Description";
    private static final String TASK_COL4 = "Skills";
    private static final String TASK_COL5 = "Status";

    private static final String SKILL_TABLE = "skills";
    private static final String SKILL_COL1 = "ID";
    private static final String SKILL_COL2 = "Description";
    private static final String SKILL_COL3 = "Level";

    private static final String CHECKLIST_TABLE = "checklists";
    private static final String CHECKLIST_COL1 = "ID";
    private static final String CHECKLIST_COL2 = "Name";

    private static final String GOAL_TABLE = "goals";
    private static final String GOAL_COL1 = "ID";
    private static final String GOAL_COL2 = "StartDate";
    private static final String GOAL_COL3 = "CompletionDate";
    private static final String GOAL_COL4 = "GoalDate";
    private static final String GOAL_COL5 = "Skills";
    private static final String GOAL_COL6 = "RewardID";

    private static final String REWARD_TABLE = "rewards";
    private static final String REWARD_COL1 = "ID";
    private static final String REWARD_COL2 = "Description";


    public DatabaseHelper(Context context) {
        super(context, TASK_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE tasks (ID INTEGER PRIMARY KEY, Checklist TEXT, Description TEXT, Skills TEXT, Status TEXT)
        String createTable = "CREATE TABLE " + TASK_TABLE + " (ID INTEGER PRIMARY KEY, " +
                TASK_COL2 + " TEXT, " + TASK_COL3 + " TEXT, " + TASK_COL4 + " TEXT, " + TASK_COL5 + " TEXT)";
        db.execSQL(createTable);

        // CREATE TABLE skills (ID INTEGER PRIMARY KEY, Description TEXT, Level INTEGER)
        createTable = "CREATE TABLE " + SKILL_TABLE + " (ID INTEGER PRIMARY KEY, " +
                SKILL_COL2 + " TEXT, " + SKILL_COL3 + " INTEGER)";
        db.execSQL(createTable);

        // CREATE TABLE checklists (ID INTEGER PRIMARY KEY, Name TEXT)
        createTable = "CREATE TABLE " + CHECKLIST_TABLE + " (ID INTEGER PRIMARY KEY, " +
                CHECKLIST_COL2 + " TEXT)";
        db.execSQL(createTable);

        // CREATE TABLE goals (ID INTEGER PRIMARY KEY, StartDate TEXT, PlannedCompletionDate TEXT,
        // CompletionDate TEXT, Skills TEXT, RewardID INTEGER)
        createTable = "CREATE TABLE " + GOAL_TABLE + " (ID INTEGER PRIMARY KEY, " +
                GOAL_COL2 + " TEXT, " + GOAL_COL3 + " TEXT, " + GOAL_COL4 + " TEXT, " +
                GOAL_COL5 + " TEXT, " + GOAL_COL6 + " INTEGER)";
        db.execSQL(createTable);

        // CREATE TABLE rewards (ID INTEGER PRIMARY KEY, Description TEXT)
        createTable = "CREATE TABLE " + REWARD_TABLE + " (ID INTEGER PRIMARY KEY, " +
                REWARD_COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SKILL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHECKLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REWARD_TABLE);
        onCreate(db);
    }

    public Cursor queryTable(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /*
    Task
     */
    public boolean addTaskData(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TASK_COL1, task.getId());
        contentValues.put(TASK_COL2, task.getChecklist());
        contentValues.put(TASK_COL3, task.getDescription());
        contentValues.put(TASK_COL4, task.getSkills());
        contentValues.put(TASK_COL5, task.getStatus().toString());

        Log.d(TAG, "addData: Adding '" + task.getDescription() + "' to " + TASK_TABLE);

        long result = db.insert(TASK_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean updateTaskData(Task task, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TASK_COL2, task.getChecklist());
        contentValues.put(TASK_COL3, task.getDescription());
        contentValues.put(TASK_COL4, task.getSkills());
        contentValues.put(TASK_COL5, task.getStatus().toString());

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "updateData: Adding '" + task.getDescription() + "' to " + TASK_TABLE);

        long result = db.update(TASK_TABLE, contentValues, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean deleteTaskData(int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "deleteData: Deleting ID '" + position + "' from " + TASK_TABLE);

        long result = db.delete(TASK_TABLE, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getTaskData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // DEBUG ONLY
    public void printTaskTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE;
        Cursor data = db.rawQuery(query, null);
        String tableString = String.format("Table %s:\n", TASK_TABLE);
        if (data.moveToFirst() ){
            String[] columnNames = data.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (data.moveToNext());
        }
        System.out.print(tableString);
    }

    /*
    Skill
     */
    public boolean addSkillData(Skill skill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SKILL_COL1, skill.getId());
        contentValues.put(SKILL_COL2, skill.getDescription());
        contentValues.put(SKILL_COL3, Integer.toString(skill.getLevel()));

        Log.d(TAG, "addData: Adding '" + skill.getDescription() + "' to " + SKILL_TABLE);

        long result = db.insert(SKILL_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean updateSkillData(Skill skill, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SKILL_COL2, skill.getDescription());
        contentValues.put(SKILL_COL3, Integer.toString(skill.getLevel()));

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "updateData: Adding '" + skill.getDescription() + "' to " + SKILL_TABLE);

        long result = db.update(SKILL_TABLE, contentValues, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean deleteSkillData(int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "deleteData: Deleting ID '" + position + "' from " + SKILL_TABLE);

        long result = db.delete(SKILL_TABLE, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getSkillData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + SKILL_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void printSkillTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + SKILL_TABLE;
        Cursor data = db.rawQuery(query, null);
        String tableString = String.format("Table %s:\n", SKILL_TABLE);
        if (data.moveToFirst() ){
            String[] columnNames = data.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (data.moveToNext());
        }
        System.out.print(tableString);
    }

    /*
    Checklist
     */
    public boolean addChecklistData(Checklist checklist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CHECKLIST_COL1, checklist.getId());
        contentValues.put(CHECKLIST_COL2, checklist.getName());

        Log.d(TAG, "addData: Adding '" + checklist.getName() + "' to " + CHECKLIST_TABLE);

        long result = db.insert(CHECKLIST_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean updateChecklistData(Checklist checklist, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CHECKLIST_COL1, checklist.getId());
        contentValues.put(CHECKLIST_COL2, checklist.getName());

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "updateData: Adding '" + checklist.getName()  + "' to " + CHECKLIST_TABLE);

        long result = db.update(CHECKLIST_TABLE, contentValues, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean deleteChecklistData(Checklist checklist, int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "deleteData: Deleting ID '" + position + "' from " + CHECKLIST_TABLE);

        long result = db.delete(CHECKLIST_TABLE, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getChecklistData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CHECKLIST_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // DEBUG ONLY
    public void printChecklistTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CHECKLIST_TABLE;
        Cursor data = db.rawQuery(query, null);
        String tableString = String.format("Table %s:\n", CHECKLIST_TABLE);
        if (data.moveToFirst() ){
            String[] columnNames = data.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (data.moveToNext());
        }
        System.out.print(tableString);
    }

    /*
    Goal
     */
    public boolean addGoalData(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(GOAL_COL1, goal.getId());
        contentValues.put(GOAL_COL2, goal.getStartDate());
        contentValues.put(GOAL_COL3, goal.getCompletionDate());
        contentValues.put(GOAL_COL4, goal.getGoalDate());
        contentValues.put(GOAL_COL5, goal.getSkills());
        contentValues.put(GOAL_COL6, goal.getRewardId());

        Log.d(TAG, "addData: Adding '" + goal.getId() + "' to " + GOAL_TABLE);

        long result = db.insert(GOAL_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean updateGoalData(Goal goal, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(GOAL_COL1, goal.getId());
        contentValues.put(GOAL_COL2, goal.getStartDate());
        contentValues.put(GOAL_COL3, goal.getCompletionDate());
        contentValues.put(GOAL_COL4, goal.getGoalDate());
        contentValues.put(GOAL_COL5, goal.getSkills());
        contentValues.put(GOAL_COL6, goal.getRewardId());

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "updateData: Adding '" + goal.getId()  + "' to " + GOAL_TABLE);

        long result = db.update(GOAL_TABLE, contentValues, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean deleteGoalData(int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "deleteData: Deleting ID '" + position + "' from " + GOAL_TABLE);

        long result = db.delete(GOAL_TABLE, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getGoalData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + GOAL_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // DEBUG ONLY
    public void printGoalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + GOAL_TABLE;
        Cursor data = db.rawQuery(query, null);
        String tableString = String.format("Table %s:\n", GOAL_TABLE);
        if (data.moveToFirst() ){
            String[] columnNames = data.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (data.moveToNext());
        }
        System.out.print(tableString);
    }

    /*
    Reward
     */
    public boolean addRewardData(Reward reward) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(REWARD_COL1, reward.getId());
        contentValues.put(REWARD_COL2, reward.getDescription());

        Log.d(TAG, "addData: Adding '" + reward.getId() + "' to " + REWARD_TABLE);

        long result = db.insert(REWARD_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean updateRewardData(Reward reward, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(REWARD_COL1, reward.getId());
        contentValues.put(REWARD_COL2, reward.getDescription());

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "updateData: Adding '" + reward.getId()  + "' to " + REWARD_TABLE);

        long result = db.update(REWARD_TABLE, contentValues, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public boolean deleteRewardData(int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = {Integer.toString(position)};

        Log.d(TAG, "deleteData: Deleting ID '" + position + "' from " + REWARD_TABLE);

        long result = db.delete(REWARD_TABLE, whereClause, whereArgs);

        if (result == -1) {
            return false;
        }

        return true;
    }

    public Cursor getRewardData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + REWARD_TABLE;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // DEBUG ONLY
    public void printRewardTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + REWARD_TABLE;
        Cursor data = db.rawQuery(query, null);
        String tableString = String.format("Table %s:\n", REWARD_TABLE);
        if (data.moveToFirst() ){
            String[] columnNames = data.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            data.getString(data.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (data.moveToNext());
        }
        System.out.print(tableString);
    }
}
