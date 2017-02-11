package com.pdx.kstn.kstn_boggle;

import android.content.Intent;
import android.os.Bundle;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by nathanreed on 2/5/17.
 */

public class RulesActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);

        final Button button_return = (Button) findViewById(R.id.button_return);

        button_return.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }
}

