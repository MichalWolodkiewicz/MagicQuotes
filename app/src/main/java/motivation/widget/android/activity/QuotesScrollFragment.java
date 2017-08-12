package motivation.widget.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Iterator;

import motivation.widget.android.R;
import motivation.widget.android.RepositoryProvider;
import motivation.widget.android.activity.view.UserGuideRelativeLayout;
import motivation.widget.android.model.quote.Quote;
import motivation.widget.android.model.quote.Quotes;
import motivation.widget.android.model.quote.QuotesRepository;
import motivation.widget.android.repository.UserRepository;
import motivation.widget.android.util.CommonValues;


public class QuotesScrollFragment extends Fragment {

    private static final int UPDATE_TO_PREMIUM_REQUEST_CODE = 1234;
    private ViewFlipper viewFlipper;
    private ProgressBar quotesProgressBar;
    private Quotes quotes;
    private Iterator<Quote> quoteIterator;
    private LayoutInflater layoutInflater;
    private int currentQuoteIndex;
    private boolean wasInitialized;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.quotes_scroll_fragment, container, false);
        viewFlipper = (ViewFlipper) fragmentView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.fade_out);
        quotesProgressBar = (ProgressBar) fragmentView.findViewById(R.id.quotesProgress);
        if (getUserRepository().isPremiumUser()) {
            quotes = getQuotesRepository().loadAllPremiumUserQuotes();
        } else {
            quotes = getQuotesRepository().loadAllFreeUserQuotes();
        }
        currentQuoteIndex = getQuotesRepository().getLastSeenQuoteIndex();
        quoteIterator = quotes.getQuotesIteratorWithOffset(currentQuoteIndex);
        quotesProgressBar.setMax(quotes.count());
        layoutInflater = LayoutInflater.from(getActivity());
        loadNextQuotes();
        updateQuotesProgressBar();
        fragmentView.findViewById(R.id.nextQuote).setOnClickListener(new NextQuoteButtonOnClickListener());
        wasInitialized = true;
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserRepository().hasUserSeenFirstTimeTips()) {
            getUserRepository().setUserHasSeenFirstTimeTips();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addUserTipView();
                }
            }, 2000);
        }
    }

    private void addUserTipView() {
        RelativeLayout activityRootView = (RelativeLayout) getActivity().findViewById(R.id.activity_motivation);
        final UserGuideRelativeLayout userGuideView = (UserGuideRelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.favourite_tip, activityRootView, false);
        ImageView favouriteIconView = (ImageView) viewFlipper.getChildAt(0).findViewById(R.id.favouriteIcon);
        Rect favouriteIconPosition = getFavouriteCircleRectRelativeTo(favouriteIconView, activityRootView);
        userGuideView.setFavouriteCircleCenter(favouriteIconPosition.centerX(), favouriteIconPosition.centerY());
        userGuideView.findViewById(R.id.gotItButton).setOnClickListener(new GotItButtonClickListener(activityRootView, userGuideView));
        activityRootView.addView(userGuideView);
    }

    private Rect getFavouriteCircleRectRelativeTo(ImageView favouriteIconView, RelativeLayout activityRootView) {
        int activityHorizontalMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        int activityVerticalMargin = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        int[] circleScreenLocation = new int[2];
        favouriteIconView.getLocationOnScreen(circleScreenLocation);
        int[] activityScreenLocation = new int[2];
        activityRootView.getLocationOnScreen(activityScreenLocation);
        return new Rect(
                circleScreenLocation[0] - activityHorizontalMargin,
                circleScreenLocation[1] - activityScreenLocation[1] - activityVerticalMargin,
                circleScreenLocation[0] + favouriteIconView.getWidth() - activityHorizontalMargin,
                circleScreenLocation[1] + favouriteIconView.getHeight() - activityScreenLocation[1] - activityVerticalMargin);
    }

    @Override
    public void onPause() {
        super.onPause();
        getQuotesRepository().saveLastSeenQuoteIndex(currentQuoteIndex);
    }

    private void loadNextQuotes() {
        viewFlipper.removeAllViews();
        int currentIndex = 0;
        while (quoteIterator.hasNext() && currentIndex++ < 5) {
            final Quote quote = quoteIterator.next();
            viewFlipper.addView(createQuoteView(quote));
        }
        if (!quoteIterator.hasNext()) {
            currentQuoteIndex = 0;
            quoteIterator = quotes.getQuotesIteratorWithOffset(0);
            onQuotesLastEnd();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && wasInitialized) {
            getQuotesRepository().saveLastSeenQuoteIndex(currentQuoteIndex);
        } else if (wasInitialized) {
            refreshVisibleQuoteView((ImageButton) viewFlipper.getCurrentView().findViewById(R.id.favouriteIcon), currentQuoteIndex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onQuotesLastEnd() {
        //if (!userRepository.hasBeenAskedToBuyPremium()) {
        getUserRepository().markHasBeenAskedToBuyPremium();
        new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.purchase_premium_accept_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(getActivity(), UpgradeToPremiumActivity.class), UPDATE_TO_PREMIUM_REQUEST_CODE);
                    }
                })
                .setNegativeButton(R.string.purchase_premium_dissmis_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setMessage(R.string.ask_for_premium_purchase)
                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                .setTitle("    ")
                .show();
        // }
    }

    private View createQuoteView(Quote quote) {
        final LinearLayout quoteView = (LinearLayout) layoutInflater.inflate(R.layout.quote_view, viewFlipper, false);
        ((TextView) quoteView.findViewById(R.id.quote)).setText(quote.getText());
        ((TextView) quoteView.findViewById(R.id.author)).setText(quote.getAuthor());
        int imageResource = getQuotesRepository().loadFavourites().contains(quote.getIndex()) ? android.R.drawable.star_on : android.R.drawable.star_off;
        ImageView favouriteIcon = (ImageView) quoteView.findViewById(R.id.favouriteIcon);
        favouriteIcon.setOnClickListener(new OnFavouriteIconClickListener(quote.getIndex()));
        favouriteIcon.setImageResource(imageResource);
        return quoteView;
    }

    private void showNextQuote() {
        if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
            ++currentQuoteIndex;
            loadNextQuotes();
        } else {
            viewFlipper.showNext();
            ++currentQuoteIndex;
        }
        updateQuotesProgressBar();
    }

    private void updateQuotesProgressBar() {
        Log.i(CommonValues.TAG, "quotes list iteration position " + (currentQuoteIndex + 1) + "/" + quotes.count());
        quotesProgressBar.setProgress(currentQuoteIndex + 1);
    }

    private class OnFavouriteIconClickListener implements View.OnClickListener {

        private int quoteIndex;

        OnFavouriteIconClickListener(int quoteIndex) {
            this.quoteIndex = quoteIndex;
        }

        @Override
        public void onClick(View v) {
            if (!getQuotesRepository().loadFavourites().contains(quoteIndex)) {
                getQuotesRepository().addToFavourites(quoteIndex);
            } else {
                getQuotesRepository().removeFromFavourites(quoteIndex);
            }
            refreshVisibleQuoteView((ImageButton) v, quoteIndex);
        }
    }

    private void refreshVisibleQuoteView(ImageButton favouriteIcon, int quoteIndex) {
        if (getQuotesRepository().loadFavourites().contains(quoteIndex)) {
            favouriteIcon.setImageResource(android.R.drawable.star_on);
        } else {
            favouriteIcon.setImageResource(android.R.drawable.star_off);
        }
    }

    private class NextQuoteButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showNextQuote();
        }
    }

    private class GotItButtonClickListener implements View.OnClickListener {
        private final RelativeLayout activityRoot;
        private final RelativeLayout userGuideView;

        GotItButtonClickListener(RelativeLayout activityRoot, RelativeLayout userGuideView) {
            this.activityRoot = activityRoot;
            this.userGuideView = userGuideView;
        }

        @Override
        public void onClick(View v) {
            if (activityRoot != null && userGuideView != null) {
                activityRoot.removeView(userGuideView);
            }
        }
    }

    private UserRepository getUserRepository() {
        return ((RepositoryProvider) getActivity()).getUserRepository();
    }

    private QuotesRepository getQuotesRepository() {
        return ((RepositoryProvider) getActivity()).getQuotesRepository();
    }
}
