package com.example.jisupark.firebaseloginapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button btnChangePassword, btnRemoveUser,changePassword, remove,signOut, ConnectserverButton, ParkingLotButton;
    private TextView email;
    private EditText oldEmail,password,newPassword;
    private ProgressBar progressBar;
    protected FirebaseAuth auth;
    public static final String TAG = MainActivity.class.getSimpleName();
    public DatabaseReference authorizedCar =FirebaseDatabase.getInstance().getReference("AuthorizedCar");

    void checking() {
            authorizedCar = FirebaseDatabase.getInstance().getReference("AuthorizedCar");
            authorizedCar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value;
                    value = dataSnapshot.getValue(String.class);
                    if (value.equals("alarm")) {
                        Toast.makeText(MainActivity.this, "alarm on", Toast.LENGTH_SHORT).show();
                        setAlarm();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
    }

    public void setAlarm()
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
                        Socket socket = null;
                        try {
                            socket = new Socket("192.168.20.86", 5520);
                            //송신
                            OutputStream out = socket.getOutputStream();
                            out.write(1);
                            dialog.cancel();
                        } catch (UnknownHostException e) {
                            // Auto-generated catch block  q
                            e.printStackTrace();
                        } catch (IOException e) {
                            // Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            if (socket != null) {
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    // Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Do not open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        authorizedCar.setValue("00");
                        dialog.cancel();
                    }
                });
        builder.setMessage("Hello, unauthorized car want to enter the parking lot. Would you open the gate?" +
                "");
        AlertDialog diag = builder.create();
        //Display the message!
        diag.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean first = true;
        long start_time = 0;
        long elapsedTime = 0;
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
        ConnectserverButton=(Button) findViewById(R.id.Connect_server_button);
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

        ConnectserverButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= new Intent(getApplicationContext(), SocketActivity.class);
                startActivity(intent);
            }
        }); // main 화면에서 connect_server_button 누르면 server 화면으로 넘어가게 하기 위한 코드

        ParkingLotButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= new Intent(getApplicationContext(), ParkingLotActivity.class);
                startActivity(intent);
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
