package com.example.sankarmanoj.pushnotif;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;


public class MainActivity extends Activity {
    InstanceID instanceID;
    String token;
    TextView TV1;
    Boolean registered = false;
    String ProjectID = "authentic-light-87814";
    GoogleCloudMessaging GCM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GCM = GoogleCloudMessaging.getInstance(getApplicationContext());
        TV1 = (TextView)findViewById(R.id.textView);
        TV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TV1 ",TV1.getText().toString());
                if(registered==false)
                {
                    RegisterGCM runner = new RegisterGCM();
                    runner.execute();
                }
            }
        });
        RegisterGCM runner =  new RegisterGCM();
        runner.execute();
    }
    class RegisterGCM extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            TV1.setText(msg);
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("GCM","Started Registration");
            String msg = "";
            try
            {
                msg = GCM.register("354667777132");
                Log.d("GCM","reg id = "+msg);
                registered=true;
            }
            catch (IOException e)
            {
                msg = "Error";
                e.printStackTrace();
            }
            return  msg;
        }

    }


}
