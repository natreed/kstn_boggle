package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Button;
import android.widget.Toast;

public class DoublePlayerActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter = null;


    private String mConnectedDeviceName = null;


    // Intent request codes for Bluetooth Chat Server
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    Button server, client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_player);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "bluetooth is not available", Toast.LENGTH_SHORT).show();
        }
        if (mBluetoothAdapter!=null){
            Toast.makeText(getApplicationContext(),"bluetooth is available", Toast.LENGTH_SHORT).show();
        }



    }
}
