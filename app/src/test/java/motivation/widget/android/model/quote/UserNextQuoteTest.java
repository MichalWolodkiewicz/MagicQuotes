package motivation.widget.android.model.quote;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserNextQuoteTest {

    @Test
    public void isTimeForNextQuote() {
        assertTrue(new NextQuoteProvider(0L, 0, new String[0]).isTimeForNextQuote());
    }

    @Test
    public void updateLastUpdateTimeWhenNextQuoteIsUsed() {
        // given
        final NextQuoteProvider nextQuoteProvider = new NextQuoteProvider(0L, 0, new String[]{"a|a", "b|b"});

        // when
        nextQuoteProvider.getNextQuote();

        // then
        assertTrue(nextQuoteProvider.getLastQuoteUpdateTime() > 0L);
    }

    @Test
    public void getFirstQuoteWhenLastWasUsedAlready() {
        // given
        final NextQuoteProvider nextQuoteProvider = new NextQuoteProvider(0L, 1, new String[]{"a|a", "b|b"});

        // when
        final NextQuote nextQuote = nextQuoteProvider.getNextQuote();

        // then
        assertEquals(nextQuote.getAuthor(), "a");
        assertEquals(nextQuote.getQuote(), "a");
        assertEquals(nextQuote.getIndex(), 0);
    }

    @Test
    public void getNextQuote() {
        // given
        final NextQuoteProvider nextQuoteProvider = new NextQuoteProvider(0L, 0, new String[]{"a|a", "b|b"});

        // when
        final NextQuote nextQuote = nextQuoteProvider.getNextQuote();

        // then
        assertEquals(nextQuote.getAuthor(), "b");
        assertEquals(nextQuote.getQuote(), "b");
        assertEquals(nextQuote.getIndex(), 1);
    }

}