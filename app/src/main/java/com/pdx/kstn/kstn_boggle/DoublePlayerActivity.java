package com.pdx.kstn.kstn_boggle;

import android.app.Activity;

public class DoublePlayerActivity extends Activity {

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class DoublePlayerActivity extends AppCompatActivity {

    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

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
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            //if (mChatService == null) setupChat();
            Toast.makeText(getApplicationContext(),"Set up board", Toast.LENGTH_LONG).show();
        }
    }
}
