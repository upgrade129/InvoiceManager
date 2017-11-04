package com.ethingstech.InvoiceQPrint;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ethingstech.InvoiceQPrint.db.ItemsEntry;
import com.ethingstech.InvoiceQPrint.db.dbHelper;
import com.hoin.btsdk.BluetoothService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillEntryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mItemsListView;
    Double invoiceTotal;
    ArrayAdapter<String> arrayadapter;
    String itemToQuery;
    private ArrayAdapter<String> mAdapter;
    String tempPrice;
    EditText itemName ;
    EditText quantity;
    EditText price;
    private Button addEntry = null;
    private dbHelper mHelper;
    Context mContext;
    AutoCompleteTextView productCode;
    AutoCompleteTextView productInput ;
    EditText priceInput;
    EditText quantityInput;
    EditText pSGST;
    EditText pCGST;
    EditText pOffer;
    //TextView totalBalanceView;
    TextView addressView;
    String invoiceName = "NA";
    String invoicePhNumber = "NA";
    String Name = "NA";
    String address1 = "NA";
    String city = "NA";
    String pincode = "NA";
    String gstnumber = "0";
    String phone = "NA";
    String mailid = "NA";
    String invoiceSerial = "00000";
    String toPrint = "NA";
    String paymentRcvd = "NA";
    String balanceAmount = "NA";
    String macid = "00:00:00:00:00:00";
    String quickPrint = "false";
    String gstActive = "false";
    String geScanCode = "";
    int invoice = 0;
    int paymentCycle = 0;
    double oldQuan = 0.0;
    double newPayment = 0;
    double balancePayment = 0;
    double paymentsofar = 0;
    boolean editInvice = false;
    boolean viewPayment = false;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private AutoCompleteTextView textUniqueCode;
    List<String> productsIDlist;
    List<String> productsNamelist;
    String Date = "01 Jan 2018";
    String Time = "00:00:00";
    String pName;
    String pMAP;
    String iSGST;
    String iCGST;
    String iOffer;

    private PrinterBtConnection connectionObj;


    String[] subjects = new String[] {
            "Android",
            "PHP",
            "Blogger",
            "WordPress",
            "SEO"
    };
    String[] months = new String[]{
            "JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"
    };
    private OnFragmentInteractionListener mListener;

    public BillEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillEntryFragment newInstance(String param1, String param2) {
        BillEntryFragment fragment = new BillEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Item: onResume:");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObj = PrinterBtConnection.getInstance(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        invoiceTotal = 0.0;
        if(macid.contentEquals("00:00:00:00:00:00") || macid == null)
        {
            Log.d(TAG, "Device is not paired yet");
        }
        else
        {
            if(!connectionObj.getConnectionStatus())
                connectionObj.connectPrinter(macid);
        }
        mContext = this.getContext();
        View view = inflater.inflate(R.layout.fragment_bill_entry, container, false);
        addressView = (TextView) view.findViewById(R.id.address);
        mItemsListView = (ListView) view.findViewById(R.id.add_items);
        arrayadapter = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1, subjects);
        mItemsListView.setAdapter(arrayadapter);
        mItemsListView.setLongClickable(true);
        mItemsListView.setOnTouchListener(new View.OnTouchListener() {
             public boolean onTouch(View v, MotionEvent event) {
                return false;
             }
         });
        mItemsListView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                int key;
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_0: key = 0; geScanCode += key; break;
                        case KeyEvent.KEYCODE_1: key = 1;geScanCode += key; break;
                        case KeyEvent.KEYCODE_2: key = 2;geScanCode += key; break;
                        case KeyEvent.KEYCODE_3: key = 3;geScanCode += key; break;
                        case KeyEvent.KEYCODE_4: key = 4;geScanCode += key; break;
                        case KeyEvent.KEYCODE_5: key = 5;geScanCode += key; break;
                        case KeyEvent.KEYCODE_6: key = 6;geScanCode += key; break;
                        case KeyEvent.KEYCODE_7: key = 7;geScanCode += key; break;
                        case KeyEvent.KEYCODE_8: key = 8;geScanCode += key; break;
                        case KeyEvent.KEYCODE_9: key = 9;geScanCode += key; break;
                        case KeyEvent.KEYCODE_ENTER: {
                            Log.d(TAG, "Key ENTER: " + geScanCode);
                            if (searchProductListForScan(geScanCode)) {

                                Double finalValue;
                                Double tax = 0.0;
                                String item = pName;
                                String quan = "1";
                                String value = pMAP;
                                String sSGST = iSGST;
                                String sCGST = iCGST;
                                String offer = iOffer;
                                if (gstActive.contentEquals("false")) {
                                    sSGST = "0";
                                    sCGST = "0";
                                }
                                if (item.length() != 0) {
                                    Double count = isListedAlready(item);
                                    if (count != 0.0) {
                                        Log.d(TAG, "Listed Already : " + count);
                                        if (quan.length() == 0) quan = "0";
                                        count = count + Double.parseDouble(quan);
                                        if (value.length() == 0) value = tempPrice;
                                        finalValue = count * Double.parseDouble(value);
                                        finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                        tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                        SQLiteDatabase db = mHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                        values.put(ItemsEntry.TaskEntry.COL_QUANTITY, count);
                                        values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                        values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                        values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                        values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                        values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                        values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                        values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
                                        String[] selectionArgs = {item};
                                        db.update(ItemsEntry.TaskEntry.TABLE_INVOICE, values, selection, selectionArgs);
                                        db.close();
                                        updateProductDB(item, quan, false);
                                    } else {
                                        if (quan.length() == 0) quan = "0";
                                        if (value.length() == 0) value = "0";
                                        finalValue = Double.parseDouble(quan) * Double.parseDouble(value);
                                        finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                        tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                        Log.d(TAG, "Tax: " + tax);
                                        SQLiteDatabase db = mHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                        values.put(ItemsEntry.TaskEntry.COL_QUANTITY, quan);
                                        values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                        values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                        values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                        values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                        values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                        values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                        values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                        values.put(ItemsEntry.TaskEntry.COL_IE_RES1, "false");
                                        values.put(ItemsEntry.TaskEntry.COL_IE_RES2, "false");
                                        values.put(ItemsEntry.TaskEntry.COL_IE_RES3, "false");
                                        db.insert(ItemsEntry.TaskEntry.TABLE_INVOICE, null, values);
                                        db.close();
                                        updateProductDB(item, quan, false);
                                    }
                                    Log.d(TAG, "Task to add: " + item);
                                    updateUI();
                                } else
                                    Log.d(TAG, "Empty List:");
                                Log.d(TAG, "Check Product List: OK");
                            }
                        }
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        mItemsListView.requestFocus();
                                    }
                                }
                                , 50 // 50...for delay in my case
                        );
                        geScanCode = "";
                            break;
                    }
                }
                return false;
            }
            });
        mItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Update Item"+parent.getItemAtPosition(position));
                try {
                    updateItem(String.valueOf(parent.getItemAtPosition(position)));
                }
                catch (CursorIndexOutOfBoundsException e)
                {
                    Log.d(TAG, "Update Item"+ "Index OOB");
                }

            }
        });
        mHelper = new dbHelper(getContext());
        getSettingsInfo();
        String user = getArguments().getString("invoice");
        invoiceName = getArguments().getString("InvoiceTo");
        invoicePhNumber = getArguments().getString("InvoicePh");
        Date = getArguments().getString("ivDate");
        Time = getArguments().getString("ivTime");
        try {
            if(user.contentEquals("New")) {
                invoice = Integer.parseInt(getArguments().getString("invoiceNumber")) + 1;
                editInvice = false;
                paymentCycle = 0;
                balancePayment = 0;
                invoiceSerial = invoiceSerial+invoice;
            }
            else
            {
                editInvice = true;
                invoice = Integer.parseInt(user);
                paymentCycle = Integer.parseInt(getArguments().getString("PaymentCycle"));
                balancePayment = Double.parseDouble(getArguments().getString("balancePayment"));
                invoiceSerial = getArguments().getString("prefix");
            }
        }catch(NullPointerException e)
        {
        }
        Log.d(TAG, "Invoice Name :" + invoiceName);
        Log.d(TAG, "Invoice List :" + invoicePhNumber);
        Log.d(TAG, "Invoice List :" + user);
        Log.d(TAG, "Invoice Number :" + invoice);

        addressView.setText
                (       "Invoice To: "+invoiceName +"\n"+
                        "Phone Number: "+invoicePhNumber +"\n"+
                        "Date: " + Date+"                      Time: "+Time+"\n"+
                        "Invoice No:"+invoiceSerial+"\n"+
                        "--------------------------------------------------------------");
        updateUI();

        final Button backInvoice = (Button)view.findViewById(R.id.IV_Back);

         backInvoice.setOnClickListener(new View.OnClickListener() {
                  public void onClick(View v) {
                      if(!editInvice) {
                          AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                          dialog.setTitle("Do you want to Discard the Invoice?");
                          dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  deleteInvoice();
                                  getActivity().getSupportFragmentManager()
                                          .beginTransaction()
                                          .replace(R.id.fragment, new InvoiceEntryFragment())
                                          .addToBackStack(null).commitAllowingStateLoss();
                              }
                          });
                          dialog.setNegativeButton("Cancel", null);
                          dialog.show();
                      }
                      else
                      {
                          getActivity().getSupportFragmentManager()
                                  .beginTransaction()
                                  .replace(R.id.fragment, new InvoiceEntryFragment())
                                  .addToBackStack(null).commitAllowingStateLoss();
                      }
                  }
        });

        final Button saveInvoice = (Button)view.findViewById(R.id.IV_Save);
        saveInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(invoiceTotal !=0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Do you want to Save Invoice?");
                    final TextView payment = new TextView(getActivity());
                    final TextView balance = new TextView(getActivity());
                    final TextView nextPay = new TextView(getActivity());
                    payment.setText("Payment Received ₹");
                    balance.setText("Balance to Pay ₹");
                    nextPay.setText("Next Payment Date");
                    final EditText amount = new EditText(getActivity());
                    final EditText balancePay = new EditText(getActivity());
                    final EditText nextPayment = new EditText(getActivity());
                    amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    balancePay.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    nextPayment.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE|InputType.TYPE_CLASS_NUMBER );
                    //amount.setText(String.valueOf(invoiceTotal));
                    //balancePay.setText(String.valueOf(0.0));
                    final RadioButton pending = new RadioButton(getActivity());
                    final RadioButton completed = new RadioButton(getActivity());
                    LinearLayout ll=new LinearLayout(getContext());
                    completed.setText("Invoice Complete");
                    pending.setText("Invoice Pending");
                    nextPayment.setText("DDMMYYYY");
                    nextPayment.setEnabled(false);
                    balancePay.setEnabled(false);
                    completed.setChecked(true);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    amount.setText(String.valueOf(invoiceTotal - paymentsofar));
                    balancePay.setText(String.valueOf(invoiceTotal - paymentsofar));
                    ll.addView(payment);
                    ll.addView(amount);
                    /*ll.addView(balance);
                    ll.addView(balancePay);
                    ll.addView(completed);
                    ll.addView(pending);
                    ll.addView(nextPay);
                    ll.addView(nextPayment);*/
                    dialog.setView(ll);
                    balancePay.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "balancePay click");
                            double b = invoiceTotal-(paymentsofar+Double.parseDouble(amount.getText().toString()));
                            Log.d(TAG, "balancePay "+b);
                            balancePay.setText(String.valueOf(b));
                            if(b>0.0)
                            {
                                completed.setChecked(false);
                                pending.setChecked(true);
                                nextPayment.setEnabled(true);
                            }
                            else
                            {
                                completed.setChecked(true);
                                pending.setChecked(false);
                                nextPayment.setEnabled(false);
                            }
                        }
                    });
                    completed.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "completed click");
                            double b = invoiceTotal-(paymentsofar+Double.parseDouble(amount.getText().toString()));                            Log.d(TAG, "balancePay "+b);
                            completed.setChecked(true);
                            pending.setChecked(false);
                            nextPayment.setEnabled(false);
                            balancePay.setText(String.valueOf(b));
                        }
                    });
                    pending.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "pending click");
                            double b = invoiceTotal-(paymentsofar+Double.parseDouble(amount.getText().toString()));                            Log.d(TAG, "balancePay "+b);
                            completed.setChecked(false);
                            pending.setChecked(true);
                            nextPayment.setEnabled(true);
                            balancePay.setText(String.valueOf(b));
                        }
                    });

                    dialog.setPositiveButton("Print", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String statusPrint;
                            boolean updatePayment = false;
                            if((invoiceTotal - paymentsofar) != Double.parseDouble(amount.getText().toString()))
                            {
                                updatePayment = true;
                            }

                            double b = invoiceTotal-(paymentsofar+Double.parseDouble(amount.getText().toString()));                            Log.d(TAG, "balancePay "+b);
                            balancePay.setText(String.valueOf(b));
                            if(b>0.0)
                            {
                                completed.setChecked(false);
                                pending.setChecked(true);
                                nextPayment.setEnabled(true);
                            }
                            else
                            {
                                completed.setChecked(true);
                                pending.setChecked(false);
                                nextPayment.setEnabled(false);
                            }

                            if(completed.isChecked())
                            {
                                statusPrint = "completed";
                            }
                            else
                            {
                                statusPrint = "pending";
                            }
                            Calendar c = Calendar.getInstance();
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_PRENAME, invoiceSerial);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_SERIAL, invoice);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_NAME, invoiceName);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_NUMBER, invoicePhNumber);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_DATE, c.get(Calendar.DATE));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_MONTH, c.get(Calendar.MONTH));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_Year, c.get(Calendar.YEAR));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_HOUR, c.get(Calendar.HOUR_OF_DAY));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_MIN, c.get(Calendar.MINUTE));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_SEC, c.get(Calendar.SECOND));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_VALUE, invoiceTotal);
                            paymentRcvd = amount.getText().toString();
                            balanceAmount = String.valueOf(invoiceTotal - Double.parseDouble(amount.getText().toString()));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_BALANCE, String.valueOf(b));
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_Term, paymentCycle+1);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_status, statusPrint);
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_RES1, "false");
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_RES2, "false");
                            values.put(ItemsEntry.TaskEntry.COL_INVOICE_RES3, "false");

                            if(editInvice)
                            {
                                String selection = ItemsEntry.TaskEntry.COL_INVOICE_SERIAL + " = ?";
                                String[] selectionArgs = {String.valueOf(invoice)};
                                if(updatePayment)
                                db.update(ItemsEntry.TaskEntry.TABLE_INVOICE_LIST,values,selection,selectionArgs);
                            }
                            else
                            {
                                db.insert(ItemsEntry.TaskEntry.TABLE_INVOICE_LIST, null, values);
                                updatePayment = true;
                            }
                            db.close();

                            /*Update last payment table*/
                            if( updatePayment)
                            {
                                SQLiteDatabase db2 = mHelper.getWritableDatabase();
                                ContentValues values2 = new ContentValues();
                                values2.put(ItemsEntry.TaskEntry.COL_LP_IVNUM, invoice);
                                values2.put(ItemsEntry.TaskEntry.COL_LP_DATE, c.get(Calendar.DATE));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_MONTH, c.get(Calendar.MONTH));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_Year, c.get(Calendar.YEAR));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_HOUR, c.get(Calendar.HOUR));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_MIN, c.get(Calendar.MINUTE));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_SEC, c.get(Calendar.SECOND));
                                values2.put(ItemsEntry.TaskEntry.COL_LP_VALUE, paymentRcvd);
                                db2.insert(ItemsEntry.TaskEntry.TABLE_LASTPAYMENT, null, values2);
                                db2.close();
                            }

                            printInvoice();

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment, new InvoiceEntryFragment())
                                    .addToBackStack(null).commitAllowingStateLoss();
                        }
                    });
                    dialog.setNegativeButton("Cancel", null);
                    dialog.show();
                }
           }
        });
        final Button addIVItem = (Button)view.findViewById(R.id.IV_Add);
        if(editInvice)
            addIVItem.setEnabled(false);
        else
            addIVItem.setEnabled(true);
        addIVItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quickPrint.contentEquals("true"))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    final TextView itemCode = new TextView(getActivity());
                    final TextView quantity = new TextView(getActivity());
                    itemCode.setText("Item Code");
                    quantity.setText("Quantity");
                    final EditText enterItemCode = new EditText(getContext());
                    final EditText enterQuantity = new EditText(getActivity());
                    enterItemCode.setInputType(InputType.TYPE_CLASS_NUMBER);
                    enterQuantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    LinearLayout ll=new LinearLayout(getContext());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(itemCode);
                    ll.addView(enterItemCode);
                    ll.addView(quantity);
                    ll.addView(enterQuantity);
                    dialog.setView(ll);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
                                    String[] selectionArgs = {enterItemCode.getText().toString()};
                                    Log.d(TAG, "Product code:"+selectionArgs[0]);
                                    SQLiteDatabase db = mHelper.getReadableDatabase();
                                    Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                                            new String[]{ItemsEntry.TaskEntry._ID,
                                                    ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                                                    ItemsEntry.TaskEntry.COL_ITEM_NAME,
                                                    ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                                                    ItemsEntry.TaskEntry.COL_MRP,
                                                    ItemsEntry.TaskEntry.COL_OFFER,
                                                    ItemsEntry.TaskEntry.COL_SGST,
                                                    ItemsEntry.TaskEntry.COL_CGST,
                                            },
                                            selection, selectionArgs, null, null, null);
                                    cursor.moveToNext();
                                    Double finalValue = 0.0;
                                    Double tax = 0.0;
                                    String code = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE));
                                    String item = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME));
                                    String quan = enterQuantity.getText().toString();
                                    String value = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP));
                                    String sSGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST));
                                    String sCGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST));
                                    String offer = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER));
                                    db.close();
                                    cursor.close();
                                    if (code.length() != 0) {
                                        Double count = isListedAlready(code);
                                        if (count == 0.0) {
                                            if (quan.length() == 0) quan = "0";
                                            if (value.length() == 0) value = "0";
                                            finalValue = Double.parseDouble(quan) * Double.parseDouble(value);
                                            finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                            tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                            Log.d(TAG, "Tax: " + tax);
                                            db = mHelper.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                            values.put(ItemsEntry.TaskEntry.COL_QUANTITY, quan);
                                            values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                            values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                            values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                            values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                            values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                            values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                            values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                            values.put(ItemsEntry.TaskEntry.COL_IE_RES1, "false");
                                            values.put(ItemsEntry.TaskEntry.COL_IE_RES2, "false");
                                            values.put(ItemsEntry.TaskEntry.COL_IE_RES3, "false");
                                            db.insert(ItemsEntry.TaskEntry.TABLE_INVOICE, null, values);
                                            db.close();
                                            updateProductDB(item,quan,false);
                                            updateUI();
                                        }
                                        else
                                        {
                                            Log.d(TAG, "Listed Already : " + count);
                                            if (quan.length() == 0) quan = "0";
                                            count = count + Double.parseDouble(quan);
                                            if (value.length() == 0) value = tempPrice;
                                            finalValue = count * Double.parseDouble(value);
                                            finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                            tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                            db = mHelper.getWritableDatabase();
                                            ContentValues values = new ContentValues();
                                            values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                            values.put(ItemsEntry.TaskEntry.COL_QUANTITY, count);
                                            values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                            values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                            values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                            values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                            values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                            values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                            values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                            selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
                                            String[] selection_Args = {item};
                                            db.update(ItemsEntry.TaskEntry.TABLE_INVOICE, values, selection, selection_Args);
                                            db.close();
                                            updateProductDB(item,quan,false);
                                            updateUI();
                                        }
                                    }
                                }
                            });
                    dialog.setNegativeButton("Cancel", null);
                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
                    View dialogView = li.inflate(R.layout.add_item_dialog, null);
                    alertDialogBuilder.setTitle("Add Item");
                    alertDialogBuilder.setView(dialogView);
                    productCode = (AutoCompleteTextView) dialogView.findViewById(R.id.Unique_Code);
                    productInput = (AutoCompleteTextView) dialogView.findViewById(R.id.product_input);
                    priceInput = (EditText) dialogView.findViewById(R.id.price_input);
                    quantityInput = (EditText) dialogView.findViewById(R.id.quantity_input);
                    pSGST = (EditText) dialogView.findViewById(R.id.sgst_input);
                    pCGST = (EditText) dialogView.findViewById(R.id.cgst_input);
                    pOffer = (EditText) dialogView.findViewById(R.id.offer_input);
                    if(gstActive.contentEquals("false")) {
                        pSGST.setEnabled(false);
                        pCGST.setEnabled(false);
                    }
                    searchProductList("null");
                    addProductsToAutoComplete(productsIDlist);
                    addNameToAutoComplete(productsNamelist);

                    productCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "productCode.setOnItemClickListener:" + position);
                            Log.d(TAG, "productCode.setOnItemClickListener Text:" + parent.getItemAtPosition(position));
                            searchProductList(parent.getItemAtPosition(position).toString());
                        }
                    });
                    productInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "productInput.setOnItemClickListener:" + position);
                            Log.d(TAG, "productInput.setOnItemClickListener Text:" + parent.getItemAtPosition(position));
                            searchProductNameList(parent.getItemAtPosition(position).toString());
                        }
                    });

                    productInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Check Product List:");
                            searchProductList(productCode.getText().toString());
                        }
                    });
                    quantityInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Check Product List:");
                            searchProductList(productCode.getText().toString());
                        }
                    });
                    priceInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Check Product List:");
                            searchProductList(productCode.getText().toString());
                        }
                    });
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Double finalValue = 0.0;
                                            Double tax = 0.0;
                                            String item = productInput.getText().toString();
                                            String quan = quantityInput.getText().toString();
                                            String value = priceInput.getText().toString();
                                            String sSGST = pSGST.getText().toString();
                                            String sCGST = pCGST.getText().toString();
                                            String offer = pOffer.getText().toString();
                                            if(gstActive.contentEquals("false")) {
                                                sSGST = "0";
                                                sCGST = "0";
                                            }
                                            if (item.length() != 0) {
                                                Double count = isListedAlready(item);
                                                if (count != 0.0) {
                                                    Log.d(TAG, "Listed Already : " + count);
                                                    if (quan.length() == 0) quan = "0";
                                                    count = count + Double.parseDouble(quan);
                                                    if (value.length() == 0) value = tempPrice;
                                                    finalValue = count * Double.parseDouble(value);
                                                    finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                                    tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                                    SQLiteDatabase db = mHelper.getWritableDatabase();
                                                    ContentValues values = new ContentValues();
                                                    values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                                    values.put(ItemsEntry.TaskEntry.COL_QUANTITY, count);
                                                    values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                                    values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                                    values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                                    values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                                    values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                                    values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                                    values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                                    String selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
                                                    String[] selectionArgs = {item};
                                                    db.update(ItemsEntry.TaskEntry.TABLE_INVOICE, values, selection, selectionArgs);
                                                    db.close();
                                                    updateProductDB(item,quan,false);
                                                } else {
                                                    if (quan.length() == 0) quan = "0";
                                                    if (value.length() == 0) value = "0";
                                                    finalValue = Double.parseDouble(quan) * Double.parseDouble(value);
                                                    finalValue -= ((finalValue / 100) * Double.parseDouble(offer));
                                                    tax = ((finalValue / 100) * (Double.parseDouble(sSGST) + Double.parseDouble(sCGST)));
                                                    Log.d(TAG, "Tax: " + tax);
                                                    SQLiteDatabase db = mHelper.getWritableDatabase();
                                                    ContentValues values = new ContentValues();
                                                    values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                                                    values.put(ItemsEntry.TaskEntry.COL_QUANTITY, quan);
                                                    values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                                                    values.put(ItemsEntry.TaskEntry.COL_ISGST, sSGST);
                                                    values.put(ItemsEntry.TaskEntry.COL_ICGST, sCGST);
                                                    values.put(ItemsEntry.TaskEntry.COL_IOFFER, offer);
                                                    values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                                                    values.put(ItemsEntry.TaskEntry.COL_IVPRF, invoiceSerial);
                                                    values.put(ItemsEntry.TaskEntry.COL_IVNUM, invoice);
                                                    values.put(ItemsEntry.TaskEntry.COL_IE_RES1, "false");
                                                    values.put(ItemsEntry.TaskEntry.COL_IE_RES2, "false");
                                                    values.put(ItemsEntry.TaskEntry.COL_IE_RES3, "false");
                                                    db.insert(ItemsEntry.TaskEntry.TABLE_INVOICE, null, values);
                                                    db.close();
                                                    updateProductDB(item,quan,false);
                                                }
                                                Log.d(TAG, "Task to add: " + item);
                                                updateUI();
                                            } else
                                                Log.d(TAG, "Empty List:");
                                            Log.d(TAG, "Check Product List: OK");
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            Log.d(TAG, "Check Product List: Cancel");
                                            dialog.cancel();
                                        }
                                    });
                    //AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialogBuilder.show();
                }
        }
        });

        mItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                if(!editInvice) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Do you want to Delete the item?");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            View parentView = getView();
                            TextView taskTextView = (TextView) parentView.findViewById(R.id.textViewlist);
                            String task = String.valueOf(taskTextView.getText());
                            String selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
                            updateProductDB(
                                    task.substring(0, task.indexOf(':')),
                                    isListedAlready(task.substring(0, task.indexOf(':'))).toString(),
                                    true);
                            String[] selectionArgs = {task.substring(0, task.indexOf(':'))};
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            db.delete(ItemsEntry.TaskEntry.TABLE_INVOICE, selection, selectionArgs);
                            db.close();
                            updateUI();
                        }
                    });
                    dialog.setNegativeButton("Cancel", null);
                    dialog.show();
                }
                return true;
            }
        });
        itemName = new EditText(this.getActivity());
        quantity = new EditText(this.getActivity());
        price = new EditText(this.getActivity());
        pSGST = new EditText(this.getActivity());
        pCGST = new EditText(this.getActivity());
        return view;
    }


    class ClickEvent implements View.OnClickListener {

        public void onClick(View v) {
            Log.d(TAG, "onClick:");

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "List: onDestroy:");
    }

    private void addProductsToAutoComplete(List<String> productsIDfromDB) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this.getContext(),
                        android.R.layout.simple_dropdown_item_1line, productsIDfromDB);

        productCode.setAdapter(adapter);
    }
    private void addNameToAutoComplete(List<String> productsIDfromDB) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this.getContext(),
                        android.R.layout.simple_dropdown_item_1line, productsIDfromDB);

        productInput.setAdapter(adapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   private void updateUI() {
       invoiceTotal = 0.0;
       ArrayList<String> taskList = new ArrayList<>();
       SQLiteDatabase db = mHelper.getReadableDatabase();
       Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE,
               new String[]{ItemsEntry.TaskEntry._ID,
                       ItemsEntry.TaskEntry.COL_PRODUCT_ID,
                       ItemsEntry.TaskEntry.COL_QUANTITY,
                       ItemsEntry.TaskEntry.COL_PRICE,
                       ItemsEntry.TaskEntry.COL_ISGST,
                       ItemsEntry.TaskEntry.COL_ICGST,
                       ItemsEntry.TaskEntry.COL_IOFFER,
                       ItemsEntry.TaskEntry.COL_TOTAL,
                       ItemsEntry.TaskEntry.COL_IVNUM,
               },
               null, null, null, null, null);
       while (cursor.moveToNext()) {
           int invoiceNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IVNUM)));
           if (invoiceNumber == invoice) {
               String StringToPrint;
               int bItem = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_ID)).length();
               int bQuantity = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY)).length();
               int bPrice = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE)).length();
               int bISGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ISGST)).length();
               int bICGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ICGST)).length();
               int bOffer = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IOFFER)).length();
               int bTotal = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_TOTAL)).length();
               invoiceTotal = invoiceTotal + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_TOTAL)));
               StringToPrint = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_ID));
               StringToPrint += ":\n";
               StringToPrint += "                         ";
               /*for (int i = 0; i <= (25 - bItem); i++) {
                   StringToPrint += " ";
               }*/
               StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY));

               for (int i = 0; i <= (10 - bQuantity); i++) {
                   StringToPrint += " ";
               }
               StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE));
               for (int i = 0; i <= (7 - bPrice); i++) {
                   StringToPrint += " ";
               }
               StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IOFFER))+"%";
               for (int i = 0; i <= (10 - bOffer); i++) {
                   StringToPrint += " ";
               }

               double total = (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE))))*
                       Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY)));
               total = total - ((total/100)*(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IOFFER)))));

               StringToPrint += "₹ ";
               StringToPrint += String.format("%.2f", total);//String Price = String.format("%.2f", total);
               StringToPrint += "\n";
               if(gstActive.contentEquals("true"))
               {
                   double gst = (total / 100) * (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ISGST))));
                   StringToPrint += "                                   SGST:   ";
                   StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ISGST));
                   StringToPrint += "%          ₹" + String.format("%.2f", gst) + "\n";
               /*for (int i = 0; i <= (10 - bISGST); i++) {
                   StringToPrint += " ";
               }*/
                   gst = (total / 100) * (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ICGST))));
                   StringToPrint += "                                   CGST:   ";
                   StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ICGST));
                   StringToPrint += "%          ₹" + String.format("%.2f", gst) + "\n";
               /*for (int i = 0; i <= (10 - bICGST); i++) {
                   StringToPrint += " ";
               }*/
               }
               Log.d(TAG, "Added Items: " + StringToPrint);
               taskList.add(StringToPrint);
           }

   }
       db.close();
        cursor.close();

       taskList.add("                                                  Total ₹: "+ invoiceTotal);
       paymentsofar = 0.0;
       if(editInvice)
       {
           int count =0;
           String StringToPrint;
           String selection = ItemsEntry.TaskEntry.COL_LP_IVNUM + " = ?";
           String[] selectionArgs = {String.valueOf(invoice)};
           SQLiteDatabase db2 = mHelper.getReadableDatabase();
           Cursor cursor2 = db2.query(ItemsEntry.TaskEntry.TABLE_LASTPAYMENT,
                   new String[]{ItemsEntry.TaskEntry._ID,
                           ItemsEntry.TaskEntry.COL_LP_DATE,
                           ItemsEntry.TaskEntry.COL_LP_MONTH,
                           ItemsEntry.TaskEntry.COL_LP_Year,
                           ItemsEntry.TaskEntry.COL_LP_HOUR,
                           ItemsEntry.TaskEntry.COL_LP_MIN,
                           ItemsEntry.TaskEntry.COL_LP_SEC,
                           ItemsEntry.TaskEntry.COL_LP_VALUE,
                   },
                   selection, selectionArgs, null, null, null);
           while (cursor2.moveToNext()) {
               count+=1;
               StringToPrint = "Payment"+count+":                                       ";
               StringToPrint+="     ₹:";
               StringToPrint+=cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_VALUE))+"\n";

               StringToPrint += cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_DATE)) + ":"+
                       cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_MONTH)) + ":"+
                       cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_Year));
               StringToPrint+="/";
               StringToPrint += cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_HOUR)) + ":"+
                       cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_MIN)) + ":"+
                       cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_SEC));
               paymentsofar += Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(ItemsEntry.TaskEntry.COL_LP_VALUE)));
               taskList.add(StringToPrint);
           }
           cursor2.close();
           db2.close();
           taskList.add("                                                  Balance ₹: "+ String.format("%.2f", (invoiceTotal-paymentsofar)));
       }
        if (mAdapter == null) {
            Log.d(TAG, "mAdapter is null ");
            mAdapter = new ArrayAdapter<>(this.getContext(),
                    R.layout.add_items_text,
                    R.id.textViewlist,
                    taskList);
            mItemsListView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "update list ");
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

       //totalBalanceView.setText(invoiceTotal.toString());
    }
    public void updateItem(String task) {
        Log.d(TAG, "updateItem:"+task);
        oldQuan = 0.0;
        final TextView itemTitle = new TextView(this.getActivity());
        final TextView qantityTitle = new TextView(this.getActivity());
        final TextView priceTitle = new TextView(this.getActivity());
        final TextView sGST = new TextView(this.getActivity());
        final TextView cGST = new TextView(this.getActivity());
        final TextView offer = new TextView(this.getActivity());
        itemTitle.setText("Product Name");
        qantityTitle.setText("Quantity");
        priceTitle.setText("Price ₹");
        sGST.setText("SGST(%)");
        cGST.setText("CGST(%)");
        offer.setText("Discount(%)");

        /*First, do query based on the ITEM_NAME of the selected item name from the List View*/
        /*Get the Item name only from the string of List View using the char ":"*/
        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
        String[] selectionArgs = {task.substring(0, task.indexOf(':'))};
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_ID,
                        ItemsEntry.TaskEntry.COL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_PRICE,
                        ItemsEntry.TaskEntry.COL_ISGST,
                        ItemsEntry.TaskEntry.COL_ICGST,
                        ItemsEntry.TaskEntry.COL_IOFFER,
                        ItemsEntry.TaskEntry.COL_TOTAL,
                },
                selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        db.close();
        int idx2 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_ID);
        final int idx3 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY);
        int idx4 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE);
        int idx5 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ISGST);
        int idx6 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ICGST);
        int idx7 = cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IOFFER);

        /*Now Item name, quantity and price details of the selected item is ready*/
        /*Apply all information in dialog when it's show*/
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setTitle("Values");
        final EditText itemName = new EditText(this.getActivity());
        final EditText quantity = new EditText(this.getContext());
        final EditText itemSGST = new EditText(this.getContext());
        final EditText itemCGST = new EditText(this.getContext());
        final EditText itemOffer = new EditText(this.getContext());
        final EditText price = new EditText(this.getActivity());
        itemName.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        price.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        itemSGST.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        itemCGST.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        itemOffer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout ll=new LinearLayout(this.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        itemToQuery = String.valueOf(cursor.getString(idx2)); /*Should use this value when update the entry*/
        oldQuan = cursor.getShort(idx3);
        itemName.setText(cursor.getString(idx2));
        quantity.setText(cursor.getString(idx3));
        price.setText(cursor.getString(idx4));
        itemSGST.setText(cursor.getString(idx5));
        itemCGST.setText(cursor.getString(idx6));
        itemOffer.setText(cursor.getString(idx7));
        cursor.close();
        ll.addView(itemTitle);
        ll.addView(itemName);
        ll.addView(qantityTitle);
        ll.addView(quantity);
        ll.addView(sGST);
        ll.addView(itemSGST);
        ll.addView(cGST);
        ll.addView(itemCGST);
        ll.addView(offer);
        ll.addView(itemOffer);
        ll.addView(priceTitle);
        ll.addView(price);
        dialog.setView(ll);
        dialog.setCancelable(false);
        /*Make the changes and click Ã„dd button*/
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = String.valueOf(itemName.getText());
                String quan = String.valueOf(quantity.getText());
                String value = String.valueOf(price.getText());
                String uSGST = String.valueOf(itemSGST.getText());
                String uCGST = String.valueOf(itemCGST.getText());
                String ioffer = String.valueOf(itemOffer.getText());
                if(gstActive.contentEquals("false")) {
                    uSGST = "0";
                    uCGST = "0";
                }
                Double quanToUpdate = Double.parseDouble(quan);

                Log.d(TAG, "Update new: " + quanToUpdate);
                Log.d(TAG, "Update old: " + oldQuan);
                if(quanToUpdate > oldQuan)
                {
                    Log.d(TAG, "Update false: " + quanToUpdate);
                    quanToUpdate = quanToUpdate - oldQuan;
                    updateProductDB(item, Double.toString(quanToUpdate), false);

                }
                else
                {
                    Log.d(TAG, "Update true: " + quanToUpdate);
                    quanToUpdate = oldQuan - quanToUpdate;
                    updateProductDB(item, Double.toString(quanToUpdate), true);

                }
                Double finalValue=Double.parseDouble(quan) * Double.parseDouble(value);
                finalValue -= ((finalValue/100) * Double.parseDouble(ioffer));
                Double tax = (finalValue/100) *(Double.parseDouble(uCGST)+Double.parseDouble(uSGST));
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ItemsEntry.TaskEntry.COL_PRODUCT_ID, item);
                values.put(ItemsEntry.TaskEntry.COL_QUANTITY, quan);
                values.put(ItemsEntry.TaskEntry.COL_PRICE, value);
                values.put(ItemsEntry.TaskEntry.COL_ISGST, uSGST);
                values.put(ItemsEntry.TaskEntry.COL_ICGST, uCGST);
                values.put(ItemsEntry.TaskEntry.COL_IOFFER, ioffer);
                values.put(ItemsEntry.TaskEntry.COL_TOTAL, finalValue + tax);
                String selection = ItemsEntry.TaskEntry.COL_PRODUCT_ID + " = ?";
                String[] selectionArgs = {itemToQuery};
                db.update(ItemsEntry.TaskEntry.TABLE_INVOICE,values,selection,selectionArgs);
                db.close();
                Log.d(TAG, "Task to add: " + item);
                updateUI();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public Double isListedAlready(String task) {
        Log.d(TAG, "checkListItem:"+task);
        String selection = task + " = ?";
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_ID,
                        ItemsEntry.TaskEntry.COL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_PRICE,
                        ItemsEntry.TaskEntry.COL_ISGST,
                        ItemsEntry.TaskEntry.COL_ICGST,
                        ItemsEntry.TaskEntry.COL_TOTAL,
                },
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            String bItem = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_ID));
            if(bItem.contentEquals(task))
            {
                tempPrice = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE));
                return Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY)));
            }
        }
        db.close();
        cursor.close();
        return 0.0;
    }
    public String searchProductNameList(String serachText)
    {
        productsIDlist = new ArrayList<>();
        productsNamelist = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_OFFER,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String productName = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME));
            if(productName.contentEquals(serachText))
            {
                productCode.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE)));
                priceInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP)));
                pSGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
                pCGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST)));
                pSGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
                pOffer.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER)));
                return cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP));
            }
        }
        db.close();
        cursor.close();
        return "0";
    }
    public String getQuickProductList(String task)
    {
        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
        String[] selectionArgs = {task};
        Log.d(TAG, "Product code:"+selectionArgs[0]);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_OFFER,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                },
                selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        cursor.close();
        db.close();
        return "0";
    }

    public boolean searchProductList(String serachText)
    {
        Log.d(TAG, "searchProductList:" + serachText);
        boolean foundProduct = false;
        productsIDlist = new ArrayList<>();
        productsNamelist = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_OFFER,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            productsIDlist.add(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE)));
            productsNamelist.add(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME)));
            String productName = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE));
            if(productName.contentEquals(serachText))
            {
                productInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME)));
                priceInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP)));
                pSGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
                pCGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST)));
                pSGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
                pOffer.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER)));
                foundProduct = true;
                Log.d(TAG, "Product Found");
            }
        }
        cursor.close();
        db.close();
        return foundProduct;
    }
    public boolean searchProductListForScan(String serachText)
    {
        Log.d(TAG, "searchProductList:" + serachText);
        boolean foundProduct = false;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_OFFER,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String pCode = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE));
            if(pCode.contentEquals(serachText))
            {
                pName = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME));
                pMAP = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP));
                iSGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST));
                iCGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST));
                iOffer = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER));
                foundProduct = true;
                Log.d(TAG, "Product Found");
            }
        }
        cursor.close();
        db.close();
        return foundProduct;
    }

    void getSettingsInfo()
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_SETTINGS,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_NAME,
                        ItemsEntry.TaskEntry.COL_ADDLINE1,
                        ItemsEntry.TaskEntry.COL_ADDLINE2,
                        ItemsEntry.TaskEntry.COL_CITY,
                        ItemsEntry.TaskEntry.COL_PIN,
                        ItemsEntry.TaskEntry.COL_GST_NUMBER,
                        ItemsEntry.TaskEntry.COL_PHONE_NUMBER,
                        ItemsEntry.TaskEntry.COL_MAIL_ID,
                        ItemsEntry.TaskEntry.COL_INVOICE_PREFIX,
                        ItemsEntry.TaskEntry.COL_GST_ACTIVE,
                        ItemsEntry.TaskEntry.COL_QUICKPRINT,
                },
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            Name = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_NAME));
            address1 = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ADDLINE1));
            macid = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ADDLINE2));
            city = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CITY));
            pincode = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PIN));
            gstnumber = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_GST_NUMBER));
            phone = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PHONE_NUMBER));
            mailid = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MAIL_ID));
            invoiceSerial = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_PREFIX));
            quickPrint = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUICKPRINT));
            gstActive = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_GST_ACTIVE));
        }
        cursor.close();
        db.close();
    }
    public void deleteInvoice()
    {
        String selection = ItemsEntry.TaskEntry.COL_IVNUM + " = ?";
        String[] selectionArgs = {String.valueOf(invoice)};
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ItemsEntry.TaskEntry.TABLE_INVOICE, selection, selectionArgs);
        db.close();
    }
   public void updateProductDB(String product_id, String quantity, boolean status)
    {
        Log.d(TAG, "Update call:" + quantity);
        /*1. Is the product is available in product database*/
        Double qualityToUpdate = 0.0;
        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
        String[] selectionArgs = {product_id};
        Log.d(TAG, "updateProductDB:"+selectionArgs[0]);
        SQLiteDatabase productCheck = mHelper.getReadableDatabase();
        Cursor cursor = productCheck.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_OFFER,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                },
                null, null, null, null, null);
        if(cursor.moveToNext())
        {
            Log.d(TAG, "Product code found:");
            /*2. If the product already available, then less the invoice quantity from total quantity*/

            String availQuantity =cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY));
            Log.d(TAG, "Update call availQuantity:" + availQuantity);
            if(status == false)

                qualityToUpdate =  Double.parseDouble(availQuantity) - Double.parseDouble(quantity);
            else
                qualityToUpdate =  Double.parseDouble(availQuantity) + Double.parseDouble(quantity);

            if(qualityToUpdate < 0)
                qualityToUpdate = 0.0;

            productCheck.close();

            Log.d(TAG, "Quantity to Update:"+qualityToUpdate);
            productCheck = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY, qualityToUpdate);
            values.put(ItemsEntry.TaskEntry.COL_ITEM_NAME, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME)));
            values.put(ItemsEntry.TaskEntry.COL_PRODUCT_CODE, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE)));
            values.put(ItemsEntry.TaskEntry.COL_MRP, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP)));
            values.put(ItemsEntry.TaskEntry.COL_OFFER, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER)));
            values.put(ItemsEntry.TaskEntry.COL_SGST, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
            values.put(ItemsEntry.TaskEntry.COL_CGST, cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST)));
            selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
            String[] selectionArgs1 = {cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE))};
            productCheck.update(ItemsEntry.TaskEntry.TABLE_PRODUCT, values, selection, selectionArgs1);
            productCheck.close();
        }
        cursor.close();
    }
    public void printInvoice()
    {
        int nameLength = Name.length();
        int leftSpace = (32 - nameLength)/2;
        invoiceTotal = 0.0;
        toPrint = "\n";
                /*"12345678901234567890123456789012"*/
        for (int i = 0; i < leftSpace; i++) {
            toPrint += " ";
        }
        toPrint+= Name+"\n";
        nameLength = city.length() + pincode.length()+1;
        leftSpace = (32 - nameLength)/2;
        for (int i = 0; i < leftSpace; i++) {
            toPrint += " ";
        }
        toPrint+=city+" "+pincode+"\n";

        if(gstActive.contentEquals("true"))
            toPrint+= "GST No# "+gstnumber+"\n";
        /*"12345678901234567890123456789012"*/
        /*"Date: 14:Sep:2017 Time: 00:00:00"*/
        toPrint+="Date: "+Date+" "+"Time: "+Time+"\n";

        toPrint+= "Invoice To: "+invoiceName+"/"+invoicePhNumber+"\n";
        toPrint+= "Invoice No# "+invoiceSerial+"\n";
        toPrint+= "--------------------------------\n";
        toPrint+= "ID#   Qty  Price  Dis%   Total \n";
        toPrint+= "--------------------------------\n";
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_ID,
                        ItemsEntry.TaskEntry.COL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_PRICE,
                        ItemsEntry.TaskEntry.COL_ISGST,
                        ItemsEntry.TaskEntry.COL_ICGST,
                        ItemsEntry.TaskEntry.COL_IOFFER,
                        ItemsEntry.TaskEntry.COL_TOTAL,
                        ItemsEntry.TaskEntry.COL_IVNUM,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int invoiceNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IVNUM)));
            if (invoiceNumber == invoice) {


                String StringToPrint="";
                String bItem = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_ID));
                String bQuantity = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUANTITY));
                String bPrice = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRICE));
                String bISGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ISGST));
                String bICGST = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ICGST));
                String bOffer = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_IOFFER));
                String bTotal = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_TOTAL));
                invoiceTotal = invoiceTotal + Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_TOTAL)));
                Double finalValue = Double.parseDouble(bQuantity) * Double.parseDouble(bPrice);
                finalValue -= ((finalValue/100)*Double.parseDouble(bOffer));
                Double tax1 = ((finalValue/100)* (Double.parseDouble(bISGST)));
                Double tax2 = ((finalValue/100)* (Double.parseDouble(bICGST)));
                toPrint += bItem + "\n";
                /*"12345678901234567890123456789012"
                toPrint = "ID#    Qty  Price  Off    Total \n";
                toPrint+= "--------------------------------\n";*/
                StringToPrint ="       "+bQuantity;
                for (int i = 0; i <= (3 - bQuantity.length()); i++) {
                    StringToPrint += " ";
                }
                StringToPrint += bPrice;
                for (int i = 0; i <= (6 - bPrice.length()); i++) {
                    StringToPrint += " ";
                }

                StringToPrint += bOffer;
                toPrint+=StringToPrint;
                String Price = String.format("%.2f", finalValue);
                for (int i = 0; i <= ((31 - StringToPrint.length())- Price.length()); i++) {
                    toPrint += " ";
                }

                toPrint += Price;
                toPrint += "\n";
                if(gstActive.contentEquals("true"))
                {
                /*toPrint = "ID#    Qty  Price  Off    Total \n";*/
                    StringToPrint = "    CGST: " + bICGST + "%";
                    String lenth = String.format("%.2f", tax1);
                    int len = ((31 - StringToPrint.length()) - lenth.length());
                    for (int i = 0; i <= len; i++) {
                        StringToPrint += " ";
                    }
                    StringToPrint += lenth + "\n";
                    toPrint += StringToPrint;

                    //toPrint +=  "       CGST: "+bICGST+"%        "+String.format("%.2f", tax1)+"\n";
                    StringToPrint = "    SGST: " + bICGST + "%";
                    lenth = String.format("%.2f", tax2);
                    len = ((31 - StringToPrint.length()) - lenth.length());
                    for (int i = 0; i <= len; i++) {
                        StringToPrint += " ";
                    }
                    StringToPrint += lenth + "\n";
                    toPrint += StringToPrint;
                    //toPrint +=  "       SGST: "+bISGST+"%        "+String.format("%.2f", tax2)+"\n";
                }
            }
        }
        cursor.close();
        db.close();
        /*"12345678901234567890123456789012345678"
        toPrint = "ID#     Qty   Price   Off(%)   Total  \n";*/
        toPrint+= "--------------------------------\n";
        String StringToPrint = "           Total :Rs ";
        String lenth = String.format("%.2f", invoiceTotal);
        int len = ((31 - StringToPrint.length())- lenth.length());
        for (int i = 0; i <= len; i++) {
            StringToPrint += " ";
        }
        StringToPrint+=lenth+"\n";
        toPrint+=StringToPrint;

        //toPrint+= "           Total :Rs "+invoiceTotal+"\n";

        toPrint+= "              ------------------\n";
        if(gstActive.contentEquals("true")) {
            double totalPayment = Double.parseDouble(paymentRcvd) + paymentsofar;
            StringToPrint = " Received Amount :Rs ";
            lenth = String.format("%.2f", totalPayment);
            len = ((31 - StringToPrint.length()) - lenth.length());
            for (int i = 0; i <= len; i++) {
                StringToPrint += " ";
            }
            StringToPrint += lenth + "\n";
            toPrint += StringToPrint;


            //toPrint+= " Received Amount :Rs "+paymentRcvd+"\n";
            toPrint += "              ------------------\n";
            StringToPrint = "  Balance Amount :Rs ";
            lenth = String.format("%.2f", invoiceTotal - totalPayment);
            len = ((31 - StringToPrint.length()) - lenth.length());
            for (int i = 0; i <= len; i++) {
                StringToPrint += " ";
            }
            StringToPrint += lenth + "\n";
            toPrint += StringToPrint;
        }

        //toPrint+= "  Balance Amount :Rs "+balanceAmount+"\n";
        toPrint+= "------ End of the Invoice ------\n\n";
        Log.d(TAG, toPrint);
        if(connectionObj.printOut(toPrint) == false)
        {
            Log.d(TAG, "Bluetooth Print Failed");
        }
        else
        {
            Log.d(TAG, "Bluetooth Print Success");
        }
     }
}