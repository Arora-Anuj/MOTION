package com.example.anjali.myapplication3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.SlidingDrawer;

import java.util.List;


public class MyService extends Service implements SensorEventListener {
            private final IBinder mBinder=new LocalService();
            private View view1;
            GridView drawerGrid;
            Pac[] pacs;
            PackageManager pm;
            DrawerAdapter drawerAdapterObject;
        boolean isBind=false;
    boolean isOpen=false;
    private SensorManager sManager;

    SlidingDrawer drawer;
    //for shake detection
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 3000;



            private WindowManager windowManager;



        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);



            @Override
            public IBinder onBind(Intent intent) {
                isBind=true;
                return mBinder;
    }



            public class LocalService extends Binder{

                MyService getService(){

                    return MyService.this;
                }

            }

            @Override
            public void onCreate() {
                super.onCreate();
                MyView myView =new MyView(this);
                view1=myView.returnView();
                isBind=true;


                drawer=(SlidingDrawer)view1.findViewById(R.id.drawer);


                pm =getPackageManager();
                set_pacs();
                drawerGrid=(GridView)view1.findViewById(R.id.content);
                drawerAdapterObject = new DrawerAdapter(this, pacs);
                drawerGrid.setAdapter(drawerAdapterObject);
                drawerGrid.setOnItemClickListener(new DrawerClickListener(this, pacs, pm));

                sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_FASTEST);





              //for shake detection
                senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);







                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                params.x = 0;                params.y = 0;


                windowManager.addView(view1, params);
            }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(view1);
        sManager.unregisterListener(this);
    }

    public void set_pacs(){
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
                pacs = new Pac[pacsList.size()];
                for(int I=0;I<pacsList.size();I++){
                    pacs[I]= new Pac();
                    pacs[I].icon=pacsList.get(I).loadIcon(pm);
                    pacs[I].name=pacsList.get(I).activityInfo.packageName;
                    pacs[I].label=pacsList.get(I).loadLabel(pm).toString();
                }
                new SortApps().exchange_sort(pacs);
            }
//motion detecting events

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        //else it will output the Roll, Pitch and Yawn values

        if (event.values[1] < -0.5) {
            drawerGrid.smoothScrollByOffset(-2);
        }
        if (event.values[1]> 5.0) {
            drawerGrid.smoothScrollByOffset(2);

        }


        //for shake detection
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                    //implement here
                    if (isOpen==false) {
                        drawer.open();
                        isOpen = true;
                    }
                    else{
                        isOpen = false;
                    }


                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    }



