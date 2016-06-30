package com.arush.android.helloservice;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        textView = new TextView(this);
        textView.append("Using Services\n");
        setContentView(textView);

        textView.append("Account Manager\n");
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        //Note! Android.permission.GET_ACCOUNTS needed
        Account[] accounts = accountManager.getAccounts();
        if (accounts.length == 0) {
            textView.append("No Accounts!\n");
        } else {
            textView.append("Found " + accounts.length + " accounts!\n");
        }

        textView.append("\nConnectivity Manager\n");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //Note! Android.permission.ACCESS_NETWORK_STATE needed
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : networkInfos) {
            textView.append(ni.getTypeName() + "-" + ni.isAvailable() + "\n");
        }

        textView.append("\nLocation Manager\n");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //Note! Android.permission.ACCESS_FINE_LOCATION needed
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            textView.append("provider: " + provider + "\n");
        }

        String provider = providers.get(1);
        textView.append("\nUsing " + provider + "\n");
        long minTime = 5 * 1000; //5 seconds
        float minDistance = 5; //5 meters
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, minTime, minDistance, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.append("onLocationChanged\n");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                textView.append("onStatusChanged\n");
            }

            @Override
            public void onProviderEnabled(String provider) {
                textView.append("onProviderEnabled\n");
            }

            @Override
            public void onProviderDisabled(String provider) {
                textView.append("onProviderDisabled\n");
            }
        });

    }

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.append(intent.getAction() + "\n");
        }
    };

    private final IntentFilter myFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, myFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
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
}
