package motivation.widget.android.model.quote;


public interface QuotesRepository {
    UserNextQuote loadUserNextQuote();

    void updateNextQuote(Quote quote);
}
