package motivation.widget.android.repository;


import android.content.Context;
import android.content.SharedPreferences;

public class UserRepository {

    private final SharedPreferences userPreferences;

    public UserRepository(Context context) {
        userPreferences = context.getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
    }

    public boolean isPremiumUser() {
        return userPreferences.getBoolean("isPremium", false);
    }

    public void setIsPremium() {
        userPreferences.edit().putBoolean("isPremium", true).apply();
    }

    public boolean hasBeenAskedToBuyPremium() {
        return userPreferences.getBoolean("hasBeenAskedToBuyPremium", false);
    }

    public void markHasBeenAskedToBuyPremium() {
        userPreferences.edit().putBoolean("hasBeenAskedToBuyPremium", true).apply();
    }
}
