package com.b07.database.helper.android;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.b07.database.DatabaseDriverAndroid;
import com.b07.database.DatabaseSelector;
import com.b07.database.helper.DatabaseDriverHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.enumerators.Roles;
import com.b07.exceptions.InvalidIdException;
import com.b07.exceptions.InvalidInputException;
import com.b07.exceptions.InvalidRoleException;
import com.b07.exceptions.InventoryFullException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.inventory.ItemImpl;
import com.b07.store.ItemizedSaleImpl;
import com.b07.store.Sale;
import com.b07.store.SaleImpl;
import com.b07.store.SalesLog;
import com.b07.store.SalesLogImpl;
import com.b07.users.Account;
import com.b07.users.Admin;
import com.b07.users.Customer;
import com.b07.users.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tayyab on 2017-12-01.
 */

public class DatabaseAndroidSelectHelper extends DatabaseDriverAndroid {
    public DatabaseAndroidSelectHelper(Context context) {
        super(context);
    }

    public User getUser(int userId) throws InvalidRoleException {
        User user = null;
        Cursor c = super.getUserDetails(userId);
        int id = -1;
        String name = "";
        int age = -1;
        String address = "";

        // get user info
        while(c.moveToNext()){
            id = c.getInt(c.getColumnIndex("ID"));
            name = c.getString(c.getColumnIndex("NAME"));
            age =  c.getInt(c.getColumnIndex("AGE"));
            address = c.getString(c.getColumnIndex("ADDRESS"));
        }
        c.close();
        // get users role id
        int roleId = getUserRole(userId);
        // construct user and return
        user = createUser(id, name, age, address, roleId);
        return user;

    }
    private User createUser(int id, String name, int age, String address, int roleId) {
        // initialize the user
        User user = null;
        // get the role name using the id
        String roleName = getRoleName(roleId);
        // if role is activity_admin create an activity_admin
        if (roleName.equals(Roles.ADMIN.toString())) {
            user = new Admin(id, name, age, address);
        } else if (roleName.equals(Roles.CUSTOMER.toString())) {
            user = new Customer(id, name, age, address);
        }
        return user;
    }
    public String getRoleName(int roleId) {
        return super.getRole(roleId);
    }

    public int getUserRoleId(int userId) {
        return super.getUserRole(userId);
    }

    public List<Integer> getRoleIdsHelper() {
        Cursor c = super.getRoles();
        List<Integer> roleIds = new ArrayList<>();

        while(c.moveToNext()){
            roleIds.add(c.getInt(c.getColumnIndex("ID")));
        }
        c.close();
        return roleIds;
    }

    public String getPasswordHelper(int userId)  {
        return super.getPassword(userId);
    }
    public Item getItemHelper(int itemId) throws InvalidIdException {
        // intitialize item info
        int id = 0;
        String name = null;
        BigDecimal price = null;

        // get cursor and search for the item's info
        Cursor c = super.getItem(itemId);
        while(c.moveToNext()){
            id = c.getInt(c.getColumnIndex("ID"));
            name = c.getString(c.getColumnIndex("NAME"));
            price = new BigDecimal(c.getString(c.getColumnIndex("PRICE")));
        }
        c.close();

        // create item and return if item was found
        if(id > 0){
            Item item = new ItemImpl();
            item.setId(id);
            item.setPrice(price);
            item.setName(name);
            return item;
        } else {
            // item not in database so throw exception
            throw new InvalidIdException("Item not in database.");
        }

    }

    public List<Item> getAllItemsHelper() throws SQLException {
        Cursor c = super.getAllItems();
        // initialize list of items to return
        List<Item> items = new ArrayList<>();
        while (c.moveToNext()) {
            // get the information about the current item
            int id = c.getInt(c.getColumnIndex("ID"));
            String name = c.getString(c.getColumnIndex("NAME"));
            BigDecimal price = new BigDecimal(c.getString(c.getColumnIndex("PRICE")));

            // create the item
            Item item = new ItemImpl();
            item.setId(id);
            item.setName(name);
            item.setPrice(price);

            // add the current item to the list of items
            items.add(item);
        }
        // close cursor and return list of items
        c.close();
        return items;
    }

    public int getInventoryQuantity(int itemId) {
        // initialize quantity and search inventory for it
        int quantity = -1;
        List<Item> itemList = getAllItemsHelper();
        for (Item item : itemList) {
            if (item.getId() == itemId) {
                // connect to database and get the inventory quantity once we have found the item
                quantity = super.getInventoryQuantity(itemId);
                return quantity;
            }
        }
        // quantity of -1  will be returned if invalid item Id
        return quantity;
    }

/*








    private boolean checkUserId(int userId) {

    }


    public List<Integer> getUsersByRoleHelper(int roleId){

    }


    public List<User> getUsersDetailsHelper() {

    }

    public List<Item> getAllItemsHelper()  {

    }





    public Inventory getInventoryHelper() {

    }


    public SalesLog getSalesHelper() {

    }


    public Sale getSaleByIdHelper(int saleId) {

    }


    public List<Sale> getSalesToUserHelper(int userId) {

    }


    public void getItemizedSaleByIdHelper(int saleId, Sale sale) throws InvalidIdException {

    }


    public void getItemizedSalesHelper(SalesLog salesLog){

    }


    public List<Integer> getUserAccountsHelper(int userId)  {

    }


    public Account getAccountDetailsHelper(int accountId) {



    }
 */
}

