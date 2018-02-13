package com.el.uso.onethreethreeseven.leet;

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

/**
 * Created by Cash on 23/01/2018.
 * For detail problem
 */

public class DetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DetailFragment.class.getSimpleName();

    private Context mContext;
    protected SharedPreferences mPrefs;
    private View mContentView;
    private TextView mTitle;
    private TextView mNodeText1;
    private TextView mNodeText2;
    private TextView mResult;
    private Button mRoll;
    private Button mCalculate;
    private Solution.ListNode mNode1;
    private Solution.ListNode mNode2;

    public static DetailFragment newInstance(int index) {
        DetailFragment f = new DetailFragment();

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
        mContentView = inflater.inflate(R.layout.detail_fragment, container, false);
        mTitle = mContentView.findViewById(R.id.title);
        mTitle.setText(((MainActivity) mContext).getProblemSetTitle()[(int) getArguments().get("index")]);
        mNodeText1 = mContentView.findViewById(R.id.node1);
        mNodeText2 = mContentView.findViewById(R.id.node2);
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
                int num1 = (int) (Math.random() * 100000);
                int num2 = (int) (Math.random() * 100000);
                Log.w(TAG, "node1 = " + num1 + ", node2 = " + num2);
                mNode1 = Solution.getInstance().generateNode(num1);
                mNode2 = Solution.getInstance().generateNode(num2);
                mNodeText1.setText(mNode1.printNode());
                mNodeText2.setText(mNode2.printNode());
                break;
            case R.id.calculate:
                mResult.setText(Solution.getInstance().addTwoNumbers(mNode1, mNode2).printNode());
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
