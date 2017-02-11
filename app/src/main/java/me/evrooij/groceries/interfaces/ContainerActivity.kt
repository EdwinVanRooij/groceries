package me.evrooij.groceries.interfaces

import me.evrooij.groceries.models.Account

/**
 * Author: eddy
 * Date: 19-1-17.
 */

interface ContainerActivity {
    /**
     * Returns the currently logged in account

     * @return account object
     */
    fun getThisAccount(): Account

    /**
     * Changes the current fragment with a new one, optionally adding it to back stack

     * @param fragmentClass new fragment to show
     * *
     * @param addToStack    true to add current to back stack
     */
    fun setFragment(fragmentClass: Class<*>, addToStack: Boolean = false)

    /**
     * Executes a runnable in the container ThreadPool

     * @param runnable runnable to execute
     */
    fun executeRunnable(runnable: Runnable)

    /**
     * Sets the title of the action bar

     * @param title title to change actionbar title to
     */
    fun setActionBarTitle(title: String)
}
