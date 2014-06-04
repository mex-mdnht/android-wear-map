package jp.mdnht.android_wear_map.androidwearmap;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.IOException;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.navi.NaviController;
import jp.co.yahoo.android.maps.routing.RouteOverlay;


public class MainActivity extends MapActivity implements RouteOverlay.RouteOverlayListener, NaviController.NaviControllerListener {

    private NaviController naviController = null;
    private  MapView mapView;

    private Address startAddress;
    private Address finishAddress;
    private RouteOverlay routeOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GeoPoint gp = new GeoPoint(35658630,139745410);
        float lat = 36.637166f;
        float lng = 139.06966f;
        GeoPoint sp = new GeoPoint((int)(lat * 1E6), (int)(lng * 1E6));

       mapView = new MapView(this,"dj0zaiZpPVNoakRjUUpQNkRMSyZzPWNvbnN1bWVyc2VjcmV0Jng9ZDM-");
        MapController c = mapView.getMapController();
        c.setCenter(sp);
        c.setZoom(1);

        FrameLayout frame =  (FrameLayout)findViewById(R.id.mapFrame);
       // frame.addView(mapView);

        //setContentView(mapView);

        WebView wv = (WebView)findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/www/gmap.html");

        //RouteOverlay作成
       routeOverlay = new RouteOverlay(this,"dj0zaiZpPVNoakRjUUpQNkRMSyZzPWNvbnN1bWVyc2VjcmV0Jng9ZDM-");



        //MapViewにRouteOverlayを追加
        mapView.getOverlays().add(routeOverlay);



        //searchButton
       /* Button notifyButton = (Button) findViewById(R.id.notifyButton);
        notifyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //createNotification();
                setMockLoacation();
            }
         });*/

        //startPosition
        Button searchStartPointButton = (Button) findViewById(R.id.searchStartPointButton);
        searchStartPointButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.startPointText);
                String startPointSearchQuery = (ed).getText().toString();
                startAddress = geoCode(startPointSearchQuery);
                ed.setText(startAddress.getAddressLine(0));
            }
        });

        //endPosition
        Button searchFinishPointButton = (Button) findViewById(R.id.searchFinishPointButton);
        searchFinishPointButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.finishiPointText);
                String finishPointSearchQuery = (ed).getText().toString();
                finishAddress = geoCode(finishPointSearchQuery);
                ed.setText(finishAddress.getAddressLine(0));
            }
        });

        //startNavigationButton
        Button searchRouteButton = (Button) findViewById(R.id.searchRouteButton);
        searchRouteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                searchRoute();
            }
        });


}

    private void searchRoute() {
        //出発地、目的地、移動手段を設定

        routeOverlay.setRoutePos(new GeoPoint((int)(startAddress.getLatitude()*1E6),(int)(startAddress.getLongitude()*1E6)),
                new GeoPoint((int)(finishAddress.getLatitude()*1E6),(int)(finishAddress.getLongitude()*1E6)), RouteOverlay.TRAFFIC_WALK);

        //RouteOverlayListenerの設定
        routeOverlay.setRouteOverlayListener(this);

        //検索を開始
        routeOverlay.search();
    }

    private Address geoCode(String searchQuery) {
        Geocoder geocoder = new Geocoder(this);
        try {
            return geocoder.getFromLocationName(searchQuery,1).get(0);
        } catch (IOException e) {
            return null;
        }
    }


    private void setMockLoacation() {
        //mock
        Location newLocation = new Location(LocationManager.GPS_PROVIDER);
        newLocation.setLatitude(36.637166f);
        newLocation.setLongitude(139.06966f);
        newLocation.setAccuracy(3.0f);
        newLocation.setElapsedRealtimeNanos(2);
        newLocation.setTime(System.currentTimeMillis());

        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.addTestProvider(LocationManager.GPS_PROVIDER,false,false,false,false,false,true,true,0,5);
        manager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
       manager.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
        // LocationClient lc = new LocationClient(this, this, this);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void createNotification() {
        int notificationId = 001;
        // Build intent for notification content
        //Intent viewIntent = new Intent(this, ViewEventActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
       /* PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);*/

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("右折");

                        //.setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean finishRouteSearch(RouteOverlay routeOverlay) {
        //NaviControllerを作成しRouteOverlayインスタンスを設定
        naviController = new NaviController(this,routeOverlay);

        //MapViewインスタンスを設定
        naviController.setMapView(mapView);

        //NaviControllerListenerを設定
        naviController.setNaviControlListener(this);

        //案内処理を開始
        naviController.start();
        return false;
    }

    @Override
    public boolean errorRouteSearch(RouteOverlay routeOverlay, int i) {
        return true;
    }

    @Override
    public boolean onLocationChanged(NaviController naviController) {

        return false;
    }

    @Override
    public boolean onLocationTimeOver(NaviController naviController) {
        return false;
    }

    @Override
    public boolean onLocationAccuracyBad(NaviController naviController) {
        return false;
    }

    @Override
    public boolean onRouteOut(NaviController naviController) {
        return false;
    }

    @Override
    public boolean onGoal(NaviController naviController) {
        return false;
    }
}
