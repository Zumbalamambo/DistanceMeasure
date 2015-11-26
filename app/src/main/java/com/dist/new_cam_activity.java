package com.dist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nishant on 24-Aug-15.
 */
public class new_cam_activity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    SensorManager sensorManager;
    SensorEventListener mEventListener=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix1    = new float[9];
        final float[] mRotationMatrix2    = new float[9];

        final TextView txt1 = (TextView) findViewById(R.id.answer);

        mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;

                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                }
                SensorManager.getRotationMatrix(mRotationMatrix1, null, mValuesAccel, mValuesMagnet);
                // SensorManager.getOrientation(mRotationMatrix1, mValuesOrientation);
                SensorManager.remapCoordinateSystem(mRotationMatrix1,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, mRotationMatrix2);
                Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int rot = d.getRotation();
                SensorManager.getOrientation(mRotationMatrix2, mValuesOrientation);
                final CharSequence test;
                test = "results:Rotation: " + rot + "\nZ:" + Math.toDegrees(mValuesOrientation[0]) + " \nX:" + Math.toDegrees(mValuesOrientation[1]) + " \nY:" + Math.toDegrees(mValuesOrientation[2]);

                txt1.setText(test);
                SharedPreferences sp=getSharedPreferences("MyCred", MODE_PRIVATE);
                String log=sp.getString("k1","unable to fetch");
                double h = Double.parseDouble(log);
                //double  h= 1.5;
                double xangle = mValuesOrientation[1];
                if(Math.toDegrees(xangle)<0.5)
                    txt1.setText("INF");
                else
                {double a = Math.tan(xangle);
                    double ans = h / a;
                   // double ans1=ans*0.25f;
                   // ans=ans-ans1;
                    txt1.setText("" + ans+"-m");
                }
            };
        };


        // Create an instance of Camera







        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        Camera.Parameters params1 = mCamera.getParameters();
        params1.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(params1);
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camp);
        ImageView im = (ImageView)findViewById(R.id.imageView);
        im.setImageResource(android.R.drawable.button_onoff_indicator_on);
        FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        FrameLayout.LayoutParams ltxt = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.END);
        ((ViewGroup) im.getParent()).removeView(im);
        ((ViewGroup)txt1.getParent()).removeView(txt1);
        preview.addView(mPreview);
        preview.addView(im,l);
        preview.addView(txt1,ltxt);

        Button b = (Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListners(sensorManager,mEventListener);
                startActivity(new Intent(new_cam_activity.this,MainActivity.class));
            }
        });

    }
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener) {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c;

    }
    private void releaseCam()
    {
        if(mCamera!=null)
            mCamera.release();
        mCamera=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera=getCameraInstance();
        setListners(sensorManager, mEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(mEventListener);
        releaseCam();
    }
}

