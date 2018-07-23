package com.el.uso.onethreethreeseven;

import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.el.uso.onethreethreeseven.dummy.DummyContent.DummyItem;
import com.el.uso.onethreethreeseven.leet.ProblemSet;
import com.el.uso.onethreethreeseven.leet.ProblemSetListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainUIListener {

    private FragmentManager mFM;
    private ArrayList<ProblemSet> mProblemSet;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    BaseFragment baseFragment = new BaseFragment();
                    mFM.beginTransaction().replace(R.id.fragment_container, baseFragment).commit();
                    return true;
                case R.id.navigation_problems:
                    ProblemSetListFragment problemFragment = new ProblemSetListFragment();
                    mFM.beginTransaction().replace(R.id.fragment_container, problemFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    ItemFragment itemFragment = new ItemFragment();
                    mFM.beginTransaction().replace(R.id.fragment_container, itemFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFM = getFragmentManager();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BaseFragment baseFragment = new BaseFragment();
        mFM.beginTransaction().add(R.id.fragment_container, baseFragment).commit();
        mProblemSet = new ArrayList<>();
        String[] titles = getProblemSetTitle();
        for (int i = 0; i < titles.length; i++) {
            mProblemSet.add(new ProblemSet("" + (i+1), titles[i]));
        }
        checkPermission();
    }

    public String[] getProblemSetTitle() {
        return getResources().getStringArray(R.array.problem_title);
    }

    @Override
    public void queryProblemSetList() {
        ProblemSetListFragment fragment = (ProblemSetListFragment) mFM.findFragmentById(R.id.fragment_container);
        fragment.updateProblemSetList(mProblemSet);
    }

    @Override
    public void onListFragmentInteraction(DummyItem item) {

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

}
