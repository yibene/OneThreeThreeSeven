package com.el.uso.onethreethreeseven;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.el.uso.onethreethreeseven.dummy.DummyContent.DummyItem;
import com.el.uso.onethreethreeseven.helper.TaskRunningManager;
import com.el.uso.onethreethreeseven.leet.ProblemSet;
import com.el.uso.onethreethreeseven.leet.ProblemSetFragment;
import com.el.uso.onethreethreeseven.log.L;
import com.el.uso.onethreethreeseven.map.MapFragment;
import com.el.uso.onethreethreeseven.web.CustomWebFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainUIListener {

    private static final String TAG = "MainActivity";
    private FragmentManager mFM;
    private ArrayList<ProblemSet> mProblemSet;
    BottomNavigationView mNavigation;
    private CustomWebFragment mCustomWebView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    BaseFragment baseFragment = BaseFragment.newInstance();
                    mFM.beginTransaction().replace(R.id.fragment_container, baseFragment).commit();
                    return true;
                case R.id.navigation_problems:
                    ProblemSetFragment problemFragment = ProblemSetFragment.newInstance();
                    mFM.beginTransaction().replace(R.id.fragment_container, problemFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    ItemFragment itemFragment = ItemFragment.newInstance();
                    mFM.beginTransaction().replace(R.id.fragment_container, itemFragment).commit();
                    return true;
                case R.id.custom_web_view:
                    mCustomWebView = CustomWebFragment.newInstance();
                    mFM.beginTransaction().replace(R.id.fragment_container, mCustomWebView).commit();
                    return true;
                case R.id.map_view:
                    MapFragment mapFragment = MapFragment.newInstance();
                    mFM.beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.d(TAG, "onCreate");
        mFM = getSupportFragmentManager();
        TaskRunningManager.inst();
        BottomNavigationView mNavigation = findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BaseFragment baseFragment = new BaseFragment();
        mFM.beginTransaction().replace(R.id.fragment_container, baseFragment).commit();
        mProblemSet = new ArrayList<>();
        String[] titles = getProblemSetTitle();
        for (int i = 0; i < titles.length; i++) {
            mProblemSet.add(new ProblemSet("" + (i+1), titles[i]));
        }
        checkPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CustomWebFragment.LOAD_PAYMENT_DATA_REQUEST_CODE:
                Status status = AutoResolveHelper.getStatusFromIntent(data);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        if (mCustomWebView != null && status != null) {
                            mCustomWebView.handlePaymentSuccess(paymentData, status.getStatusCode());
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        if (mCustomWebView != null && status != null) {
                            mCustomWebView.handleError(status.getStatusCode());
                        }
                        break;
                }
                break;
        }
    }

    public String[] getProblemSetTitle() {
        return getResources().getStringArray(R.array.problem_title);
    }

    @Override
    public void queryProblemSetList() {
        ProblemSetFragment fragment = (ProblemSetFragment) mFM.findFragmentById(R.id.fragment_container);
        fragment.updateProblemSetList(mProblemSet);
    }

    @Override
    public void onListFragmentInteraction(DummyItem item) {
        L.d(TAG, "onListFragmentInteraction: " + item.id + ", " + item.content + ": " + item.details);
    }

    public void setNavigationSelected(int itemId) {
        if (mNavigation != null) {
            mNavigation.setSelectedItemId(itemId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static final int PERMISSION_1010 = 1010;

    protected void checkPermission() {

        //android.permission.ACCESS_FINE_LOCATION
        //(for parking card, map, ad, trip track, GoLocManager.inst().updateLocationOnce(),
        //LocationServices.FusedLocationApi.requestLocationUpdates

        //android.permission.GET_ACCOUNTS (for gcm)
        //android.permission.WRITE_EXTERNAL_STORAGE (for log, download)
        //android.permission.READ_EXTERNAL_STORAGE (for log, download)
        //android.permission.READ_CALENDAR
        //android.permission.WRITE_CALENDAR

        ArrayList<String> li = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            li.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            li.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (li.size() > 0) {
            String[] arr = new String[li.size()];
            li.toArray(arr);
            ActivityCompat.requestPermissions(this, arr, PERMISSION_1010);
        }
    }

    @Override
    protected void onDestroy() {
        TaskRunningManager.inst().release();
        super.onDestroy();
    }

}
