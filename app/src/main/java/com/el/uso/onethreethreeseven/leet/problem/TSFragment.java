package com.el.uso.onethreethreeseven.leet.problem;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.ConstantValues;
import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.leet.DetailFragment;
import com.el.uso.onethreethreeseven.leet.Solution;

/**
 * Created by Cash on 23/01/2018.
 * For detail problem
 */

public class TSFragment extends DetailFragment implements View.OnClickListener {

    private static final String TAG = TSFragment.class.getSimpleName();

    private Context mContext;
    protected SharedPreferences mPrefs;
    private View mContentView;
    private TextView mTitle;
    private TextView mNumbersText;
    private TextView mTargetText;
    private TextView mResult;
    private Button mRoll;
    private Button mCalculate;
    private int[] mNumbers;
    private int mTarget;

    public static TSFragment newInstance(int index) {
        TSFragment f = new TSFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

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
        mContentView = inflater.inflate(R.layout.two_sum, container, false);
        mTitle = mContentView.findViewById(R.id.title);
        mTitle.setText(((MainActivity) mContext).getProblemSetTitle()[(int) getArguments().get("index")]);
        mNumbersText = mContentView.findViewById(R.id.numbers);
        mTargetText = mContentView.findViewById(R.id.target);
        mResult = mContentView.findViewById(R.id.result);
        mRoll = mContentView.findViewById(R.id.roll);
        mRoll.setOnClickListener(this);
        mCalculate = mContentView.findViewById(R.id.calculate);
        mCalculate.setOnClickListener(this);
        return mContentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roll:
                mNumbers = Solution.getInstance().getNRandomNumbers(1000, 10);
                int[] indices = Solution.getInstance().getNRandomNumbers(10, 2);
                mTarget = mNumbers[indices[0]] + mNumbers[indices[1]];
                mNumbersText.setText(Solution.getInstance().printArray(mNumbers));
                mTargetText.setText("target = " + mTarget);
                break;
            case R.id.calculate:
                int[] result = Solution.getInstance().twoSum(mNumbers, mTarget);
                mResult.setText(Solution.getInstance().printArray(result));
                break;
            default:
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
