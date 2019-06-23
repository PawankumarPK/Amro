package com.example.amro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.amro.R;

public class Demo extends AppCompatActivity {

    TextView display = null;
    Button mOne, mTwo;
    int digit;
    long number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        display = (TextView) findViewById(R.id.display);
        mOne = (Button) findViewById(R.id.mOne);
        mTwo = (Button) findViewById(R.id.mTwo);

        mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Button b  =  (Button) v;

                Toast.makeText(Demo.this, "fkbkbr", Toast.LENGTH_SHORT).show();
                digit = Integer.parseInt(mOne.getText().toString());

                number = Long.parseLong(display.getText().toString());


                number = number * 10 + digit;

                display.setText(String.valueOf(number));

            }
        });


        mTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  Button b  =  (Button) v;

                Toast.makeText(Demo.this, "fkbkbr", Toast.LENGTH_SHORT).show();
                digit = Integer.parseInt(mTwo.getText().toString());

//                number = Long.parseLong(display.getText().toString());


                number = number * 10 + digit;

                display.setText(String.valueOf(number));

            }
        });


    }

   /* public void numberPressed(View v) {

        Button b  =  (Button) v;

        digit = Integer.parseInt(b.getText().toString());

        number = Long.parseLong(display.getText().toString());


        number = number * 10 + digit;

        display.setText(String.valueOf(number));
    }*/
}
