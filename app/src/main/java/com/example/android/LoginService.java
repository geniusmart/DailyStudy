package com.example.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.genius.binder.ILogin;

public class LoginService extends Service {

    IBinder mBinder = new ILogin.Stub(){
        @Override
        public String login() throws RemoteException {
            return "这是从 " + this.getClass().getName() + " 返回的字符串";
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
