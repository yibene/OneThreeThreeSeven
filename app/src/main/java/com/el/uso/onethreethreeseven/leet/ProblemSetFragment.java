package com.el.uso.onethreethreeseven.leet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.ConstantValues;
import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.MainUIListener;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.leet.problem.ATNFragment;
import com.el.uso.onethreethreeseven.leet.problem.TSFragment;
import com.el.uso.onethreethreeseven.log.L;

import java.util.List;

/**
 * Created by Cash on 23/01/2018.
 *
 */

public class ProblemSetFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "ProblemSetListFragment";

    private Context mContext;
    private View mContentView;
    private MainUIListener mCallback;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    ProblemSetListFragment mListFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (MainUIListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainUIListener");
        }
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
        mContentView = inflater.inflate(R.layout.problem_set, container, false);

        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListFragment = ProblemSetListFragment.newInstance(this);
        FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_inside, mListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setNavigationSelected(R.id.navigation_problems);
    }

    @Override
    public boolean onBackPressed() {
        if (mListFragment.isHidden()) {
            FragmentManager fm = this.getChildFragmentManager();
            fm.popBackStack();
            FragmentTransaction ft = fm.beginTransaction();
            ft.show(mListFragment);
            ft.commitAllowingStateLoss();
            return true;
        } else {
            return super.onBackPressed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "onDestroyView");
    }

    public void updateProblemSetList(List<ProblemSet> problemSets) {
        mListFragment.updateProblemSetList(problemSets);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailFragment detail;
        switch (position) {
            case 0:
                detail = TSFragment.newInstance(position);
                break;
            case 1:
                detail = ATNFragment.newInstance(position);
                break;
            default:
                detail = DetailFragment.newInstance(position);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(mListFragment);
        ft.setCustomAnimations(R.anim.fragment_slide_up, R.anim.fragment_fade_out);
        ft.replace(R.id.fragment_container_inside, detail);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

}
