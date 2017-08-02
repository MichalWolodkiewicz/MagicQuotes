package motivation.widget.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Iterator;
import java.util.Set;

import motivation.widget.android.R;
import motivation.widget.android.model.quote.Quote;
import motivation.widget.android.model.quote.Quotes;
import motivation.widget.android.repository.QuotesRepositoryImpl;


public class QuotesScrollFragment extends Fragment {

    private static final String TAG = "MOTIVATION";
    private ViewFlipper viewFlipper;
    private ProgressBar quotesProgressBar;
    private Quotes quotes;
    private Iterator<Quote> quoteIterator;
    private LayoutInflater layoutInflater;
    private QuotesRepositoryImpl quotesRepository;
    private int currentQuoteIndex;
    private Set<Integer> favourites;
    private boolean wasInitialized;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.quotes_scroll_fragment, container, false);
        viewFlipper = (ViewFlipper) fragmentView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.fade_out);
        quotesProgressBar = (ProgressBar) fragmentView.findViewById(R.id.quotesProgress);
        quotesRepository = new QuotesRepositoryImpl(getActivity());
        quotes = quotesRepository.loadAllQuotes();
        currentQuoteIndex = quotesRepository.getLastSeenQuoteIndex();
        quoteIterator = quotes.getQuotesIteratorWithOffset(currentQuoteIndex);
        quotesProgressBar.setMax(quotes.count() - 1);
        favourites = quotesRepository.loadFavourites();
        layoutInflater = LayoutInflater.from(getActivity());
        loadNextQuotes();
        updateQuotesProgressBar();
        fragmentView.findViewById(R.id.nextQuote).setOnClickListener(new NextQuoteButtonOnClickListener());
        wasInitialized = true;
        return fragmentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && wasInitialized) {
            quotesRepository.saveLastSeenQuoteIndex(currentQuoteIndex);
            quotesRepository.saveFavourites(favourites);
        }
    }

    private void loadNextQuotes() {
        viewFlipper.removeAllViews();
        int currentIndex = 0;
        while (quoteIterator.hasNext() && currentIndex++ < 5) {
            Log.d(TAG, "Loading more quotes to flipper");
            final Quote quote = quoteIterator.next();
            viewFlipper.addView(createQuoteView(quote));
        }
        if (!quoteIterator.hasNext()) {
            currentQuoteIndex = 0;
            quoteIterator = quotes.getQuotesIteratorWithOffset(0);
        }
    }

    private View createQuoteView(Quote quote) {
        final LinearLayout quoteView = (LinearLayout) layoutInflater.inflate(R.layout.quote_view, viewFlipper, false);
        ((TextView) quoteView.findViewById(R.id.quote)).setText(quote.getText());
        ((TextView) quoteView.findViewById(R.id.author)).setText(quote.getAuthor());
        int imageResource = favourites.contains(quote.getIndex()) ? android.R.drawable.star_on : android.R.drawable.star_off;
        ImageView favouriteIcon = (ImageView) quoteView.findViewById(R.id.favouriteIcon);
        favouriteIcon.setOnClickListener(new OnFavouriteIconClickListener(quote.getIndex(), favourites.contains(quote.getIndex())));
        favouriteIcon.setImageResource(imageResource);
        return quoteView;
    }

    private void showNextQuote() {
        if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            loadNextQuotes();
            ++currentQuoteIndex;
        } else {
            viewFlipper.showNext();
            ++currentQuoteIndex;
            updateQuotesProgressBar();
        }
    }

    private void updateQuotesProgressBar() {
        quotesProgressBar.setProgress(currentQuoteIndex);
    }

    private class OnFavouriteIconClickListener implements View.OnClickListener {

        private int quoteIndex;
        private boolean isFavourite;

        OnFavouriteIconClickListener(int quoteIndex, boolean isFavourite) {
            this.quoteIndex = quoteIndex;
            this.isFavourite = isFavourite;
        }

        @Override
        public void onClick(View v) {
            isFavourite = !isFavourite;
            if (isFavourite) {
                ((ImageView) v).setImageResource(android.R.drawable.star_on);
                favourites.add(quoteIndex);
            } else {
                ((ImageView) v).setImageResource(android.R.drawable.star_off);
                favourites.remove(quoteIndex);
            }
        }
    }

    private class NextQuoteButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showNextQuote();
        }
    }
}
