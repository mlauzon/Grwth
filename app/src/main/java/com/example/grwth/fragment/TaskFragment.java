package com.example.grwth.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.grwth.R;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Task;
import com.example.grwth.adapter.TaskListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private List<Task> mTaskList;
    private ListView mTaskListView;
    private Button mAddTaskBtn;
    private DatabaseHelper mDatabaseHelper;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String checklist_name) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, checklist_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        mTaskListView = (ListView)view.findViewById(R.id.taskListView);
        mAddTaskBtn = (Button)view.findViewById(R.id.btnAddTask);
        mDatabaseHelper = new DatabaseHelper(view.getContext());

        mTaskList = populateListView();

        final TaskListViewAdapter adapter = new TaskListViewAdapter(view.getContext(), mParam1, mTaskList, mDatabaseHelper);
        adapter.notifyDataSetChanged();
        mTaskListView.setAdapter(adapter);

        mAddTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addTask();
            }
        });

        return view;
    }

    private List<Task> populateListView() {
        Cursor data = mDatabaseHelper.getTaskData();
        List<Task> taskListData = new ArrayList<>();

        while(data.moveToNext()) {
            Task newTask = new Task();

            // Get data for column 1 (ID)
            newTask.setId(data.getInt(0));

            // Get data for column 2 (Checklist Name)
            newTask.setChecklist(data.getString(1));

            // Get data for column 3 (Task Description)
            newTask.setDescription(data.getString(2));

            // Get data for column 4 (Skill Tags)
            newTask.setSkills(data.getString(3));

            // Get data for column 5 (Status)
            if (data.getString(4).contains("false")) {
                newTask.setStatus(false);
            } else {
                newTask.setStatus(true);
            }

            taskListData.add(newTask);
        }

        return taskListData;
    }
}