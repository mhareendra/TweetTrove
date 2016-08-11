package com.codepath.apps.tweettrove.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.tweettrove.fragments.HomeTimelineFragment;
import com.codepath.apps.tweettrove.fragments.MentionsTimelineFragment;

/**
 * Created by Hari on 8/9/2016.
 */
public class FragmentTimelinePagerAdapter
        extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Home", "Mentions"};

    private Context context;

    public FragmentTimelinePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        //return TimelineFragment.newInstance(position + 1);

        if(position == 0)
        {
            HomeTimelineFragment frag = HomeTimelineFragment.newInstance();
            return frag;
        }
        else if (position == 1)
        {
            MentionsTimelineFragment frag = MentionsTimelineFragment.newInstance();
            return frag;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles[position];
    }


    @Override
    public int getCount() {
        return PAGE_COUNT ;
    }
}
