package me.evrooij.groceries.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by eddy on 27-11-16.
 */
public class LoginManagerTest {
    /*
     * at least 6 characters, at max 30
     */
    public static final String CORRECT_USERNAME_1 = "111111";
    public static final String CORRECT_USERNAME_2 = "111111111111111111111111111111";
    public static final String CORRECT_USERNAME_3 = "Hank";
    public static final String INCORRECT_USERNAME_1 = "11111";
    public static final String INCORRECT_USERNAME_2 = "1111111111111111111111111111111";
    public static final String INCORRECT_USERNAME_3 = "hank@hankinson.com";
    public static final String CORRECT_EMAIL_1 = "mail@gmail.com";
    public static final String CORRECT_EMAIL_2 = "somestring@student.fontys.me";
    public static final String CORRECT_EMAIL_3 = "info@evrooij.me";
    public static final String INCORRECT_EMAIL_1 = "@mail.com";
    public static final String INCORRECT_EMAIL_2 = "info@.com";
    public static final String INCORRECT_EMAIL_3 = "info@me.";
    /*
     * at least 8 characters, at max 100
     */
    public static final String CORRECT_PASS_1 = "11111111";
    public static final String CORRECT_PASS_2 = "mypass1332";
    public static final String CORRECT_PASS_3 = "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    public static final String INCORRECT_PASS_1 = "1111111";
    public static final String INCORRECT_PASS_2 = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    public static final String INCORRECT_PASS_3 = "r11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    private LoginManager loginManager;

    @Before
    public void setUp() throws Exception {
        loginManager = new LoginManager();
    }

    @After
    public void tearDown() throws Exception {
        loginManager = null;
    }

    @Test
    public void login() throws Exception {
        /*
         * Happy flow: correct login
         */
        // Test one
        loginManager.register(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        Account account = loginManager.login(CORRECT_USERNAME_1, CORRECT_PASS_1);
        assertNotNull(account);
        // Test two
        loginManager.register(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        Account account2 = loginManager.login(CORRECT_USERNAME_2, CORRECT_PASS_2);
        assertNotNull(account2);
        /*
         * Test if it doesn't return anything when we have a correct login, but account was not registered
         */
        // Note how we don't register the account first
        Account accountFail = loginManager.login(CORRECT_USERNAME_3, CORRECT_PASS_3);
        assertNull(accountFail);
    }

    @Test
    public void register() throws Exception {
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
        /*
         * Check the happy flow first
         */
        Account account = loginManager.register(CORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNotNull(account);
        Account account2 = loginManager.register(CORRECT_USERNAME_2, CORRECT_EMAIL_2, CORRECT_PASS_2);
        assertNotNull(account2);
        Account account3 = loginManager.register(CORRECT_USERNAME_3, CORRECT_EMAIL_3, CORRECT_PASS_3);
        assertNotNull(account3);
        /*
         * Check whether the registration fails at any incorrect parameter
         */
        // Check incorrect username
        Account incorrectAccountUsername1 = loginManager.register(INCORRECT_USERNAME_1, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername1);
        Account incorrectAccountUsername2 = loginManager.register(INCORRECT_USERNAME_2, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername2);
        Account incorrectAccountUsername3 = loginManager.register(INCORRECT_USERNAME_3, CORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountUsername3);
        // Check incorrect email
        Account incorrectAccountEmail1 = loginManager.register(CORRECT_USERNAME_1, INCORRECT_EMAIL_1, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail1);
        Account incorrectAccountEmail2 = loginManager.register(CORRECT_USERNAME_1, INCORRECT_EMAIL_2, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail2);
        Account incorrectAccountEmail3 = loginManager.register(CORRECT_USERNAME_1, INCORRECT_EMAIL_3, CORRECT_PASS_1);
        assertNull(incorrectAccountEmail3);
        // Check incorrect pass
        Account incorrectAccountPass = loginManager.register(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_1);
        assertNull(incorrectAccountPass);
        Account incorrectAccountPass2 = loginManager.register(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_2);
        assertNull(incorrectAccountPass2);
        Account incorrectAccountPass3 = loginManager.register(CORRECT_USERNAME_2, CORRECT_EMAIL_3, INCORRECT_PASS_3);
        assertNull(incorrectAccountPass3);
        // Now let's get batshit crazy
        Account incorrectAccountEverything = loginManager.register(INCORRECT_USERNAME_1, INCORRECT_EMAIL_1, INCORRECT_PASS_1);
        assertNull(incorrectAccountEverything);
        Account incorrectAccountEverything2 = loginManager.register(INCORRECT_USERNAME_2, INCORRECT_EMAIL_2, INCORRECT_PASS_2);
        assertNull(incorrectAccountEverything2);
        Account incorrectAccountEverything3 = loginManager.register(INCORRECT_USERNAME_3, INCORRECT_EMAIL_3, INCORRECT_PASS_3);
        assertNull(incorrectAccountEverything3);
    }

}