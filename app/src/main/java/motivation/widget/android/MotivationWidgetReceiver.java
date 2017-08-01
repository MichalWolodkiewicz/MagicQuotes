package motivation.widget.android;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import motivation.widget.android.model.quote.NextQuote;
import motivation.widget.android.model.quote.NextQuoteProvider;
import motivation.widget.android.repository.QuotesRepositoryImpl;

public class MotivationWidgetReceiver extends AppWidgetProvider {

    public MotivationWidgetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            Log.i("WIDGET_TAG", "onReceive");
            if (appWidgetIds != null) {
                Log.i("WIDGET_TAG", "appWidgetIds.length = " + appWidgetIds.length);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i("WIDGET_TAG", "onUpdate");
        final QuotesRepositoryImpl quotesRepository = new QuotesRepositoryImpl(context.getApplicationContext());
        final NextQuoteProvider nextQuoteProvider = quotesRepository.loadUserNextQuote();
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.motivation_widget);
        final NextQuote nextQuote = nextQuoteProvider.getNextQuote();
        views.setTextViewText(R.id.author, nextQuote.getAuthor());
        views.setTextViewText(R.id.quote, nextQuote.getQuote());
        views.setOnClickPendingIntent(R.id.widget_container, getOnClickPendingIntent(context, appWidgetIds));
        if (nextQuoteProvider.isTimeForNextQuote()) {
            appWidgetManager.updateAppWidget(appWidgetIds, views);
            quotesRepository.updateNextQuote(nextQuote);
        }
    }

    public PendingIntent getOnClickPendingIntent(Context context, int[] appWidgetIds) {
        Intent intent = new Intent(context.getApplicationContext(), MotivationWidgetReceiver.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        return PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
