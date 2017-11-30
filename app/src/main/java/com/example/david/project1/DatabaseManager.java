package com.example.david.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager {

    private Context context;
    private SQLHelper helper;
    private SQLiteDatabase db;
    protected static final String DB_NAME = "items.db";

    protected static final int DB_VERSION = 1;
    protected static final String DB_TABLE = "data";

    private static final String DATE_COL = "_date";
    protected static final String DESC_COL = "description";
    protected static final String PICTURE_COL = "pic";

    private static final String DB_TAG = "DatabaseManager" ;
    private static final String SQL_TAG = "SQLHelper" ;

    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close(); // Closes the database - very important to ensure all data is saved!
    }


    public void addItem(String date,String desc, byte[] blob){
        ContentValues newProduct = new ContentValues();
        newProduct.put(DATE_COL,date);
        newProduct.put(DESC_COL,desc);
        newProduct.put(PICTURE_COL,blob);
        try {
            db.insertOrThrow(DB_TABLE,null,newProduct);
        }
        catch (SQLiteConstraintException sqlce){
            Log.e("Database","Error inserting data");
        }
    }

    public void deleteItem(String date){
        String[] whereArgs = {date};
        String where = DATE_COL + " = ?";
        int rowsDeleted = db.delete(DB_TABLE,where,whereArgs);
        Log.i("Database","Deleted");
    }

    public ArrayList<ListItem> fetchAllProducts() {

        ArrayList<ListItem> products = new ArrayList<>();

        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null, DATE_COL);

        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            String desc = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            Bitmap pic = getImage(image);
            ListItem listItem = new ListItem();
            listItem.setCurrentDateTimeString(date);
            listItem.setDesc(desc);
            listItem.setPic(pic);

            products.add(listItem);
        }

        Log.i(DB_TAG, products.toString());

        cursor.close();
        return products;

    }



    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper(Context c){
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            String createTable = "CREATE TABLE " + DB_TABLE +
                    " (" + DATE_COL + " TEXT PRIMARY KEY, " +
                    DESC_COL +" TEXT, " + PICTURE_COL +" BLOB);";

            Log.d(SQL_TAG, createTable);
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
            Log.w(SQL_TAG, "Upgrade table - drop and recreate it");
        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}