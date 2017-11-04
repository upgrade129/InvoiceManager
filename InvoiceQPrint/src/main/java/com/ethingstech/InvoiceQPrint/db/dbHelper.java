package com.ethingstech.InvoiceQPrint.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Sathesh Kumar M on 4/29/2017.
 */
public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context) {
        super(context, ItemsEntry.DB_NAME, null, ItemsEntry.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String invoiceList = "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_INVOICE_LIST+ " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_INVOICE_PRENAME + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_INVOICE_SERIAL + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_INVOICE_NAME+ " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_INVOICE_NUMBER  + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_INVOICE_DATE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_MONTH + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_Year + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_HOUR + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_MIN + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_SEC + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_VALUE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_BALANCE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_Term + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_RES1 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_RES2 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_RES3 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_status + " TEXT NOT NULL" +
                ");";
        String invoicesTable = "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_INVOICE + " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_PRODUCT_ID + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_QUANTITY + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PRICE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ISGST + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ICGST + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IOFFER + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_TOTAL + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IVPRF + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IVNUM + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IE_RES1 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IE_RES2 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_IE_RES3 + " TEXT NOT NULL" +
                ");";
        String productList =  "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_PRODUCT + " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_ITEM_NAME + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_MRP + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_OFFER + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_SGST + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_CGST + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PR_RES1 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PR_RES2 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PR_RES3 + " TEXT NOT NULL" +
                ");";
        String settings = "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_SETTINGS + " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_NAME + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_ADDLINE1 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ADDLINE2 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_CITY + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PIN + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_GST_NUMBER + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_PHONE_NUMBER + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_INVOICE_PREFIX + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_QUICKPRINT + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_GST_ACTIVE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_MAIL_ID + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ST_RES1 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ST_RES2 + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_ST_RES3 + " TEXT NOT NULL" +
                ");";
        String lastPayment = "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_LASTPAYMENT + " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_LP_IVNUM + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_LP_DATE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_MONTH + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_Year + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_HOUR + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_MIN + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_SEC + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_LP_VALUE + " TEXT NOT NULL" +
                ");";
        String nextPayment = "CREATE TABLE " + ItemsEntry.TaskEntry.TABLE_NEXTPAYMENT + " ( " +
                ItemsEntry.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemsEntry.TaskEntry.COL_NP_IVNUM + " TEXT NOT NULL,"+
                ItemsEntry.TaskEntry.COL_NP_DATE + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_MONTH + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_Year + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_HOUR + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_MIN + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_SEC + " TEXT NOT NULL," +
                ItemsEntry.TaskEntry.COL_NP_DUE + " TEXT NOT NULL" +
                ");";
        db.execSQL(invoicesTable);
        db.execSQL(productList);
        db.execSQL(settings);
        db.execSQL(invoiceList);
        db.execSQL(lastPayment);
        db.execSQL(nextPayment);
        Log.d(TAG, "SQL db Create");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "SQL onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_INVOICE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_LASTPAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + ItemsEntry.TaskEntry.TABLE_NEXTPAYMENT);
    }
}
