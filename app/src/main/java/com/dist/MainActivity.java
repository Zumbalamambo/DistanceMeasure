package com.dist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    SensorManager sensorManager;
     SensorEventListener mEventListener=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        final float[] mValuesMagnet      = new float[3];
        final float[] mValuesAccel       = new float[3];
        final float[] mValuesOrientation = new float[3];
        final float[] mRotationMatrix1    = new float[9];
        final float[] mRotationMatrix2    = new float[9];

       // final Button btn_valider = (Button) findViewById(R.id.btn);

        final TextView txt1 = (TextView) findViewById(R.id.answer);
        final TextView txt2=(TextView)findViewById(R.id.tv2);
      // final EditText ed = (EditText)findViewById(R.id.ed1);
     //   final TextView txt3=(TextView)findViewById(R.id.tv3);
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
                //double h = Double.parseDouble(ed.getText().toString());
              double  h= 1.5;
                    double xangle = mValuesOrientation[1];
                if(Math.toDegrees(xangle)<0.5)
                    txt2.setText("INF");
                else
                {double a = Math.tan(xangle);
                    double ans = h / a;
                    double ans1=ans*0.25f;
                    ans=ans-ans1;
                    txt2.setText("" + ans+"-m");
                }
            };
        };

        // You have set the event lisetner up, now just need to register this with the
        // sensor manager along with the sensor wanted.

/*
        btn_valider.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                setListners(sensorManager, mEventListener);

            }
        });*/


    }

    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener) {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListners(sensorManager, mEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(mEventListener);
    }
}
