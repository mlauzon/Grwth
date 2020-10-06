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
import com.example.grwth.model.Skill;
import com.example.grwth.adapter.SkillListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    private ListView mSkillListView;
    private Button mAddSkillBtn;
    private DatabaseHelper mDatabaseHelper;
    private List<Skill> mSkillList;


    public SkillFragment() {
        // Required empty public constructor
    }

    public static SkillFragment newInstance(String skill_name) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, skill_name);
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
        View view =  inflater.inflate(R.layout.fragment_skill, container, false);

        mSkillListView = (ListView) view.findViewById(R.id.skillListView);
        mAddSkillBtn = (Button) view.findViewById(R.id.btnAddSkill);
        mDatabaseHelper = new DatabaseHelper(view.getContext());

        mSkillList = populateListView();

        final SkillListViewAdapter adapter = new SkillListViewAdapter(view.getContext(), mSkillList, mDatabaseHelper);
        mSkillListView.setAdapter(adapter);

        mAddSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addSkill();
            }
        });

        return view;
    }

    private List<Skill> populateListView() {
        Cursor data = mDatabaseHelper.getSkillData();
        List<Skill> skillListData = new ArrayList<>();

        while(data.moveToNext()) {
            Skill newSkill = new Skill();

            // Get data for column 1 (ID)
            newSkill.setId(data.getInt(0));

            // Get data for column 2 (Skill Description)
            newSkill.setDescription(data.getString(1));

            // Get data for column 3 (Skill Level)
            newSkill.setLevel(data.getInt(2));

            skillListData.add(newSkill);
        }

        return skillListData;
    }
}