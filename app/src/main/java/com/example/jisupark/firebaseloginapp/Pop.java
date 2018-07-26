package com.example.jisupark.firebaseloginapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

class Pop extends Activity {
    int buttonNum=0;
    Boolean empty=false;

    Pop(int num, Boolean emp)
    {
        buttonNum = num;
        empty = emp;

    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(!empty) {
            setContentView(R.layout.popupwindow);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            getWindow().setLayout((int) (width * .8), (int) (height * .6));
            //DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("ParkingLot" + "/" + buttonNum + "/license");

           // TextView licenseText = (TextView)  findViewById(R.id.LicenseText);
           // TextView userNameText = (TextView) findViewById(R.id.userText);

            //licenseText.setText("hello");
            //mRef = FirebaseDatabase.getInstance().getReference("ParkingLot" + "/" + buttonNum + "/user");
            //userNameText.setText("hi");

        }

    }
    void display()
    {

    }
}
