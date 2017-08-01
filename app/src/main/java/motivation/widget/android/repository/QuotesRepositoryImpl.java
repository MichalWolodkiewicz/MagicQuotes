package motivation.widget.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import motivation.widget.android.R;
import motivation.widget.android.model.quote.NextQuote;
import motivation.widget.android.model.quote.NextQuoteProvider;
import motivation.widget.android.model.quote.Quotes;
import motivation.widget.android.model.quote.QuotesRepository;


public class QuotesRepositoryImpl implements QuotesRepository {

    private final String[] quotesArray;
    private final SharedPreferences sharedPreferences;

    public QuotesRepositoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("quotes_shared", Context.MODE_PRIVATE);
        quotesArray = context.getResources().getStringArray(R.array.quotes);
    }

    @Override
    public NextQuoteProvider loadUserNextQuote() {
        long lastQuoteUpdateTime = sharedPreferences.getLong("lastQuoteUpdateTime", 0L);
        int lastQuoteIndex = sharedPreferences.getInt("lastQuoteIndex", 0);
        return new NextQuoteProvider(lastQuoteUpdateTime, lastQuoteIndex, quotesArray);
    }

    @Override
    public void updateNextQuote(NextQuote nextQuote) {
        sharedPreferences.edit()
                .putLong("lastQuoteUpdateTime", nextQuote.getCreateTime())
                .putInt("lastQuoteIndex", nextQuote.getIndex())
                .apply();
    }

    @Override
    public Quotes loadAllQuotes() {
        return new Quotes(quotesArray);
    }

    @Override
    public void saveLastSeenQuoteIndex(int index) {
        sharedPreferences.edit().putInt("last_seen_quote", index).apply();
    }

    @Override
    public int getLastSeenQuoteIndex() {
        return sharedPreferences.getInt("last_seen_quote", 0);
    }

    @Override
    public Set<Integer> loadFavourites() {
        final HashSet<Integer> favourites = new HashSet<>();
        favourites.add(100);
        favourites.add(102);
        favourites.add(104);
        favourites.add(106);
        return favourites;
    }

    @Override
    public void saveFavourites(Set<Integer> favourites) {

    }
}
