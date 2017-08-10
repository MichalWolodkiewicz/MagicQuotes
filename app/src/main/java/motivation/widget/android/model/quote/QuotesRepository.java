package motivation.widget.android.model.quote;


import java.util.Set;

public interface QuotesRepository {
    NextQuoteProvider loadUserNextQuote();

    void updateNextQuote(NextQuote nextQuote);

    Quotes loadAllFreeUserQuotes();

    Quotes loadAllPremiumUserQuotes();

    void saveLastSeenQuoteIndex(int index);

    int getLastSeenQuoteIndex();

    Set<Integer> loadFavourites();

    void saveFavourites(Set<Integer> favourites);

    Quotes loadFavouritesQuotes();
}
