package com.dist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Nishant on 24-Aug-15.
 */
public class splash extends Activity {
    double h;
    String log=null;
    EditText ed;
    static final String DEFAULT = "??";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ed = (EditText)findViewById(R.id.edt);
        SharedPreferences sp=getSharedPreferences("MyCred", MODE_PRIVATE);
         log=sp.getString("k1", DEFAULT);
        ed.setText(log);
        Button b = (Button)findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log=ed.getText().toString();
                SharedPreferences sp=getSharedPreferences("MyCred", Context.MODE_PRIVATE);
                SharedPreferences.Editor e=sp.edit();
                e.putString("k1", log);
                e.commit();
                startActivity(new Intent(splash.this,new_cam_activity.class));
            }
        });

    }
}
