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

import java.io.InvalidObjectException;

import static android.graphics.Color.rgb;

/**
 * Created by JisuPark on 2018-07-25.
 */

public class ParkingLotActivity extends AppCompatActivity{
    Button[] carButton = new Button[6];
    Boolean[] emptyList = new Boolean[6];
    String[] values = new String[6];

    public static final String TAG = ParkingLotActivity.class.getSimpleName();

    void check() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ParkingLot");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = null;
                int v = 0;
                int i = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    value = postSnapshot.getValue(String.class);


                    if(value.equals("00"))
                    {

                        carButton[i].setBackgroundColor(rgb(38, 174, 144));
                        emptyList[i] = true;
                        values[i] = value;

                    }else
                    {

                        emptyList[i] = false;
                        carButton[i].setBackgroundColor(rgb(255, 104, 97));
                        values[i]=value;
                    }

                    /*
                    if (value==null) {
                        carButton[i].setBackgroundColor(rgb(38, 174, 144));
                        emptyList[i] = true;
                    } else {
                    }
                    */

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
        /*
                for(int i=0; i<6; i++) {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ParkingLot" + "/" + i);
                    myRef.setValue(0);
                }*/
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
                if(!emptyList[0])
                {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[0]);

                    startActivity(intent);
                }

            }
        });
        carButton[1] = (Button) findViewById(R.id.car_2);

        carButton[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!emptyList[1]) {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[1]);
                    startActivity(intent);
                }
                 //   Pop popUpWindow = new Pop(1, emptyList[1]);
                  // startActivity(new Intent(ParkingLotActivity.this, Pop.class));

            }
        });
        carButton[2] = (Button) findViewById(R.id.car_3);

        carButton[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!emptyList[2]) {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[2]);

                    startActivity(intent);
                }
              //  Pop popUpWindow = new Pop(2, emptyList[2]);
              //  startActivity(new Intent(ParkingLotActivity.this, Pop.class) );

            }
        });
        carButton[3] = (Button) findViewById(R.id.car_4);

        carButton[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!emptyList[3]) {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[3]);

                    startActivity(intent);

                }
                //    Pop popUpWindow = new Pop(3, emptyList[3]);
                  //  startActivity(new Intent(ParkingLotActivity.this, Pop.class));


            }
        });

        carButton[4] = (Button) findViewById(R.id.car_5);

        carButton[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!emptyList[4])

                {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);

                    intent.putExtra("licenseNumber", values[4]);

                    startActivity(intent);
                }
                //   Pop popUpWindow = new Pop(4, emptyList[4]);
                 //   startActivity(new Intent(ParkingLotActivity.this, Pop.class));

            }
        });

        carButton[5] = (Button) findViewById(R.id.car_6);

        carButton[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!emptyList[5]) {
                    Intent intent = new Intent(ParkingLotActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[5]);

                    startActivity(intent);
                }
            }
        });
    }
}