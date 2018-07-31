package com.example.jisupark.firebaseloginapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

class Pop extends Activity {

    private Button CloseButton;
    String btn;
    TextView userNameText;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.popupwindow);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            getWindow().setLayout((int) (width * .8), (int) (height * .6));
            //CloseButton = (Button) findViewById(R.id.closeButton);

        TextView licenseText = (TextView)  findViewById(R.id.LicenseText);
        userNameText = (TextView) findViewById(R.id.userText);

        btn = getIntent().getExtras().getString("licenseNumber");

       licenseText.setText("license # : " + btn);

       /* CloseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void OnClick(View v){
                finish();
            }
        });*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CarLicense_list" + "/" + btn + "/name");

        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userNameText.setText("Username : " + snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // TextView licenseText = (TextView)  findViewById(R.id.LicenseText);
           // TextView userNameText = (TextView) findViewById(R.id.userText);

            //licenseText.setText("hello");
            //mRef = FirebaseDatabase.getInstance().getReference("ParkingLot" + "/" + buttonNum + "/user");
            //userNameText.setText("hi");


    }
}
