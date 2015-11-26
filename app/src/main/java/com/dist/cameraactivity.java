package com.dist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nishant on 23-Aug-15.
 */
public class cameraactivity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    sensor_activity s = null;
SensorManager sm;
    SensorEventListener ev;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameralayout);
        s=new sensor_activity(cameraactivity.this);
        String a = null;
        a=s.result();
        sm=s.sensmang();
        ev= s.evntlis();
        s.setListners(sm,ev);
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        Camera.Parameters params1 = mCamera.getParameters();
         params1.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(params1);
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camp);
        ImageView im = (ImageView)findViewById(R.id.imageView);
        TextView txt=(TextView)findViewById(R.id.answer);
        txt.setText(a);
        im.setImageResource(android.R.drawable.button_onoff_indicator_on);
       FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,Gravity.CENTER);
                   ((ViewGroup) im.getParent()).removeView(im);
        ((ViewGroup)txt.getParent()).removeView(txt);
        preview.addView(mPreview);
        preview.addView(im,l);
        preview.addView(txt);

        Button b = (Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cameraactivity.this,MainActivity.class));
            }
        });

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
        s.setListners(sm,ev);
        mCamera=getCameraInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
      sm.unregisterListener(ev);
        releaseCam();
    }
}
