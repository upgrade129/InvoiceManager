package com.ethingstech.InvoiceQPrint.db;

import android.provider.BaseColumns;

/**
 * Created by Sathesh Kumar M on 4/29/2017.
 */
public class ItemsEntry {
    public static final String DB_NAME = "SmartInvoice.1.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE_INVOICE_LIST = "invoicesList";
        public static final String COL_INVOICE_NAME = "name";
        public static final String COL_INVOICE_NUMBER = "PhNumber";
        public static final String COL_INVOICE_PRENAME = "prefixSerial";
        public static final String COL_INVOICE_SERIAL = "serialNumber";
        public static final String COL_INVOICE_DATE = "date";
        public static final String COL_INVOICE_MONTH = "month";
        public static final String COL_INVOICE_Year = "year";
        public static final String COL_INVOICE_HOUR = "hour";
        public static final String COL_INVOICE_MIN = "min";
        public static final String COL_INVOICE_SEC = "sec";
        public static final String COL_INVOICE_VALUE = "value";
        public static final String COL_INVOICE_BALANCE = "balance";
        public static final String COL_INVOICE_status = "status";
        public static final String COL_INVOICE_Term = "term";
        public static final String COL_INVOICE_RES1 = "iv_res1";
        public static final String COL_INVOICE_RES2 = "iv_res2";
        public static final String COL_INVOICE_RES3 = "iv_res3";


        public static final String TABLE_INVOICE = "invoicesEntry";
        public static final String COL_PRODUCT_ID = "productname";
        public static final String COL_QUANTITY = "quantity";
        public static final String COL_PRICE = "price";
        public static final String COL_ISGST = "isgst";
        public static final String COL_ICGST = "icgst";
        public static final String COL_IOFFER = "ioffer";
        public static final String COL_TOTAL = "total";
        public static final String COL_IVNUM = "invoice";
        public static final String COL_IVPRF = "invoicePrefix";
        public static final String COL_IE_RES1 = "ie_res1";
        public static final String COL_IE_RES2 = "ie_res2";
        public static final String COL_IE_RES3 = "ie_res3";

        public static final String TABLE_PRODUCT = "productsList";
        public static final String COL_PRODUCT_CODE = "productcode";
        public static final String COL_ITEM_NAME = "productname";
        public static final String COL_AVAIL_QUANTITY = "quantity";
        public static final String COL_MRP = "price";
        public static final String COL_OFFER = "offer";
        public static final String COL_SGST = "sgst";
        public static final String COL_CGST = "cgst";
        public static final String COL_PR_RES1 = "pr_res1";
        public static final String COL_PR_RES2 = "pr_res2";
        public static final String COL_PR_RES3 = "pr_res3";

        public static final String TABLE_SETTINGS = "Settings";
        public static final String COL_NAME = "OrgName";
        public static final String COL_ADDLINE1 = "address1";
        public static final String COL_ADDLINE2 = "address2";
        public static final String COL_CITY = "city";
        public static final String COL_PIN = "pin";
        public static final String COL_GST_NUMBER = "gstnumber";
        public static final String COL_PHONE_NUMBER = "PhoneNumber";
        public static final String COL_MAIL_ID = "eMail";
        public static final String COL_INVOICE_PREFIX = "invoiceprefix";
        public static final String COL_QUICKPRINT = "quickprint";
        public static final String COL_GST_ACTIVE = "gstactive";
        public static final String COL_ST_RES1 = "st_res1";
        public static final String COL_ST_RES2 = "st_res2";
        public static final String COL_ST_RES3 = "st_res3";


        public static final String TABLE_LASTPAYMENT = "LastPayment";
        public static final String COL_LP_IVNUM = "invoiceNumber";
        public static final String COL_LP_DATE = "date";
        public static final String COL_LP_MONTH = "month";
        public static final String COL_LP_Year = "year";
        public static final String COL_LP_HOUR = "hour";
        public static final String COL_LP_MIN = "min";
        public static final String COL_LP_SEC = "sec";
        public static final String COL_LP_VALUE = "value";

        public static final String TABLE_NEXTPAYMENT = "NextPayment";
        public static final String COL_NP_IVNUM = "invoiceNumber";
        public static final String COL_NP_DATE = "date";
        public static final String COL_NP_MONTH = "month";
        public static final String COL_NP_Year = "year";
        public static final String COL_NP_HOUR = "hour";
        public static final String COL_NP_MIN = "min";
        public static final String COL_NP_SEC = "sec";
        public static final String COL_NP_DUE = "due";

    }
}
