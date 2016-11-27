package me.evrooij.groceries.domain;

/**
 * Created by eddy
 * Date: 27-11-16.
 */

public class LoginManager {

    /**
     * Retrieves the user's Account on a correct username with password combination.
     *
     * @param username globally unique username
     * @param password password for the user account
     * @return returns the account on correct login credentials, null on incorrect login credentials
     */
    public Account login(String username, String password) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new Account entry in the database.
     * @param username identification name, must be:
     *                 unique relative to all of the other usernames
     *                 at least 6 characters long
     *                 at max 30 characters long
     * @param email e-mail address of the user used for communication purposes, must:
     *              contain something before the @
     *              contain a @ character
     *              contain something after the @
     *              contain a dot after the @
     *              contain something after the dot
     * @param password used to log into the user account for validation, must be:
     *                 at least 8 characters long
     *                 at max 100 characters long
     * @return the user account if successful, null if unsuccessful
     */
    public Account register(String username, String email, String password) {
        throw new UnsupportedOperationException();
    }
}
