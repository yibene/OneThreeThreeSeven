package com.el.uso.onethreethreeseven.leet;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.ConstantValues;
import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.log.L;

/**
 * Created by Cash on 23/01/2018.
 * For detail problem
 */

public class DetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DetailFragment";

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
        L.d(TAG, "onCreateView");
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
//                int num1 = (int) (Math.random() * 100000);
//                int num2 = (int) (Math.random() * 100000);
                int[] num = Solution.getInstance().getNRandomNumbers(1000000, 2);
                mNode1 = Solution.getInstance().generateNode(num[0]);
                mNode2 = Solution.getInstance().generateNode(num[1]);
                mNodeText1.setText(Solution.getInstance().generateNode(num[0]).dumpNode());
                mNodeText2.setText(Solution.getInstance().generateNode(num[1]).dumpNode());
                break;
            case R.id.calculate:
                mResult.setText(Solution.getInstance().addTwoNumbers(mNode1, mNode2).dumpNode());
                long time = System.currentTimeMillis();
                String[] ss = {"0","11","1000","01","0","101","1","1","1","0","0","0","0","1","0","0110101","0","11","01","00","01111","0011","1","1000","0","11101","1","0","10","0111"};
                L.d(TAG, "findMaxForm = " + Solution.getInstance().findMaxForm(ss, 9, 80));
                L.d(TAG, "time = " + (System.currentTimeMillis() - time)/1000f + "sec");
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
        ((MainActivity) getActivity()).setNavigationSelected(R.id.navigation_problems);
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
        L.d(TAG, "onDestroyView");
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
