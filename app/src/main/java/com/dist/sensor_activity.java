package com.dist;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Nishant on 24-Aug-15.
 */
public class sensor_activity {
    SensorManager sensorManager;
    SensorEventListener mEventListener = null;
    String res = null;

    public sensor_activity(final cameraactivity con) {
        sensorManager = (SensorManager) con.getSystemService(con.SENSOR_SERVICE);

        final float[] mValuesMagnet = new float[3];
        final float[] mValuesAccel = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix1 = new float[9];
        final float[] mRotationMatrix2 = new float[9];

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
                Display d = ((WindowManager) con.getSystemService(con.WINDOW_SERVICE)).getDefaultDisplay();
                int rot = d.getRotation();
                SensorManager.getOrientation(mRotationMatrix2, mValuesOrientation);
                //final CharSequence test;
                //test = "results:Rotation: " + rot + "\nZ:" + Math.toDegrees(mValuesOrientation[0]) + " \nX:" + Math.toDegrees(mValuesOrientation[1]) + " \nY:" + Math.toDegrees(mValuesOrientation[2]);

                //txt1.setText(test);
                //double h = Double.parseDouble(ed.getText().toString());
                double h = 1.5;
                double xangle = mValuesOrientation[1];

                if (Math.toDegrees(xangle) < 0.5) {
                    res = "INF";
                    // return res;
                } else {
                    double a = Math.tan(xangle);
                    double ans = h / a;
                    double ans1 = ans * 0.25f;
                    ans = ans - ans1;
                    res = Double.toString(ans) + "m";
                    // return res;
                }
            }


        };
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    public String result()
    {
        return res;
    }
    public SensorEventListener evntlis()
    {
        return mEventListener;
    }
    public SensorManager sensmang()
    {
        return sensorManager;
    }
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener) {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}


