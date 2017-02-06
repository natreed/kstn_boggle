package com.pdx.kstn.kstn_boggle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by thanhhoang on 1/28/17.
 */

public class ScoresActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        final Button button_return = (Button) findViewById(R.id.button_return);

        button_return.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }
}
