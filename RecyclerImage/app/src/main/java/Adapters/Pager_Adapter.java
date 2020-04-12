package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Friday on 2018/7/11.
 */

public class Pager_Adapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    public Pager_Adapter(FragmentManager fm,Fragment... fragments){
        super(fm);
        this.fragments=fragments;
    }

    public Pager_Adapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments.length;
    }
}