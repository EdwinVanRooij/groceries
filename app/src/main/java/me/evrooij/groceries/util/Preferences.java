package me.evrooij.groceries.util;

import android.content.Context;
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.Config;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;

import static android.R.attr.configChanges;
import static android.R.attr.id;
import static android.os.Build.ID;

/**
 * Author: eddy
 * Date: 14-1-17.
 */
public class Preferences {

    public static boolean groceryListIdExists(Context context) {
        Hawk.init(context).build();
        return Hawk.contains(Config.PREF_KEY_GROCERYLIST_ID);
    }

    public static void saveGroceryListId(Context context, int id) {
        Hawk.init(context).build();

        Hawk.put(Config.PREF_KEY_GROCERYLIST_ID, id);
    }

    public static int getGroceryListId(Context context) {
        Hawk.init(context).build();

        return Hawk.get(Config.PREF_KEY_GROCERYLIST_ID);
    }

    public static void saveAccount(Context context, Account account) {
        Hawk.init(context).build();

        Hawk.put(Config.PREF_KEY_ACCOUNT_ID, account.getId());
        Hawk.put(Config.PREF_KEY_ACCOUNT_USERNAME, account.getUsername());
        Hawk.put(Config.PREF_KEY_ACCOUNT_EMAIL, account.getEmail());
    }

    public static boolean accountExists(Context context) {
        Hawk.init(context).build();
        return Hawk.contains(Config.PREF_KEY_ACCOUNT_ID);
    }

    public static Account getAccount(Context context) {
        Hawk.init(context).build();

        int id = Hawk.get(Config.PREF_KEY_ACCOUNT_ID);
        String username = Hawk.get(Config.PREF_KEY_ACCOUNT_USERNAME);
        String email = Hawk.get(Config.PREF_KEY_ACCOUNT_EMAIL);

        if (id > 0 && username != null && email != null) {
            return new Account(id, username, email);
        } else {
            return null;
        }
    }

    public static void removeAccount() {
        Hawk.delete(Config.PREF_KEY_ACCOUNT_ID);
        Hawk.delete(Config.PREF_KEY_ACCOUNT_USERNAME);
        Hawk.delete(Config.PREF_KEY_ACCOUNT_EMAIL);
    }
}
