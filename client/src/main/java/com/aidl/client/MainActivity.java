package com.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.genius.binder.ILogin;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "geniusmart";

    ServiceConnection mLoginConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {

            // ### aidl onServiceConnected.     service : android.os.BinderProxy
            Log.e(TAG, TAG+"### aidl onServiceConnected.     service : " + binder.getClass().getName());

            ILogin login = ILogin.Stub.asInterface(binder);
            IBinder iBinder = login.asBinder();
            //### after asInterface : com.example.advanceandroid.aidl.ILogin$Stub$Proxy
            Log.e(TAG, TAG+"### after asInterface : " + login.getClass().getName());
            Log.e(TAG, TAG+"### after asBinder : " + iBinder.getClass().getName());

            try {
                //### login : 这是从 com.example.advanceandroid.aidl.LoginService$LoginStubImpl 返回的字符串
                Log.e(TAG, TAG+"### login : " + login.login());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("", TAG+"### aidl disconnected.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        // 服务端的action
        //Intent aidlIntent = new Intent("com.example.advanceandroid.aidl.LoginService");
        Intent  bingIntent = createExplicitFromImplicitIntent(this, new Intent("com.example.advanceandroid.aidl.LoginService"));
        bindService(bingIntent, mLoginConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // unbind
        unbindService(mLoginConnection);
    }

    public Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
