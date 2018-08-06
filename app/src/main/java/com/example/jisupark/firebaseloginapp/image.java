package com.example.jisupark.firebaseloginapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class image extends AppCompatActivity {
        public void main(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://smartparking-4ec8d.appspot.com").child("car-photo.jpeg");
        ImageView mImageView = (ImageView) findViewById(R.id.image);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(mImageView);}

        /*try {
            final File localFile = File.createTempFile("image", "jpeg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ImageView mImageView = (ImageView) findViewById(R.id.image);
                    mImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }*/
        class MyClientTask extends AsyncTask<Void, Void, Void> {
            String dstAddress;
            int dstPort;
            String response = "";
            String myMessage = "";

            //constructor
            MyClientTask(String addr, int port, String message) {
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
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
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
                } finally {
                    if (socket != null) {
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

    DatabaseReference authorizedCar = FirebaseDatabase.getInstance().getReference("AuthorizedCar");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        main();
        Button open = (Button) findViewById(R.id.OpenBtn);
        Button doNotOpen = (Button) findViewById(R.id.notOpenBtn);
            open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizedCar.setValue("00");
                image.MyClientTask myClientTask = new image.MyClientTask("192.168.20.86", 5521, "open");
                myClientTask.execute();
                finish();
            }
        });

        doNotOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizedCar.setValue("00");
                finish();
            }
        });

    }
}