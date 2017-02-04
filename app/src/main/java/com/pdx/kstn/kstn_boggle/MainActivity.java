package com.pdx.kstn.kstn_boggle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);




        final Button single_player_mode = (Button) findViewById(R.id.button_single_player_mode);
        final Button double_player_mode = (Button) findViewById(R.id.button_double_player_mode);

        System.out.print("Hello hello\n");

        single_player_mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                Intent myIntent = new Intent(getApplicationContext(),
                        SinglePlayerActivity.class);
                startActivity(myIntent);
            }
        });

    }

}
