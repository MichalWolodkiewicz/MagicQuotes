package motivation.widget.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import motivation.widget.android.R;
import motivation.widget.android.model.quote.NextQuote;
import motivation.widget.android.model.quote.NextQuoteProvider;
import motivation.widget.android.model.quote.Quotes;
import motivation.widget.android.model.quote.QuotesRepository;


public class QuotesRepositoryImpl implements QuotesRepository {

    private final String[] quotesArray;
    private final SharedPreferences sharedPreferences;
    private Set<Integer> favourites;

    public QuotesRepositoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("quotes_shared", Context.MODE_PRIVATE);
        quotesArray = context.getResources().getStringArray(R.array.quotes);
        favourites = loadFavourites();
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
    public Quotes loadAllFreeUserQuotes() {
        return new Quotes(Arrays.copyOfRange(quotesArray, 0, quotesArray.length / 200));
    }

    @Override
    public Quotes loadAllPremiumUserQuotes() {
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
        if (favourites == null) {
            String json = sharedPreferences.getString("favourites", "[]");
            favourites = new Gson().fromJson(json, new TypeToken<LinkedHashSet<Integer>>() {
            }.getType());
        }
        return favourites;
    }

    @Override
    public void addToFavourites(int index) {
        favourites.add(index);
        String json = new Gson().toJson(favourites);
        sharedPreferences.edit().putString("favourites", json).apply();
    }

    @Override
    public void removeFromFavourites(int index) {
        favourites.remove(index);
        String json = new Gson().toJson(favourites);
        sharedPreferences.edit().putString("favourites", json).apply();
    }

    @Override
    public Quotes loadFavouritesQuotes() {
        return new Quotes(loadFavourites(), quotesArray);
    }
}
