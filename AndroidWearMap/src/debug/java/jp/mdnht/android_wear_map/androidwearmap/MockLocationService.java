package jp.mdnht.android_wear_map.androidwearmap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MockLocationService extends Service {
    public MockLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

