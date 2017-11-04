package com.ethingstech.InvoiceQPrint;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hoin.btsdk.BluetoothService;

import static android.content.ContentValues.TAG;

/**
 * Created by satheshm on 13/10/17.
 */

public class PrinterBtConnection {

    private static PrinterBtConnection connectionObj;
    private boolean connectionStatus = false;
    private Context context;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;

    public PrinterBtConnection(Context context){
        this.context = context;
    };

    public static PrinterBtConnection getInstance(Context context) {

        if (connectionObj ==null)
        {
            connectionObj = new PrinterBtConnection(context);

        }
        return connectionObj;
    }

    public boolean connectPrinter(String address)
    {
        getService();
        if (mService != null)
        {
            con_dev = mService.getDevByMac(address);
            try
            {
                mService.connect(con_dev);
                connectionStatus = true;
            }
            catch(NullPointerException e)
            {
                Log.d(TAG,"BtService is not conneced");
            }
            Log.d(TAG,"Bluetooth printer connected");
            return true;
        }
        return false;
    }

    public boolean closePrinter()
    {
        if (mService != null) {
            mService.stop();
            connectionStatus = false;
        }
        mService = null;

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mService = null;
            }
        }, 5000);*/

        return true;
    }
    public BluetoothService getService()
    {
        if(mService == null)
            mService = new BluetoothService(this.context, mHandler);

        if( mService.isAvailable() == false ){
            Log.d(TAG,"Bluetooth is not available");
           // Toast.makeText(this.context, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return null;
        }

        if( mService.isBTopen() == false){
            Log.d(TAG,"Turn on the Bluetooth");
            //Toast.makeText(this.context, "Turn on the Bluetooth", Toast.LENGTH_LONG).show();
            return null;
        }
        return mService;
    }
    public boolean getConnectionStatus()
    {
        return connectionStatus;
    }
    public boolean printOut(String data)
    {
        if(mService != null) {
            mService.sendMessage(data, "GBK");
            Log.d(TAG,"Bluetooth print complete");
            return true;
        }
        else
            return false;
    }

    private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"Device handle msge :"+msg.what);
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d(TAG,"Connect successful.....");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG,"Device Connecting.....");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG,"Device No State.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Log.d(TAG,"Device connection was lost.");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Log.d(TAG,"Unable to connect device.");
                     break;
            }
        }

    };
}