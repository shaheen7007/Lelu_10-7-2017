package com.webquiver.lelu.classes;

/**
 * Created by WebQuiver 04 on 7/26/2017.
 */



//this class is not used
//use if database is required









import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "addressManager";

    // Contacts table name
    private static final String TABLE_ADDRESS = "addresses";

    // Contacts Table Columns names

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_ADDRESS1 = "address1";
    private static final String KEY_PLACE = "place";
    private static final String KEY_DISTRICT = "district";
    private static final String KEY_STATE = "state";
    private static final String KEY_PIN = "pincode";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"  + KEY_ADDRESS1 + " TEXT,"  + KEY_PLACE + " TEXT,"  + KEY_DISTRICT + " TEXT,"  + KEY_STATE + " TEXT,"  + KEY_PIN + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addnewaddr(AddressItem addressItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, addressItem.getNAME());
        values.put(KEY_ID, addressItem.getID());
        values.put(KEY_PH_NO, addressItem.getPHONE());
        values.put(KEY_ADDRESS1, addressItem.getADDRESS1());
        values.put(KEY_PLACE, addressItem.getPLACE());
        values.put(KEY_DISTRICT, addressItem.getDISTRICT());
        values.put(KEY_STATE, addressItem.getSTATE());
        values.put(KEY_PIN, addressItem.getPINCODE());
        // Inserting Row
        db.insert(TABLE_ADDRESS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    AddressItem addressItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADDRESS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO,KEY_ADDRESS1,KEY_PIN,KEY_STATE,KEY_PLACE,KEY_DISTRICT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AddressItem addressItem = new AddressItem(cursor.getString(1), cursor.getString(3),cursor.getString(6),cursor.getString(7),cursor.getString(4),cursor.getString(5), cursor.getString(2),Integer.parseInt(cursor.getString(0)));
        // return contact
        return addressItem;
    }

    // Getting All Contacts
    public List<AddressItem> getAllAddresses() {
        List<AddressItem> AddrList = new ArrayList<AddressItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AddressItem addressItem = new AddressItem();
                addressItem.setID(Integer.parseInt(cursor.getString(0)));
                addressItem.setNAME(cursor.getString(1));
                addressItem.setADDRESS1(cursor.getString(3));
                addressItem.setPLACE(cursor.getString(6));
                addressItem.setDISTRICT(cursor.getString(7));
                addressItem.setPINCODE(cursor.getString(4));
                addressItem.setSTATE(cursor.getString(5));
                addressItem.setPHONE(cursor.getString(2));
                // Adding addressItem to list
                AddrList.add(addressItem);
            } while (cursor.moveToNext());
        }


        // return contact list
        return AddrList;
    }

    // Updating single contact
    public int updateContact(AddressItem contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getNAME());
        values.put(KEY_ID, contact.getID());
        values.put(KEY_PH_NO, contact.getPHONE());
        values.put(KEY_ADDRESS1, contact.getADDRESS1());
        values.put(KEY_PLACE, contact.getPLACE());
        values.put(KEY_DISTRICT, contact.getDISTRICT());
        values.put(KEY_STATE, contact.getSTATE());
        values.put(KEY_PIN, contact.getPINCODE());

        // updating row
        return db.update(TABLE_ADDRESS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    /*
    // Deleting single contact
    public void deleteContact(AddressItem contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
    */


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
