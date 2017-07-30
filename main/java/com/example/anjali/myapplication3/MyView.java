package com.example.anjali.myapplication3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class MyView extends View{
View view1;

    public MyView(Context context) {
        super(context);

        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1=inflater.inflate(R.layout.my_view,null);
    }
    public View returnView(){

        return view1;
    }
}