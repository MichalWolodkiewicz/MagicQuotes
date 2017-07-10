package motivation.widget.android.model.quote;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserQuoteTest {

    @Test
    public void isTimeForNextQuote() {
        assertTrue(new UserNextQuote(0L, 0, new String[0]).isTimeForNextQuote());
    }

    @Test
    public void updateLastUpdateTimeWhenNextQuoteIsUsed() {
        // given
        final UserNextQuote userNextQuote = new UserNextQuote(0L, 0, new String[]{"a|a", "b|b"});

        // when
        userNextQuote.getNextQuote();

        // then
        assertTrue(userNextQuote.getLastQuoteUpdateTime() > 0L);
    }

    @Test
    public void getFirstQuoteWhenLastWasUsedAlready() {
        // given
        final UserNextQuote userNextQuote = new UserNextQuote(0L, 1, new String[]{"a|a", "b|b"});

        // when
        final Quote quote = userNextQuote.getNextQuote();

        // then
        assertEquals(quote.getAuthor(), "a");
        assertEquals(quote.getQuote(), "a");
        assertEquals(quote.getIndex(), 0);
    }

    @Test
    public void getNextQuote() {
        // given
        final UserNextQuote userNextQuote = new UserNextQuote(0L, 0, new String[]{"a|a", "b|b"});

        // when
        final Quote quote = userNextQuote.getNextQuote();

        // then
        assertEquals(quote.getAuthor(), "b");
        assertEquals(quote.getQuote(), "b");
        assertEquals(quote.getIndex(), 1);
    }

}