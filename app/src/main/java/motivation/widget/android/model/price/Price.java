package motivation.widget.android.model.price;


import java.util.Locale;

public class Price {

    private String yearPrice;
    private String weeklyPrice;
    private String currencyCode;
    private String currencySymbol;

    public Price(long yearPriceMicros, String currencyCode) {
        this.yearPrice = String.format(Locale.getDefault(), "%.2f", yearPriceMicros / 1000000d);
        this.currencyCode = currencyCode;
        this.weeklyPrice = calculateWeeklyPrice(yearPriceMicros / 1000000d);
        this.currencySymbol = findCurrencySymbol(currencyCode);
    }

    private String findCurrencySymbol(String currencyCode) {
        if (currencyCode.equals("USD")) {
            return "$";
        } else if (currencyCode.equals("EUR")) {
            return "€";
        }
        return "";
    }

    private String calculateWeeklyPrice(double yearPrice) {
        double weeklyPrice = (yearPrice / 52.0d);
        return String.format(Locale.getDefault(), "%.2f", weeklyPrice);
    }

    public String getYearPrice() {
        return yearPrice;
    }

    public String getWeeklyPrice() {
        return weeklyPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }
}
