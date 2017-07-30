package com.example.anjali.myapplication3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {
    MyService myService;
    boolean isBind=false;
    Button button,button1;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button1);
        intent=new Intent(this,MyService.class);
    }


    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalService localService = (MyService.LocalService) service;
            myService=localService.getService();
            isBind=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind=false;
        }
    };

    public void startit(View view){


        startService(intent);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }
    public void stopit(View view){
        //intent=new Intent(this,MyService.class);

        stopService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
