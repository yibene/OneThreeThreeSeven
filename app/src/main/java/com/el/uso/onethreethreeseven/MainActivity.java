package com.el.uso.onethreethreeseven;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.leet.ProblemSet;
import com.el.uso.onethreethreeseven.leet.ProblemSetListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainUIListener {

    private FragmentManager mFM;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_problems:
                    mTextMessage.setText(R.string.title_problems);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
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
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void queryProblemSetList() {
        ProblemSetListFragment fragment = (ProblemSetListFragment) mFM.findFragmentByTag(ConstantValues.PROBLEM_SET_LIST_FRAGMENT);
        fragment.updateProblemSetList(new ArrayList<ProblemSet>());
    }

}
