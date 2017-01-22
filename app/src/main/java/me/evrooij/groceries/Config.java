package me.evrooij.groceries;

/**
 * Author: eddy
 * Date: 9-1-17.
 */
public class Config {

    // Parcelable keys
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_NEW_PRODUCT = "newProduct";
    public static final String KEY_EDIT_PRODUCT = "editProduct";
    public static final String KEY_SELECTED_ACCOUNTS = "selectedAccounts";
    public static final String KEY_ACCOUNT_PROFILE = "searchedUser";
    public static final String KEY_CRASH_REPORT = "crashReport";
    public static final String KEY_PRODUCT_ID = "productId";


    // Shared preferences keys
    public static final String PREF_KEY_ACCOUNT_ID = "PREF_ID";
    public static final String PREF_KEY_ACCOUNT_USERNAME = "PREF_USERNAME";
    public static final String PREF_KEY_ACCOUNT_EMAIL = "PREF_EMAIL";

    public static final String PREF_KEY_GROCERYLIST_ID = "GROCERYLIST_PREF_ID";
    public static final String PREF_KEY_GROCERYLIST_NAME = "GROCERYLIST_PREF_NAME";

    public static final int THREADPOOL_MAINACTIVITY_SIZE = 4;
    public static final int THREADPOOL_NEWLIST_SIZE = 4;
}
