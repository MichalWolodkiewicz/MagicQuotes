package motivation.widget.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import motivation.widget.android.R;
import motivation.widget.android.model.price.Price;
import motivation.widget.android.util.CommonValues;
import motivation.widget.android.util.iab.IabHelper;
import motivation.widget.android.util.iab.IabResult;
import motivation.widget.android.util.iab.Inventory;
import motivation.widget.android.util.iab.Purchase;
import motivation.widget.android.util.iab.SkuDetails;

public class UpgradeToPremiumActivity extends AppCompatActivity {

    private static final int SUBSCRIBE_REQUEST_CODE = 1001;

    private static final String KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA45AnGEyTapFJANFHKpVp8DUad0PEDvDiQAd7aRzd/2zlzbbNJbytKVhOiV8rhkivCpH6kSkf5JzHN0dN8b2DGzAezVugL6AYzTOYq02mp5gy09my1lORD2JY5+e8AGNxneI66JIPNWWX0WIAz49FtY9r4OS+tGaHJfWfuE8/GfUpWGsejPagFVxLSWDn9Hj1XKUS9jKAIVANtYDAZ2iEwEvNbPmJoWPF/JlrMP6J/nPbEzWl9OL7GzVUnifbfu5BxyfHVWhvM1Xvye6lyVLQvj0pbe7xqX+NfcR25wzwfkF+oXcrpPt00y54Hb4q5MBtm9g+ayAeAJUhcs1o/QFmcwIDAQAB";

    private IabHelper iabHelper;
    private boolean billingServiceBounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_premium);
        iabHelper = new IabHelper(this.getApplicationContext(), KEY);
        iabHelper.startSetup(onIabSetupFinishedListener);
    }

    private IabHelper.OnIabSetupFinishedListener onIabSetupFinishedListener = new IabHelper.OnIabSetupFinishedListener() {
        @Override
        public void onIabSetupFinished(IabResult result) {
            if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_OK) {
                iabHelper.enableDebugLogging(true, CommonValues.TAG);
                billingServiceBounded = true;
                readSubscriptionPrice();
            }
        }
    };

    private void readSubscriptionPrice() {
        try {
            iabHelper.queryInventoryAsync(true, null, null, new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (inv.hasDetails("premium_quotes")) {
                        SkuDetails skuDetails = inv.getSkuDetails("premium_quotes");
                        Price price = new Price(skuDetails.getPriceAmountMicros(), skuDetails.getPriceCurrencyCode());
                        ((TextView) findViewById(R.id.premiumPrice)).setText(price.getYearPrice() + " " + price.getCurrencyCode());
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            Log.e(CommonValues.TAG, e.getMessage(), e);
        }
    }

    private void launchPurchaseFlow() {
        try {
            iabHelper.launchPurchaseFlow(UpgradeToPremiumActivity.this, "hc_one_year_premium", SUBSCRIBE_REQUEST_CODE, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_OK) {
                        finish();
                    } else {
                        Log.e(CommonValues.TAG, result.getMessage());
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            Log.e(CommonValues.TAG, "IabAsyncInProgressException", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUBSCRIBE_REQUEST_CODE) {
            iabHelper.handleActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iabHelper != null && billingServiceBounded) {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
                Log.e(CommonValues.TAG, "IabAsyncInProgressException", e);
            }
        }
    }

    public void startPurchaseFlow(View view) {
        launchPurchaseFlow();
    }
}
