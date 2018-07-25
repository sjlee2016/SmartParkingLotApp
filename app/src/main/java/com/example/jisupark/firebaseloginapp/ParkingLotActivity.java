package com.example.jisupark.firebaseloginapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jisupark.firebaseloginapp.AccountActivity.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.graphics.Color.rgb;

/**
 * Created by JisuPark on 2018-07-25.
 */

public class ParkingLotActivity extends AppCompatActivity{
    Button[] carButton = new Button[6];
    public static final String TAG = ParkingLotActivity.class.getSimpleName();

    void check() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ParkingLot");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = null;
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    value = null;
                    System.out.println(postSnapshot.getKey());

                    value = postSnapshot.getValue(String.class);
                    Log.d(TAG, "this is " + i + "th" + value);
                    if (value.equals("0")) {
                        carButton[i].setBackgroundColor(rgb(38, 174, 144));

                    } else {

                        carButton[i].setBackgroundColor(rgb(255, 104, 97));
                    }
                    i++;
                }
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               /* String value = dataSnapshot.getValue(String.class);
                try {
                    Log.d(TAG, "Value is: " + value);
                }
                catch(Exception nullException)
                {
                    Log.d(TAG, "NULL");
                }*/
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot);

        check();

                /*for(int i=0; i<6; i++) {
            DatabaseReference myRef = database.getReference("ParkingLot" + "/" + i);
            myRef.setValue("0");
        }
*/

        carButton[0] = (Button) findViewById(R.id.car_1);

        carButton[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ParkingLotActivity.this, Pop.class) );


            }
        });
        carButton[1] = (Button) findViewById(R.id.car_2);

        carButton[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                carButton[1].setBackgroundColor(rgb(255, 104, 97));
                Toast.makeText(getApplicationContext(), "this is button 2", Toast.LENGTH_SHORT).show();

            }
        });
        carButton[2] = (Button) findViewById(R.id.car_3);

        carButton[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                carButton[2].setBackgroundColor(Color.RED);

                Toast.makeText(getApplicationContext(), "this is button 3", Toast.LENGTH_SHORT).show();
            }
        });
        carButton[3] = (Button) findViewById(R.id.car_4);

        carButton[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                carButton[3].setBackgroundColor(rgb(255, 104, 97));
                Toast.makeText(getApplicationContext(), "this is button 4", Toast.LENGTH_SHORT).show();
            }
        });

        carButton[4] = (Button) findViewById(R.id.car_5);

        carButton[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                carButton[4].setBackgroundColor(rgb(255, 104, 97));
                Toast.makeText(getApplicationContext(), "this is button 5", Toast.LENGTH_SHORT).show();
            }
        });

        carButton[5] = (Button) findViewById(R.id.car_6);

        carButton[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                carButton[5].setBackgroundColor(rgb(255, 104, 97));
                Toast.makeText(getApplicationContext(), "this is button 6", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
