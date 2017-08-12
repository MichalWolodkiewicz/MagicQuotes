package motivation.widget.android.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import motivation.widget.android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        QuotesFragmentPagerAdapter quotesFragmentPagerAdapter = new QuotesFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(quotesFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.quotesTab);
        tabLayout.setupWithViewPager(pager);
    }
}
