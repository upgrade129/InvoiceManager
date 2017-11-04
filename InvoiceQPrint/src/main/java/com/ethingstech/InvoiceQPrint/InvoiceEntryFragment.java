package com.ethingstech.InvoiceQPrint;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ethingstech.InvoiceQPrint.db.ItemsEntry;
import com.ethingstech.InvoiceQPrint.db.dbHelper;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoiceEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InvoiceEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceEntryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment mInvoiceEntryFragment;
    private Fragment mInvoicesFragment;
    private dbHelper mHelper;
    private ArrayAdapter<String> mAdapter;
    ArrayAdapter<String> arrayadapter;
    private ListView mItemsListView;
    String invoiceName;
    String invoicePhone;
    String paymentCycle;
    String balancePayment;
    String date;
    String time;
    String prefix;
    String invoiceToDelete;
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

    public InvoiceEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoiceEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoiceEntryFragment newInstance(String param1, String param2) {
        InvoiceEntryFragment fragment = new InvoiceEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAdapter = null;
        View view =  inflater.inflate(R.layout.fragment_invoice_entry_list, container, false);
        mItemsListView = (ListView) view.findViewById(R.id.lnvoiceList);
        arrayadapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, subjects);
        mItemsListView.setAdapter(arrayadapter);

        final Button addInvoice = (Button)view.findViewById(R.id.addInvoice);
        mHelper = new dbHelper(getContext());
        updateUI();
       addInvoice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Add Invoice");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Invoice Info");
                final TextView invoiceName = new TextView(getActivity());
                final TextView invoicePhNumber = new TextView(getActivity());
                final EditText getName = new EditText(getActivity());
                final EditText getPhNumber = new EditText(getContext());
                invoiceName.setText("Invoice To");
                invoicePhNumber.setText("Phone Number");
                getName.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                getPhNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout ll=new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(invoiceName);
                ll.addView(getName);
                ll.addView(invoicePhNumber);
                ll.addView(getPhNumber);
                dialog.setView(ll);
                dialog.setCancelable(false);
                dialog.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle b = new Bundle();
                        Calendar c = Calendar.getInstance();
                        b.putString("ivDate",String.valueOf(c.get(Calendar.DATE))+":"
                                                +months[c.get(Calendar.MONTH)]+":"
                                                +String.valueOf(c.get(Calendar.YEAR)));
                        b.putString("ivTime",String.valueOf(c.get(Calendar.HOUR_OF_DAY))+":"
                                +String.valueOf(c.get(Calendar.MINUTE))+":"
                                +String.valueOf(c.get(Calendar.SECOND)));

                        b.putString("invoice","New");
                        if(mItemsListView.getAdapter().getCount() !=0 )
                        {
                            b.putString("invoiceNumber",getInvoiceSerialNumber(String.valueOf(mItemsListView.getItemAtPosition(mItemsListView.getAdapter().getCount() - 1))));
                        }
                        else
                        {
                            b.putString("invoiceNumber", "0");
                        }
                        b.putString("InvoiceTo",String.valueOf(getName.getText()));
                        b.putString("InvoicePh",String.valueOf(getPhNumber.getText()));
                        b.putString("PaymentCycle",paymentCycle);
                        b.putString("balancePayment","0");
                        mInvoicesFragment = new BillEntryFragment();
                        mInvoicesFragment.setArguments(b);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, mInvoicesFragment)
                                .addToBackStack(null).commitAllowingStateLoss();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
        mItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Update Item :\n"+parent.getItemAtPosition(position));
                Log.d(TAG, "View Invoice");
                Bundle b = new Bundle();
                b.putString("invoice",getInvoiceSerialNumber(String.valueOf(parent.getItemAtPosition(position))));
                b.putString("prefix",prefix);
                b.putString("ivTime",time);
                b.putString("ivDate",date);
                b.putString("InvoiceTo",invoiceName);
                b.putString("InvoicePh",invoicePhone);
                b.putString("PaymentCycle",paymentCycle);
                b.putString("balancePayment",balancePayment);
                mInvoicesFragment = new BillEntryFragment();
                mInvoicesFragment.setArguments(b);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, mInvoicesFragment)
                        .addToBackStack(null).commitAllowingStateLoss();
            }
        });
        mItemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> parent, View view,
                                             int position, long id)
              {
                  invoiceToDelete = getInvoiceSerialNumber(String.valueOf(parent.getItemAtPosition(position)));
                  AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                  dialog.setTitle("Do you want to Delete the item?");
                  dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          SQLiteDatabase db;
                          String selection;
                          selection = ItemsEntry.TaskEntry.COL_INVOICE_SERIAL + " = ?";
                          String[] selectionArgs = {invoiceToDelete};
                          db = mHelper.getWritableDatabase();
                          db.delete(ItemsEntry.TaskEntry.TABLE_INVOICE_LIST, selection, selectionArgs);
                          db.close();

                          selection = ItemsEntry.TaskEntry.COL_IVNUM + " = ?";
                          String[] selectionArgs1 = {invoiceToDelete};
                          db = mHelper.getWritableDatabase();
                          db.delete(ItemsEntry.TaskEntry.TABLE_INVOICE, selection, selectionArgs);
                          db.close();

                          selection = ItemsEntry.TaskEntry.COL_LP_IVNUM + " = ?";
                          String[] selectionArgs12 = {invoiceToDelete};
                          db = mHelper.getWritableDatabase();
                          db.delete(ItemsEntry.TaskEntry.TABLE_LASTPAYMENT, selection, selectionArgs);
                          db.close();

                          selection = ItemsEntry.TaskEntry.COL_NP_IVNUM + " = ?";
                          String[] selectionArgs13 = {invoiceToDelete};
                          db = mHelper.getWritableDatabase();
                          db.delete(ItemsEntry.TaskEntry.TABLE_NEXTPAYMENT, selection, selectionArgs);
                          db.close();

                          updateUI();
                      }
                  });
                  dialog.setNegativeButton("Cancel", null);
                  dialog.show();

                  return true;
              }
          });
        return view;
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    public String getInvoiceSerialNumber(String task)
    {
        String selection = ItemsEntry.TaskEntry.COL_INVOICE_PRENAME + " = ?";
        String[] selectionArgs = {task.substring(task.indexOf(':')+1, task.indexOf('#'))};
        Log.d(TAG, "Serial Number:"+selectionArgs[0]);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE_LIST,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_INVOICE_SERIAL,
                        ItemsEntry.TaskEntry.COL_INVOICE_PRENAME,
                        ItemsEntry.TaskEntry.COL_INVOICE_NAME,
                        ItemsEntry.TaskEntry.COL_INVOICE_NUMBER,
                        ItemsEntry.TaskEntry.COL_INVOICE_DATE,
                        ItemsEntry.TaskEntry.COL_INVOICE_MONTH,
                        ItemsEntry.TaskEntry.COL_INVOICE_Year,
                        ItemsEntry.TaskEntry.COL_INVOICE_HOUR,
                        ItemsEntry.TaskEntry.COL_INVOICE_MIN,
                        ItemsEntry.TaskEntry.COL_INVOICE_SEC,
                        ItemsEntry.TaskEntry.COL_INVOICE_VALUE,
                        ItemsEntry.TaskEntry.COL_INVOICE_BALANCE,
                        ItemsEntry.TaskEntry.COL_INVOICE_Term,
                },
                selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        prefix = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_PRENAME));
        date = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_DATE))+":"+
                months[Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_MONTH)))]+":"+
                cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_Year));

        time = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_HOUR))+":"+
                cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_MIN))+":"+
                cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_SEC));
        invoiceName = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_NAME));
        invoicePhone = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_NUMBER));
        paymentCycle =  cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_Term));
        balancePayment = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_BALANCE));
        db.close();
        return cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_SERIAL));
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_INVOICE_LIST,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_INVOICE_SERIAL,
                        ItemsEntry.TaskEntry.COL_INVOICE_PRENAME,
                        ItemsEntry.TaskEntry.COL_INVOICE_DATE,
                        ItemsEntry.TaskEntry.COL_INVOICE_MONTH,
                        ItemsEntry.TaskEntry.COL_INVOICE_Year,
                        ItemsEntry.TaskEntry.COL_INVOICE_HOUR,
                        ItemsEntry.TaskEntry.COL_INVOICE_MIN,
                        ItemsEntry.TaskEntry.COL_INVOICE_SEC,
                        ItemsEntry.TaskEntry.COL_INVOICE_VALUE,
                        ItemsEntry.TaskEntry.COL_INVOICE_BALANCE,
                        ItemsEntry.TaskEntry.COL_INVOICE_status,
                },
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String StringToPrint;
           int date = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_DATE)).length();

            StringToPrint = "Invoice Number:";
            //StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_PRENAME));
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_PRENAME));
            StringToPrint += "#\n";

         /*   for(int i=0; i <= (10-serial);i++)
            {
                StringToPrint += " ";
            }*/
            StringToPrint += "Last Payment Date/Time: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_DATE)) + ":"+
                    months[Integer.valueOf(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_MONTH)))] + ":"+
                    cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_Year));

            for(int i=0; i <= (10-date);i++)
            {
                StringToPrint += " ";
            }

            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_HOUR)) + ":"+
                    cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_MIN)) + ":"+
                    cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_SEC));
            StringToPrint += "\n";
           /* for(int i=0; i <= (10-date);i++)
            {
                StringToPrint += " ";
            }*/
            StringToPrint += "Value ₹:";
            StringToPrint += String.format("%.2f",Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_VALUE))));
            StringToPrint += "\n";

            /*for(int i=0; i <= (10-date);i++)
            {
                StringToPrint += " ";
            }*/
            StringToPrint += "Received ₹:";
            StringToPrint += String.format("%.2f",Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_VALUE)))-Double.parseDouble(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_BALANCE))));
            StringToPrint += "\n";
            /*
            for(int i=0; i <= (10-date);i++)
            {
                StringToPrint += " ";
            }*/

            StringToPrint += "Status :";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_INVOICE_status));

            StringToPrint += "\n";
            Log.d(TAG, "Added Items: " + StringToPrint);
            taskList.add(StringToPrint);

        }
        cursor.close();
        db.close();
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
    }
}