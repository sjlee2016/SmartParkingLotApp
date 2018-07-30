package com.example.jisupark.firebaseloginapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

import com.example.jisupark.firebaseloginapp.AccountActivity.LoginActivity;
import com.example.jisupark.firebaseloginapp.AccountActivity.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity {

    private Button btnChangePassword, btnRemoveUser,changePassword, remove,signOut, ConnectserverButton, ParkingLotButton;
    private TextView email;
    private EditText oldEmail,password,newPassword;
    private ProgressBar progressBar;
    protected FirebaseAuth auth;
    public static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseReference authorizedCar =FirebaseDatabase.getInstance().getReference("AuthorizedCar");
    Button[] carButton = new Button[6];
    Boolean[] emptyList = new Boolean[6];
    String[] values = new String[6];

    public void notificationcall()
    {
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_mr_button_connected_00_light)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_mr_button_connected_00_dark))
                .setContentTitle("Alert !")
                .setContentText("Unauthorized vehicle is trying to enter your parking lot");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());


    }
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

    void checking() {
            authorizedCar = FirebaseDatabase.getInstance().getReference("AuthorizedCar");
            authorizedCar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value;
                    value = dataSnapshot.getValue(String.class);
                    if (!(value.equals("00"))) {
                        Toast.makeText(MainActivity.this, "alarm on", Toast.LENGTH_SHORT).show();
                        setAlarm(value);
                        notificationcall();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
    }

    public void setAlarm(String plate)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog.Builder builder1 = builder.setTitle("Unauthorized Car Alarm")
                .setCancelable(false)
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    //open 버튼을 누르면 gate가 열리도록 신호를 보내게 하기 위한 코드
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        authorizedCar.setValue("00");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Do not open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        authorizedCar.setValue("00");
                        dialog.cancel();
                    }
                });
        builder.setMessage("Hello, unauthorized car("+plate+") want to enter the parking lot. Would you open the gate?" +
                "");
        AlertDialog diag = builder.create();
        //Display the message!
        diag.show();
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block  q
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checking(); // check the parking lot's status

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
                    Intent intent = new Intent(MainActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[0]);

                    startActivity(intent);
                }

            }
        });
        carButton[1] = (Button) findViewById(R.id.car_2);

        carButton[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!emptyList[1]) {
                    Intent intent = new Intent(MainActivity.this, Pop.class);
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
                    Intent intent = new Intent(MainActivity.this, Pop.class);
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
                    Intent intent = new Intent(MainActivity.this, Pop.class);
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
                    Intent intent = new Intent(MainActivity.this, Pop.class);

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
                    Intent intent = new Intent(MainActivity.this, Pop.class);
                    intent.putExtra("licenseNumber", values[5]);

                    startActivity(intent);
                }
            }
        });

        checking();

        //get firebase auth instance
    auth= FirebaseAuth.getInstance();
    email=(TextView) findViewById(R.id.useremail);

        //get current user
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user==null){
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangePassword=(Button) findViewById(R.id.change_password_button);
        btnRemoveUser=(Button) findViewById(R.id.remove_user_button);
        changePassword = (Button) findViewById(R.id.changePass);
        ParkingLotButton = (Button) findViewById(R.id.Parking_Lot_Button);

        remove=(Button) findViewById(R.id.remove);
        signOut=(Button) findViewById(R.id.sign_out);
        oldEmail=(EditText) findViewById(R.id.old_email);
        password=(EditText) findViewById(R.id.password);
        newPassword=(EditText) findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        //Button Information_button = (Button) findViewById(R.id.Information_button);

        if(progressBar!=null){
            progressBar.setVisibility(View.GONE);
        }

        ParkingLotButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*
                Intent intent= new Intent(getApplicationContext(), ParkingLotActivity.class);
                startActivity(intent);*/
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             progressBar.setVisibility(View.VISIBLE);
                if(user != null&& !newPassword.getText().toString().trim().equals("")){
                    if(newPassword.getText().toString().trim().length()<6){
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    }else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Password is updated, sign in with new password!",
                                                    Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                }else if(newPassword.getText().toString().trim().equals("")){
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar.setVisibility(View.VISIBLE);
                if(user!=null){
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"Your profile is deleted:(Create a account now!",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(MainActivity.this,"Failed to delete your account!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });
        signOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signOut();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user){
        email.setText("User Email:"+user.getEmail());
    }
    //this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener=new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
         FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user==null){
                //user auth state is changed-user is null launch login activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }else{
                setDataToView(user);

            }
        }
    };
    //sign out method
    public void signOut(){
        auth.signOut();

        //this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user == null){
                    //user auth state is change - user is null launch login activity
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        };
    }
    @Override
    protected void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

}
