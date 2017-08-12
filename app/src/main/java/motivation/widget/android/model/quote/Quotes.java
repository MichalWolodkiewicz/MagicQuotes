package motivation.widget.android.model.quote;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Quotes {

    private List<Quote> quotes;

    public Quotes(String[] rawQuotes) {
        this.quotes = new ArrayList<>(rawQuotes.length);
        for (int i = 0; i < rawQuotes.length; i++) {
            try {
                final String[] quote = rawQuotes[i].split("\\|");
                quotes.add(new Quote(i, quote[0], quote[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.w("MOTIVATION_WIDGET", rawQuotes[i]);
            }
        }
    }

    public Quotes(Collection<Integer> indexes, String[] rawQuotes) {
        List<Quote> quotes = new ArrayList<>(indexes.size());
        for (Integer index : indexes) {
            final String[] quote = rawQuotes[index].split("\\|");
            quotes.add(new Quote(index, quote[0], quote[1]));
        }
        this.quotes = quotes;
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

    public Quote get(int position) {
        return quotes.get(position);
    }

    public void removeAt(int adapterPosition) {
        quotes.remove(adapterPosition);
    }
}
