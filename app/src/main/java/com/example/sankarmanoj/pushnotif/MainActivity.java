package com.example.sankarmanoj.pushnotif;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.google.android.gms.iid.InstanceID;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends Activity {
    InstanceID instanceID;
    String messagetoSend;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    InetAddress AppServerAddress;
    int AppServerRegisterPort=12321;
    TextView TV1;
    int AppServerTalkPort=12322;
    EditText TexttoSend;
    Button ClicktoSend;
    Boolean registered = false;
    String ProjectID = "authentic-light-87814";
    PrintWriter AppServerRegisterOut;
    GoogleCloudMessaging GCM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences= getSharedPreferences("GCMRegister",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        ClicktoSend = (Button)findViewById(R.id.publishServer);
        TexttoSend=(EditText)findViewById(R.id.editText);
        GCM = GoogleCloudMessaging.getInstance(getApplicationContext());
        ClicktoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TexttoSend.getText().toString().length()>0) {
                    TalktoServer talking = new TalktoServer();
                    talking.execute();
                    messagetoSend=TexttoSend.getText().toString();
                }                     TexttoSend.setText("");
            }
        });
        TV1 = (TextView)findViewById(R.id.textView);
        try
        {
            AppServerAddress= InetAddress.getByName("128.199.108.157");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        if(sharedPreferences.getString("GCMID","null").equals("null")) {
            RegisterGCM runner = new RegisterGCM();
            runner.execute();
        }
        else
        {
            TV1.setText("Registered");
            TexttoSend.setVisibility(View.VISIBLE);
            ClicktoSend.setVisibility(View.VISIBLE);
        }
    }
    class TalktoServer extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                 Socket toServerSocket = new Socket(AppServerAddress,AppServerTalkPort);
                PrintWriter AppServerOut=new PrintWriter( new BufferedWriter(new OutputStreamWriter(toServerSocket.getOutputStream())));
                AppServerOut.print(messagetoSend);
                AppServerOut.flush();
                toServerSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    class RegisterGCM extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);
            if(msg.equals("Error"))
            {
                TV1.setText("Unable to Register");
            }
            else {
                TV1.setText("Registered");
                TexttoSend.setVisibility(View.VISIBLE);
                ClicktoSend.setVisibility(View.VISIBLE);
                editor.putString("GCMID",msg);
                editor.commit();
            }
            }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("GCM", "Started Registration");
            String msg = "";
            try
            {
                msg = GCM.register("354667777132");
                Log.d("GCM","reg id = "+msg);
                registered=true;
                Socket AppServerRegister = new Socket(AppServerAddress,AppServerRegisterPort);
                AppServerRegisterOut=new PrintWriter( new BufferedWriter(new OutputStreamWriter(AppServerRegister.getOutputStream())));
                AppServerRegisterOut.print(msg);
                AppServerRegisterOut.flush();
                AppServerRegister.close();
                AppServerRegisterOut=null;

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
