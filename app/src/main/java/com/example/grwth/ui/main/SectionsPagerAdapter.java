package com.example.grwth.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.grwth.R;
import com.example.grwth.fragment.ErrorFragment;
import com.example.grwth.fragment.GoalFragment;
import com.example.grwth.fragment.RewardFragment;
import com.example.grwth.fragment.SkillFragment;
import com.example.grwth.fragment.StatsFragment;
import com.example.grwth.fragment.TaskFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Tasks", "Skills", "Stats", "Goals", "Rewards"};
    private final Context mContext;

    public String checklist_name = "Checklist 1";
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = TaskFragment.newInstance(checklist_name);
                break;

            case 1:
                fragment = SkillFragment.newInstance("Skills");
                break;

            case 2:
                fragment = StatsFragment.newInstance("Stats");
                break;

            case 3:
                fragment = new GoalFragment();
                break;

            case 4:
                fragment = RewardFragment.newInstance("Rewards");
                break;

            default:
                fragment = new ErrorFragment();
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}