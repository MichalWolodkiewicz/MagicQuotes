package motivation.widget.android.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import motivation.widget.android.R;
import motivation.widget.android.RepositoryProvider;
import motivation.widget.android.model.quote.QuotesRepository;
import motivation.widget.android.repository.QuotesRepositoryImpl;
import motivation.widget.android.repository.UserRepository;

public class MainActivity extends AppCompatActivity implements RepositoryProvider {

    private UserRepository userRepository;
    private QuotesRepository quotesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        userRepository = new UserRepository(getApplicationContext());
        quotesRepository = new QuotesRepositoryImpl(getApplicationContext());
        QuotesFragmentPagerAdapter quotesFragmentPagerAdapter = new QuotesFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(quotesFragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.quotesTab);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public QuotesRepository getQuotesRepository() {
        return quotesRepository;
    }
}
