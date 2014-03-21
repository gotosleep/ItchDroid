package io.itch.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        Authenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }

}
