package motivation.widget.android.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


class QuotesFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public QuotesFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new QuotesScrollFragment();
                break;
            case 1:
                fragment = new FavouritesFragment();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position) {
            case 0:
                title = "Quotes";
                break;
            case 1:
                title = "Favourites";
                break;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
