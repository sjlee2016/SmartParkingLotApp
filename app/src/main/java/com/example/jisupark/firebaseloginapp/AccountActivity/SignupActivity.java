package com.example.jisupark.firebaseloginapp.AccountActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jisupark.firebaseloginapp.FirebasePost;
import com.example.jisupark.firebaseloginapp.MainActivity;
import com.example.jisupark.firebaseloginapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private DatabaseReference mPostReference;

    EditText edit_ID;
    EditText edit_Name;
    TextView text_ID;
    TextView text_Name;

    String ID;
    String name;

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    public void setInsertMode() {
        edit_ID.setText("");
        edit_Name.setText("");
        btnSignUp.setEnabled(true);
    }

    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebasePost post = new FirebasePost(ID, name);
            postValues = post.toMap();
        }
        childUpdates.put("/CarLicense_list/" + ID, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        text_ID = (TextView) findViewById(R.id.text_id);
        text_Name = (TextView) findViewById(R.id.text_name);
        btnSignUp.setEnabled(true);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_signup:
                        ID = edit_ID.getText().toString();
                        name = edit_Name.getText().toString();
                        if (!IsExistCarLicense()) {
                            postFirebaseDatabase(true);
                            setInsertMode();
                        } else {
                            Toast.makeText(SignupActivity.this, "This CarLicense exists already. Please check your email and password again.", Toast.LENGTH_LONG).show();
                        }
                        edit_ID.requestFocus();
                        edit_ID.setCursorVisible(true);
                        break;
                }
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                name = edit_Name.getText().toString().trim();
                ID = edit_ID.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!IsExistCarLicense()) {
                            postFirebaseDatabase(true);
                            setInsertMode();
                        } else {
                            Toast.makeText(SignupActivity.this, "This CarLicense exists already. Please check your email and password again.", Toast.LENGTH_LONG).show();
                        }
                        edit_ID.requestFocus();
                        edit_ID.setCursorVisible(true);

                progressBar.setVisibility(View.VISIBLE);
                //CREATE USER
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete" + task.isSuccessful(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                //If sign in fails, display a message to the user. If sign in succeeds the auth state listener will be notified
                                // and logic to handle the signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    public boolean IsExistCarLicense() {
        boolean IsExist = arrayIndex.contains(ID);
        return IsExist;
    }
}