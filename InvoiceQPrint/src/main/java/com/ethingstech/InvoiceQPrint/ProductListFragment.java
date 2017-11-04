package com.ethingstech.InvoiceQPrint;

import android.content.ContentValues;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mProductListView;
    ArrayAdapter<String> productArrayadapter;
    String productToQuery;
    private ArrayAdapter<String> mAdapter;
    String tempPrice;
    private Button addEntry = null;
    private dbHelper mHelper;
    Context mContext;
    EditText productCode;
    EditText productInput ;
    EditText priceInput;
    EditText quantityInput;
    EditText pSGST;
    EditText pCGST;
    EditText pOffer;
    String geScanCode="";
    boolean scanReady = false;

    String[] subjects = new String[] {
            "Android",
            "PHP",
            "Blogger",
            "WordPress",
            "SEO"
    };

    private OnFragmentInteractionListener mListener;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_entry, container, false);
        mProductListView = (ListView) view.findViewById(R.id.product_list);
        productArrayadapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1, subjects);
        mContext = this.getContext();
        mProductListView.setAdapter(productArrayadapter);
        mProductListView.setLongClickable(true);
        mHelper = new dbHelper(getContext());
        updateUI();
        final Button addProduct = (Button)view.findViewById(R.id.add_product);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProducttoDB();
            }
        });

        mProductListView.setOnKeyListener(new View.OnKeyListener()
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
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d(TAG, "Key ENTER: " + geScanCode);
                            scanReady = true;
                            addProducttoDB();
                            break;
                    }
                }
                return true;
            }
        });
        mProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Update Product"+parent.getItemAtPosition(position));
                updateItem(String.valueOf(parent.getItemAtPosition(position)));
            }
        });
        mProductListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                    View parentView = (View) view.getParent();
                    TextView taskTextView = (TextView) parentView.findViewById(R.id.textProductlist);
                    String task = String.valueOf(taskTextView.getText());
                    String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
                    String[] selectionArgs = {task.substring(0, task.indexOf(':'))};
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    db.delete(ItemsEntry.TaskEntry.TABLE_PRODUCT, selection, selectionArgs);
                    db.close();
                    updateUI();
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
        /*
        if (context instanceof OnFragmentInteractionListener) {
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
    private void addProducttoDB() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setTitle("Enter Product");
        final EditText productName = new EditText(this.getActivity());
        final EditText productCode = new EditText(this.getActivity());
        final EditText productQuantity = new EditText(this.getActivity());
        final EditText productPrice = new EditText(this.getActivity());
        final EditText productOffer = new EditText(this.getActivity());
        final EditText productSGST = new EditText(this.getActivity());
        final EditText productCGST = new EditText(this.getActivity());

        final TextView productTName = new TextView(this.getActivity());
        final TextView productTCode = new TextView(this.getActivity());
        final TextView productTQuantity = new TextView(this.getActivity());
        final TextView productTPrice = new TextView(this.getActivity());
        final TextView productTOffer = new TextView(this.getActivity());
        final TextView productTSGST = new TextView(this.getActivity());
        final TextView productTCGST = new TextView(this.getActivity());

        productTName.setText("Product Name");
        productTCode.setText("Product Code");
        productTQuantity.setText("Quantity");
        productTPrice.setText("Price ₹");
        productTOffer.setText("Discount(%)");
        productTSGST.setText("SGST(%)");
        productTCGST.setText("CGST(%)");


        productName.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        productCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        productQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        productCGST.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        productSGST.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        productOffer.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        productPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        LinearLayout ll=new LinearLayout(this.getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        if(scanReady == true)
        {
            Log.d(TAG, "Add DB " + geScanCode);
            productCode.setText(geScanCode);
            scanReady = false;
            geScanCode = "";
        }

        ll.addView(productTCode);
        ll.addView(productCode);
        ll.addView(productTName);
        ll.addView(productName);
        ll.addView(productTQuantity);
        ll.addView(productQuantity);
        ll.addView(productTSGST);
        ll.addView(productSGST);
        ll.addView(productTCGST);
        ll.addView(productCGST);
        ll.addView(productTOffer);
        ll.addView(productOffer);
        ll.addView(productTPrice);
        ll.addView(productPrice);
        productName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isShowing = imm.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
        if (!isShowing)
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setView(ll);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Double finalValue = 0.0;
                String pName = String.valueOf(productName.getText());
                String pQuan = String.valueOf(productQuantity.getText());
                String pPrice = String.valueOf(productPrice.getText());
                String pCode = String.valueOf(productCode.getText());
                String pSGST = String.valueOf(productSGST.getText());
                String pCGST = String.valueOf(productCGST.getText());
                String pOffer = String.valueOf(productOffer.getText());
                if(pQuan.length()==0) pQuan = "0";
                if(pSGST.length()==0) pSGST = "0";
                if(pCGST.length()==0) pCGST = "0";
                if(pOffer.length()==0) pOffer = "0";

                if(pName.length()!=0 && pCode.length()!=0 && pPrice.length()!=0) {
                    int count = isListedAlready(pCode);
                    if(count !=0) {
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(ItemsEntry.TaskEntry.COL_PRODUCT_CODE, pCode);
                        values.put(ItemsEntry.TaskEntry.COL_ITEM_NAME, pName);
                        values.put(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY, pQuan);
                        values.put(ItemsEntry.TaskEntry.COL_MRP, pPrice);
                        values.put(ItemsEntry.TaskEntry.COL_SGST, pSGST);
                        values.put(ItemsEntry.TaskEntry.COL_CGST, pCGST);
                        values.put(ItemsEntry.TaskEntry.COL_OFFER, pOffer);
                        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
                        String[] selectionArgs = {pCode};
                        db.update(ItemsEntry.TaskEntry.TABLE_PRODUCT,values,selection,selectionArgs);
                        db.close();
                    }
                    else
                    {
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(ItemsEntry.TaskEntry.COL_ITEM_NAME, pName);
                        values.put(ItemsEntry.TaskEntry.COL_PRODUCT_CODE, pCode);
                        values.put(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY, pQuan);
                        values.put(ItemsEntry.TaskEntry.COL_MRP, pPrice);
                        values.put(ItemsEntry.TaskEntry.COL_OFFER, pOffer);
                        values.put(ItemsEntry.TaskEntry.COL_SGST, pSGST);
                        values.put(ItemsEntry.TaskEntry.COL_CGST, pCGST);
                        values.put(ItemsEntry.TaskEntry.COL_PR_RES1,"false");
                        values.put(ItemsEntry.TaskEntry.COL_PR_RES2,"false");
                        values.put(ItemsEntry.TaskEntry.COL_PR_RES3,"false");

                        db.insert(ItemsEntry.TaskEntry.TABLE_PRODUCT, null, values);
                        db.close();
                    }
                    Log.d(TAG, "Task to add: " + pName);
                    updateUI();
                }
                else
                    Log.d(TAG, "Empty List:");
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
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
            String StringToPrint;
            StringToPrint = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE))+":\n";
            StringToPrint+="            Name: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME))+"\n";
            StringToPrint+="            Quantity: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY))+"     ";
            StringToPrint+="            Price: Rs.";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP))+"\n";
            StringToPrint+="            Discount: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER))+"%      ";
            StringToPrint+="            SGST: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST))+"%       ";
            StringToPrint+="            CGST: ";
            StringToPrint += cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST))+"%\n";
            Log.d(TAG, "Added Items: " + StringToPrint);
            taskList.add(StringToPrint);
        }
        cursor.close();
        db.close();
        if (mAdapter == null) {
            Log.d(TAG, "mAdapter is null ");
            mAdapter = new ArrayAdapter<>(this.getContext(),
                    R.layout.product_list_text,
                    R.id.textProductlist,
                    taskList);
            mProductListView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "update list ");
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void updateItem(String task) {
        Log.d(TAG, "updateProduct:"+task);

        String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
        String[] selectionArgs = {task.substring(0, task.indexOf(':'))};
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ItemsEntry.TaskEntry.TABLE_PRODUCT,
                new String[]{ItemsEntry.TaskEntry._ID,
                        ItemsEntry.TaskEntry.COL_PRODUCT_CODE,
                        ItemsEntry.TaskEntry.COL_ITEM_NAME,
                        ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY,
                        ItemsEntry.TaskEntry.COL_MRP,
                        ItemsEntry.TaskEntry.COL_SGST,
                        ItemsEntry.TaskEntry.COL_CGST,
                        ItemsEntry.TaskEntry.COL_OFFER,
                },
                selection, selectionArgs, null, null, null);
        cursor.moveToNext();

        LayoutInflater li = LayoutInflater.from(mContext);
        View dialogView = li.inflate(R.layout.add_item_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Update Product Entry");
        alertDialogBuilder.setView(dialogView);
        productCode = (EditText) dialogView.findViewById(R.id.Unique_Code);
        productInput = (EditText) dialogView.findViewById(R.id.product_input);
        priceInput = (EditText) dialogView.findViewById(R.id.price_input);
        quantityInput = (EditText) dialogView.findViewById(R.id.quantity_input);
        pSGST = (EditText) dialogView.findViewById(R.id.sgst_input);
        pCGST = (EditText) dialogView.findViewById(R.id.cgst_input);
        pOffer = (EditText) dialogView.findViewById(R.id.offer_input);
        productCode.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE)));
        productInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_ITEM_NAME)));
        priceInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_MRP)));
        quantityInput.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY)));
        pOffer.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_OFFER)));
        pSGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_SGST)));
        pCGST.setText(cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_CGST)));
        db.close();
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String code = String.valueOf(productCode.getText());
                                String item = String.valueOf(productInput.getText());
                                String quan = String.valueOf(quantityInput.getText());
                                String value = String.valueOf(priceInput.getText());
                                String uSGST = String.valueOf(pSGST.getText());
                                String uCGST = String.valueOf(pCGST.getText());
                                String ioffer = String.valueOf(pOffer.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(ItemsEntry.TaskEntry.COL_PRODUCT_CODE, code);
                                values.put(ItemsEntry.TaskEntry.COL_ITEM_NAME, item);
                                values.put(ItemsEntry.TaskEntry.COL_AVAIL_QUANTITY, quan);
                                values.put(ItemsEntry.TaskEntry.COL_MRP, value);
                                values.put(ItemsEntry.TaskEntry.COL_SGST, uSGST);
                                values.put(ItemsEntry.TaskEntry.COL_CGST, uCGST);
                                values.put(ItemsEntry.TaskEntry.COL_OFFER, ioffer);
                                String selection = ItemsEntry.TaskEntry.COL_PRODUCT_CODE + " = ?";
                                String[] selectionArgs = {code};
                                db.update(ItemsEntry.TaskEntry.TABLE_PRODUCT,values,selection,selectionArgs);
                                db.close();
                                Log.d(TAG, "Task to add: " + item);
                                updateUI();
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
        alertDialogBuilder.show();
    }

    public int isListedAlready(String serachText)
    {
        ArrayList<String> taskList = new ArrayList<>();
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
            String productName = cursor.getString(cursor.getColumnIndex(ItemsEntry.TaskEntry.COL_PRODUCT_CODE));
            if(productName.contentEquals(serachText))
            {
                return 1;
            }
        }
        db.close();
        return 0;
    }

}
