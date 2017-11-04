package com.ethingstech.InvoiceQPrint;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ethingstech.InvoiceQPrint.db.ItemsEntry;
import com.ethingstech.InvoiceQPrint.db.dbHelper;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private dbHelper mHelper;
    String FILENAME = "Settings";

    String Name = "Company Name1";
    String address1 = "Address 1";
    String address2 = "Address 2";
    String city = "City";
    String pincode = "Pin Code";
    String gstnumber = "GST Number";
    String phone = "Phone Number";
    String mailid = "email id";
    String invoiceSerial = "00000";
    String databaseID = "1";
    String qpCheck = "false";
    String gstActive = "false";
    boolean update = false;

    EditText eName;
    EditText eAddress1;
    //EditText eAddress2;
    EditText eCity;
    EditText ePincode;
    EditText eGstnumber;
    EditText ePhone ;
    EditText eMailid;
    EditText eInvoiceSerial;
    CheckBox quickPrint;
    CheckBox cbGstActive;
    String macid = "00:00:00:00:00:00";

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CONNECT_DEVICE = 1;

    private OnFragmentInteractionListener mListener;
    private PrinterBtConnection connectionObj;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new insac tance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Settings: onCreate:");
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
        Log.d(TAG, "Settings: onCreateView:");
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        final Button saveSettings = (Button)view.findViewById(R.id.SaveSettings);
        final Button connectBt = (Button)view.findViewById(R.id.bt_connect);
        mHelper = new dbHelper(getContext());
        getSettingsInfo();
        eName= (EditText)view.findViewById(R.id.name) ;
        eAddress1 = (EditText)view.findViewById(R.id.address1) ;
        //eAddress2 = (EditText)view.findViewById(R.id.address2) ;
        eCity = (EditText)view.findViewById(R.id.city) ;
        ePincode = (EditText)view.findViewById(R.id.pincode) ;
        eGstnumber = (EditText)view.findViewById(R.id.gst) ;
        ePhone = (EditText)view.findViewById(R.id.phone) ;
        eMailid = (EditText)view.findViewById(R.id.email) ;
        eInvoiceSerial = (EditText)view.findViewById(R.id.InvoicePrefix) ;
        quickPrint = (CheckBox)view.findViewById(R.id.QuickPrint);
        cbGstActive = (CheckBox)view.findViewById(R.id.gstActive);

        eName.setText(Name, TextView.BufferType.EDITABLE);
        eAddress1.setText(address1, TextView.BufferType.EDITABLE);
        ePincode.setText(pincode, TextView.BufferType.EDITABLE);
        eCity.setText(city, TextView.BufferType.EDITABLE);
        ePhone.setText(phone, TextView.BufferType.EDITABLE);
        eGstnumber.setText(gstnumber, TextView.BufferType.EDITABLE);
        ePhone.setText(phone, TextView.BufferType.EDITABLE);
        eMailid.setText(mailid, TextView.BufferType.EDITABLE);
        eInvoiceSerial.setText(invoiceSerial, TextView.BufferType.EDITABLE);
        if(qpCheck.contentEquals("false"))
        {
            quickPrint.setChecked(false);
        }
        else
        {
            quickPrint.setChecked(true);
        }
        if(gstActive.contentEquals("false"))
        {
            cbGstActive.setChecked(false);
        }
        else
        {
            cbGstActive.setChecked(true);
        }

        eName.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eName.getText().toString());
                 String text = "Company Name";
                if(text.contentEquals(eName.getText().toString()))
                {
                    eName.setText("");
                    if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
                    //if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                    if(eCity.getText().length()==0) { eCity.setText("City"); }
                    if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                    if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                    if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                    if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                    if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }
                }
                return false;
             }
        });
        eAddress1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eAddress1.getText().toString());
                String text = "Address 1";
                if(text.contentEquals(eAddress1.getText().toString())){eAddress1.setText("");}

                if(eName.getText().length()==0) { eName.setText("Company Name");}
                //if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }

                return false;
            }
        });
       /* eAddress2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eAddress2.getText().toString());
                String text = "Address 2";
                if(text.contentEquals(eAddress2.getText().toString()))
                {
                    eAddress2.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }

                return false;
            }
        });*/
        eCity.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eCity.getText().toString());
                String text = "City";
                if(text.contentEquals(eCity.getText().toString()))
                {
                    eCity.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
                //if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }
                return false;
            }
        });
        ePincode.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+ePincode.getText().toString());
                String text = "Pin Code";
                if(text.contentEquals(ePincode.getText().toString()))
                {
                    ePincode.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
               // if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }
                return false;
            }
        });
        ePhone.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+ePhone.getText().toString());
                String text = "Phone Number";
                if(text.contentEquals(ePhone.getText().toString()))
                {
                    ePhone.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
               // if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }

                return false;
            }
        });
        eMailid.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eMailid.getText().toString());
                String text = "email id";
                if(text.contentEquals(eMailid.getText().toString()))
                {
                    eMailid.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
               // if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }

                return false;
            }
        });
        eGstnumber.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eGstnumber.getText().toString());
                String text = "GST Number";
                if(text.contentEquals(eGstnumber.getText().toString()))
                {
                    eGstnumber.setText("");
                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
               // if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eInvoiceSerial.getText().length()==0) { eInvoiceSerial.setText("Invoice Prefix"); }
               return false;
            }
        });
        eInvoiceSerial.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "eName.setOnClickListener:"+eInvoiceSerial.getText().toString());
                String text = "Invoice Prefix";
                if(text.contentEquals(eInvoiceSerial.getText().toString()))
                {
                    eInvoiceSerial.setText("");

                }
                if(eName.getText().length()==0) { eName.setText("Company Name");}
                if(eAddress1.getText().length()==0) { eAddress1.setText("Address 1"); }
              //  if(eAddress2.getText().length()==0) { eAddress2.setText("Address 2"); }
                if(ePincode.getText().length()==0) { ePincode.setText("Pin Code"); }
                if(eCity.getText().length()==0) { eCity.setText("City"); }
                if(ePhone.getText().length()==0) { ePhone.setText("Phone Number"); }
                if(eMailid.getText().length()==0) { eMailid.setText("email id"); }
                if(eGstnumber.getText().length()==0) { eGstnumber.setText("GST Number"); }

                return false;
            }
        });

        connectBt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent serverIntent = new Intent(getActivity(),BluetoothDeviceListActivity.class);      //运行另外一个类的活动
                startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
            }
        });
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewSettings();
            }
        });
        return view;
    }
    void saveNewSettings()
    {
        Log.d(TAG, "Save Settings new:");
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemsEntry.TaskEntry.COL_NAME, ((EditText) getActivity().findViewById(R.id.name)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_ADDLINE1, ((EditText) getActivity().findViewById(R.id.address1)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_ADDLINE2, macid);
        values.put(ItemsEntry.TaskEntry.COL_CITY, ((EditText) getActivity().findViewById(R.id.city)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_PIN, ((EditText) getActivity().findViewById(R.id.pincode)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_GST_NUMBER, ((EditText) getActivity().findViewById(R.id.gst)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_PHONE_NUMBER, ((EditText) getActivity().findViewById(R.id.phone)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_MAIL_ID, ((EditText) getActivity().findViewById(R.id.email)).getText().toString());
        values.put(ItemsEntry.TaskEntry.COL_INVOICE_PREFIX, ((EditText) getActivity().findViewById(R.id.InvoicePrefix)).getText().toString());
        if(quickPrint.isChecked())
        {
            values.put(ItemsEntry.TaskEntry.COL_QUICKPRINT,"true");
        }
        else
        {
            values.put(ItemsEntry.TaskEntry.COL_QUICKPRINT,"false");
        }
        if(cbGstActive.isChecked())
        {
            values.put(ItemsEntry.TaskEntry.COL_GST_ACTIVE,"true");
        }
        else
        {
            values.put(ItemsEntry.TaskEntry.COL_GST_ACTIVE,"false");
        }
        values.put(ItemsEntry.TaskEntry.COL_ST_RES1,"false");
        values.put(ItemsEntry.TaskEntry.COL_ST_RES2,"false");
        values.put(ItemsEntry.TaskEntry.COL_ST_RES3,"false");

        if(update == false) {
            Log.d(TAG, "Save Settings Insert:");
            db.insert(ItemsEntry.TaskEntry.TABLE_SETTINGS, null, values);
        }
        else
        {
            Log.d(TAG, "Save Settings Update:");
            String selection = ItemsEntry.TaskEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(databaseID)};
            db.update(ItemsEntry.TaskEntry.TABLE_SETTINGS,values,selection,selectionArgs);
        }
        db.close();

    }
    void getSettingsInfo()
    {
        int count = 0;
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
                        ItemsEntry.TaskEntry.COL_QUICKPRINT,
                        ItemsEntry.TaskEntry.COL_GST_ACTIVE,
                },
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            count = count+1;
            update = true;
            Name = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_NAME));
            address1 = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ADDLINE1));
            address2 = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ADDLINE2));
            city = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CITY));
            pincode = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PIN));
            gstnumber = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_GST_NUMBER));
            phone = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PHONE_NUMBER));
            mailid = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MAIL_ID));
            invoiceSerial = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_PREFIX));
            databaseID = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry._ID));
            qpCheck = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_QUICKPRINT));
            gstActive = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_GST_ACTIVE));
        }
        db.close();
        Log.d(TAG, "getSettingsInfo:"+ count);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Settings: onResume:");
        /*if( mService.isBTopen() == false){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }*/
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Settings: onDestroy:");
        //connectionObj.closePrinter();

    /*   if (mService != null) {
            mService.stop();
        }
        mService = null;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mService = null;
           }
        }, 5000);*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Settings: onAttach:");
     /*   if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Settings: onDetach:");
        mListener = null;
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
     @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    Log.d(TAG,"Device Connecting....."+address);
                    macid = address;
                    connectionObj.connectPrinter(address);
                    saveNewSettings();
                }
                break;
        }
    }
}
