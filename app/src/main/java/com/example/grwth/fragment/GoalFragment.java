package com.example.grwth.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.grwth.R;
import com.example.grwth.adapter.GoalListViewAdapter;
import com.example.grwth.adapter.TaskListViewAdapter;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Goal;
import com.example.grwth.model.Reward;
import com.example.grwth.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private DatabaseHelper mDatabaseHelper;
    private ListView mGoalListView;
    private List<Goal> mGoalList;
    private Button mAddGoalBtn;

    public GoalFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalFragment newInstance(String param1, String param2) {
        GoalFragment fragment = new GoalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        mGoalListView = (ListView)view.findViewById(R.id.goalListView);
        mAddGoalBtn = (Button)view.findViewById(R.id.btnAddGoal);
        mDatabaseHelper = new DatabaseHelper(view.getContext());

        mGoalList = populateListView();

        final GoalListViewAdapter adapter = new GoalListViewAdapter(view.getContext(), mGoalList, mDatabaseHelper);
        mGoalListView.setAdapter(adapter);

        mAddGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addGoal();
            }
        });

        return view;
    }

    private List<Goal> populateListView() {
        Cursor data = mDatabaseHelper.getGoalData();
        List<Goal> goalListData = new ArrayList<>();

        while(data.moveToNext()) {
            Goal newGoal = new Goal();

            // Get data for column 1 (ID)
            newGoal.setId(data.getInt(0));

            // Get data for column 2 (Start Date)
            newGoal.setStartDate(data.getString(1));

            // Get data for column 3 (Completion Date)
            newGoal.setCompletionDate(data.getString(2));

            // Get data for column 4 (Goal Date)
            newGoal.setGoalDate(data.getString(3));

            // Get data for column 4 (Skills)
            newGoal.setSkills(data.getString(4));

            // Get data for column 5 (Reward ID)
            newGoal.setRewardId(data.getInt(5));

            goalListData.add(newGoal);
        }

        return goalListData;
    }
}