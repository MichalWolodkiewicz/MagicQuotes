package motivation.widget.android.model.quote;


public class NextQuoteProvider {

    public NextQuoteProvider(long lastQuoteUpdateTime, int lastQuoteIndex, String[] quotes) {
        this.lastQuoteUpdateTime = lastQuoteUpdateTime;
        this.lastQuoteIndex = lastQuoteIndex;
        this.quotes = quotes;
    }

    public boolean isTimeForNextQuote() {
        return true;
    }

    private int lastQuoteIndex;
    private long lastQuoteUpdateTime;
    private final String[] quotes;

    public NextQuote getNextQuote() {
        if (++lastQuoteIndex >= (quotes.length)) {
            lastQuoteIndex = 0;
        }
        lastQuoteUpdateTime = System.currentTimeMillis();
        final String[] quote = quotes[lastQuoteIndex].split("\\|");
        return new NextQuote(quote[0], quote[1], lastQuoteIndex, lastQuoteUpdateTime);
    }

    long getLastQuoteUpdateTime() {
        return lastQuoteUpdateTime;
    }
}
