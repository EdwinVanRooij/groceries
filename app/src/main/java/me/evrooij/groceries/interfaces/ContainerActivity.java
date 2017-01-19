package me.evrooij.groceries.interfaces;

import me.evrooij.groceries.data.Account;

/**
 * Author: eddy
 * Date: 19-1-17.
 */

public interface ContainerActivity {
    /**
     * Returns the currently logged in account
     *
     * @return account object
     */
    public Account getThisAccount();

    /**
     * Changes the current fragment with a new one, optionally adding it to back stack
     *
     * @param fragmentClass new fragment to show
     * @param addToStack    true to add current to back stack
     */
    public void setFragment(Class fragmentClass, boolean addToStack);

    /**
     * Executes a runnable in the container ThreadPool
     *
     * @param runnable runnable to execute
     */
    public void executeRunnable(Runnable runnable);

    /**
     * Sets the title of the action bar
     *
     * @param title title to change actionbar title to
     */
    public void setActionBarTitle(String title);
}
