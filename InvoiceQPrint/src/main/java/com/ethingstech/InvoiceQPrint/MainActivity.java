package com.ethingstech.InvoiceQPrint;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hoin.btsdk.BluetoothService;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView mInvoicesHeader;
    private TextView mProductsHeader;
    private TextView mSettingsHeader;
    private FragmentManager mManager;
    private FragmentTransaction mTransaction;
    private Fragment mInvoicesFragment;
    private Fragment mProductsFragment;
    private Fragment mSettingsFragment;
    private PrinterBtConnection connectionObj;
    BluetoothService mService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            connectionObj = PrinterBtConnection.getInstance(this);

            setContentView(R.layout.activity_main);
            mInvoicesHeader = (TextView) findViewById(R.id.invoice_header);
            mProductsHeader = (TextView) findViewById(R.id.productlist_header);
            mSettingsHeader = (TextView) findViewById(R.id.settings_header);
            mInvoicesHeader.setOnClickListener(this);
            mProductsHeader.setOnClickListener(this);
            mSettingsHeader.setOnClickListener(this);
            mManager = getSupportFragmentManager();
            mTransaction = mManager.beginTransaction();
            mInvoicesFragment = new InvoiceEntryFragment();
            mTransaction.replace(R.id.fragment, mInvoicesFragment);
            mTransaction.commit();
            mInvoicesHeader.setBackgroundColor(getResources().getColor(R.color.green));
            mInvoicesHeader.setClickable(false);
        //new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_header:
                Log.d(TAG, "items_header");
                mInvoicesHeader.setClickable(false);
                mProductsHeader.setClickable(true);
                mSettingsHeader.setClickable(true);
                mInvoicesHeader.setBackgroundColor(getResources().getColor(R.color.green));
                mProductsHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mSettingsHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mTransaction = mManager.beginTransaction();
                mInvoicesFragment = new InvoiceEntryFragment();
                mTransaction.replace(R.id.fragment,mInvoicesFragment);
                mTransaction.commit();
                break;
            case R.id.productlist_header:
                mInvoicesHeader.setClickable(true);
                mProductsHeader.setClickable(false);
                mSettingsHeader.setClickable(true);
                mProductsHeader.setBackgroundColor(getResources().getColor(R.color.green));
                mInvoicesHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mSettingsHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mTransaction = mManager.beginTransaction();
                mProductsFragment = new ProductListFragment();
                mTransaction.replace(R.id.fragment,mProductsFragment);
                mTransaction.commit();
                Log.d(TAG, "invoices_header");
                break;
            case R.id.settings_header:
                mInvoicesHeader.setClickable(true);
                mProductsHeader.setClickable(true);
                mSettingsHeader.setClickable(false);
                mSettingsHeader.setBackgroundColor(getResources().getColor(R.color.green));
                mInvoicesHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mProductsHeader.setBackgroundColor(getResources().getColor(R.color.button_bg));
                mTransaction = mManager.beginTransaction();
                mSettingsFragment = new SettingsFragment();
                mTransaction.replace(R.id.fragment,mSettingsFragment);
                mTransaction.commit();
                Log.d(TAG, "settings_header");
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy:");
        connectionObj.closePrinter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
