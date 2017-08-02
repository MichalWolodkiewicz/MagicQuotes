package motivation.widget.android.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import motivation.widget.android.R;

public class MotivationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new QuotesFragmentPagerAdapter(getSupportFragmentManager()));
    }
}
