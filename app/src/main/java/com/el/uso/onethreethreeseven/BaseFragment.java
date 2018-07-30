package com.el.uso.onethreethreeseven;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.log.L;
import com.el.uso.onethreethreeseven.view.FlatButton;

/**
 * Created by Cash on 23/01/2018.
 *
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    private Context mContext;
    protected SharedPreferences mPrefs;
    private View mContentView;
    private TextView mTextMessage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = mContext.getSharedPreferences(ConstantValues.PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d(TAG, "onCreateView");
        mContentView = inflater.inflate(R.layout.base_fragment, container, false);
        mTextMessage = mContentView.findViewById(R.id.message);
        final FlatButton flat = mContentView.findViewById(R.id.test_message);
        flat.setTag(R.id.showCustom, true);
        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAscii = (boolean) flat.getTag(R.id.showCustom);
                String text = (isAscii) ? getTest2() : getTest();
                flat.setText(text);
                flat.setTag(R.id.showCustom, !isAscii);
            }
        });
        final FlatButton flat2 = mContentView.findViewById(R.id.test_message2);
        flat2.setTag(R.id.showCustom, true);
        flat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMulti = (boolean) flat2.getTag(R.id.showCustom);
                String text = (isMulti) ? getTest() + "\n" + getTest4() : getTest();
                flat2.setText(text);
                flat2.setTag(R.id.showCustom, !isMulti);
            }
        });
        final FlatButton flat3 = mContentView.findViewById(R.id.test_message3);
        flat3.setTag(R.id.showCustom, true);
        flat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAscii = (boolean) flat3.getTag(R.id.showCustom);
                String text = (isAscii) ? getTest4() : getTest3();
                flat3.setText(text);
                flat3.setTag(R.id.showCustom, !isAscii);
            }
        });
        return mContentView;
    }

    private String getR() {
        return "Ⓡ";
    }

    private String getTest() {
        return getString(R.string.test_string);
    }

    private String getTest2() {
        return getString(R.string.test_string2);
    }

    private String getTest3() {
        return getString(R.string.test_string3);
    }

    private String getTest4() {
        return getString(R.string.test_string4);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setNavigationSelected(R.id.navigation_home);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
