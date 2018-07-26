package com.example.jisupark.firebaseloginapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
class Pop extends Activity {
    int buttonNum;
    Boolean empty;
    Pop()
    {
        buttonNum = 0;
        empty = false;
    }
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
        }

    }
    void display()
    {

    }
}
