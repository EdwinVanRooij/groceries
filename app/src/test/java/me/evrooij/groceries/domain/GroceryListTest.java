package me.evrooij.groceries.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.provider.Telephony.Carriers.PASSWORD;
import static org.junit.Assert.*;

/**
 * Created by eddy
 * Date: 27-11-16.
 */
public class GroceryListTest {

    private static final String USERNAME = "Hankinson";
    private static final String EMAIL = "Hankinson";
    private static final String PASSWORD = "Hankinson";
    private Account currentAccount;

    public static final String LIST_NAME = "MyList";
    private GroceryList currentList;

    @Before
    public void setUp() throws Exception {
        currentAccount = new Account(USERNAME, EMAIL, PASSWORD);
        currentList = new GroceryList(LIST_NAME, currentAccount);
    }

    @After
    public void tearDown() throws Exception {
        currentAccount = null;
        currentList = null;
    }

    @Test
    public void getName() throws Exception {
        String expected = LIST_NAME;
        String actual = currentList.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getOwner() throws Exception {
        Account expected = currentAccount;
        Account actual = currentList.getOwner();
        /*
         * assertSame because the Account object does not override assertEquals, it would compare hashcodes
         * instead of object fields.
         */
        assertSame(expected, actual);
    }

    @Test
    public void addItem() throws Exception {
        /*
         * Declare some variables for the product
         */
        String productName = "Apples";
        int amount = 3;
        String comment = "Jonagold";
        String owner = currentAccount.getUsername();

        /*
         * Get the current size of the list to compare with later
         */
        int sizeBeforeAddition = currentList.getAmountOfProducts();
        /*
         * Add it to the list, so we can retrieve it later on
         */
        currentList.addItem(productName, amount, comment, owner);
        /*
         * Verify that we have one more product now
         */
        int expectedSize = sizeBeforeAddition + 1;
        assertEquals(expectedSize, currentList.getAmountOfProducts());
    }

    @Test
    public void editItem() throws Exception {
        /**
         * Edits an existing item of this list
         *
         * @param name    new name of the product
         * @param amount  new amount of the product
         * @param comment new comment of the product
         * @param owner   must equal the owner of this product
         *                you're not allowed to edit the product of someone else
         * @return boolean indicating the exit status of the method
         */
        /*
         * Declare some variables for the product
         */
        String productName = "Apples";
        int amount = 3;
        String comment = "Jonagold";
        String owner = currentAccount.getUsername();

        /*
         * Create the product to edit later on
         */
        Product product = new Product(productName, amount, comment, owner);
        /*
         * Declare new values
         */
        String newProductName = "Big apples";
        int newAmount = 4;
        String newComment = "Jonagold in a basket";

        /*
         * Attempt to edit the item
         */
        currentList.editItem(product, newProductName, newAmount, newComment, owner);

        // TODO: 27-11-16 finish this test
    }

    @Test
    public void removeItem() throws Exception {
        /**
         * Removes a product
         *
         * @param product the product to remove
         */

    }

    @Test
    public void getAmountOfProducts() throws Exception {

        /**
         * Returns the amount of products currently in the list
         *
         * @return integer value indicating the size of the product list
         */
    }


    @Test
    public void testConstructor() throws Exception {

        /**
         * A list of product objects
         *
         * @param name  name of the list
         * @param owner account of the user who created this list
         */
    }
}