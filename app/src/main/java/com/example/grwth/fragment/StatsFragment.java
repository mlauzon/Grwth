package com.example.grwth.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.grwth.R;
import com.example.grwth.adapter.SkillListViewAdapter;
import com.example.grwth.adapter.StatsGridViewAdapter;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Skill;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private GridView mStatsGrid;
    private PieChart mSkillPieChart;
    private DatabaseHelper mDatabaseHelper;
    private List<Skill> mSkillList;

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1) {
        StatsFragment fragment = new StatsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_stats, container, false);

        mDatabaseHelper = new DatabaseHelper(view.getContext());
        mStatsGrid = (GridView) view.findViewById(R.id.statsGridView);
        mSkillPieChart = (PieChart) view.findViewById(R.id.statsPieChart);

        mSkillList = populateListView();

        mSkillPieChart.setUsePercentValues(true);
        mSkillPieChart.setHoleRadius(25f);
        mSkillPieChart.setTransparentCircleRadius(25f);
        List<PieEntry> pieValue = new ArrayList<>();

        for (Skill skill : mSkillList) {
            pieValue.add(new PieEntry((float)skill.getLevel(), skill.getDescription()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieValue, "Skills");
        PieData pieData = new PieData(pieDataSet);
        mSkillPieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        mSkillPieChart.animateXY(1400, 1400);

        final StatsGridViewAdapter adapter = new StatsGridViewAdapter(view.getContext(), mSkillList, mDatabaseHelper);
        mStatsGrid.setAdapter(adapter);

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