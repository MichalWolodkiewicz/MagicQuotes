package motivation.widget.android.model.quote;


import android.util.Log;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Quotes {

    private Set<Quote> quotes;

    public Quotes(String[] rawQuotes) {
        this.quotes = new LinkedHashSet<>(rawQuotes.length);
        for (int i = 0; i < rawQuotes.length; i++) {
            try {
                final String[] quote = rawQuotes[i].split("\\|");
                quotes.add(new Quote(i, quote[0], quote[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.w("MOTIVATION_WIDGET", rawQuotes[i]);
            }
        }
    }

    public Iterator<Quote> getQuotesIteratorWithOffset(int offset) {
        final Iterator<Quote> iterator = quotes.iterator();
        for (int i = 0; i < offset && iterator.hasNext(); i++) {
            iterator.next();
        }
        return iterator;
    }

    public int count() {
        return quotes.size();
    }
}
