package motivation.widget.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import motivation.widget.android.R;
import motivation.widget.android.model.quote.Quote;
import motivation.widget.android.model.quote.QuotesRepository;
import motivation.widget.android.model.quote.UserNextQuote;


public class QuotesRepositoryImpl implements QuotesRepository {

    private final String[] stringArray;
    private final SharedPreferences sharedPreferences;

    public QuotesRepositoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("quotes_shared", Context.MODE_PRIVATE);
        stringArray = context.getResources().getStringArray(R.array.quotes);
    }

    @Override
    public UserNextQuote loadUserNextQuote() {
        long lastQuoteUpdateTime = sharedPreferences.getLong("lastQuoteUpdateTime", 0L);
        int lastQuoteIndex = sharedPreferences.getInt("lastQuoteIndex", 0);
        return new UserNextQuote(lastQuoteUpdateTime, lastQuoteIndex, stringArray);
    }

    @Override
    public void updateNextQuote(Quote quote) {
        sharedPreferences.edit()
                .putLong("lastQuoteUpdateTime", quote.getCreateTime())
                .putInt("lastQuoteIndex", quote.getIndex())
                .commit();
    }
}
