package com.example.grwth.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.grwth.R;
import com.example.grwth.adapter.RewardListViewAdapter;
import com.example.grwth.adapter.StatsGridViewAdapter;
import com.example.grwth.data.DatabaseHelper;
import com.example.grwth.model.Reward;
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
 * Use the {@link RewardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RewardFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ListView mRewardListView;
    private DatabaseHelper mDatabaseHelper;
    private List<Reward> mRewardList;
    private Button mAddRewrdBtn;

    public RewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RewardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RewardFragment newInstance(String param1) {
        RewardFragment fragment = new RewardFragment();
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
        View view =  inflater.inflate(R.layout.fragment_reward, container, false);

        mDatabaseHelper = new DatabaseHelper(view.getContext());
        mRewardListView = (ListView)view.findViewById(R.id.rewardListView);
        mAddRewrdBtn = (Button)view.findViewById(R.id.btnAddReward);

        mRewardList = populateListView();

        final RewardListViewAdapter adapter = new RewardListViewAdapter(view.getContext(), mRewardList, mDatabaseHelper);
        mRewardListView.setAdapter(adapter);

        mAddRewrdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addReward();
            }
        });

        return view;
    }

    private List<Reward> populateListView() {
        Cursor data = mDatabaseHelper.getRewardData();
        List<Reward> rewardListData = new ArrayList<>();

        while(data.moveToNext()) {
            Reward newReward = new Reward();

            // Get data for column 1 (ID)
            newReward.setId(data.getInt(0));

            // Get data for column 2 (Reward Description)
            newReward.setDescription(data.getString(1));

            rewardListData.add(newReward);
        }

        return rewardListData;
    }
}