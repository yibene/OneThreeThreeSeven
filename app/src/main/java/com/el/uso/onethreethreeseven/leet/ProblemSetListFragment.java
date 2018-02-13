package com.el.uso.onethreethreeseven.leet;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.ConstantValues;
import com.el.uso.onethreethreeseven.MainUIListener;
import com.el.uso.onethreethreeseven.R;

import java.util.List;

/**
 * Created by Cash on 23/01/2018.
 *
 */

public class ProblemSetListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static String TAG = ProblemSetListFragment.class.getSimpleName();

    private Context mContext;
    private View mContentView;
    private ListView mProblemSetList;
    private ProblemSetAdapter mAdapter;
    private MainUIListener mCallback;

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
        mAdapter = new ProblemSetAdapter(mContext, R.layout.problem_set_row);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.problem_set_list, container, false);
        mProblemSetList = mContentView.findViewById(android.R.id.list);
        mProblemSetList.setOnItemClickListener(this);
        mProblemSetList.setAdapter(mAdapter);

        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.queryProblemSetList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailFragment detail = DetailFragment.newInstance(position);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, detail);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void updateProblemSetList(List<ProblemSet> problemSets) {
        mAdapter.clear();
        if (problemSets != null) {
            for (ProblemSet s : problemSets) {
                mAdapter.add(s);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private class ProblemSetAdapter extends ArrayAdapter<ProblemSet> {

        private Context mContext;
        private int mLayoutResId;

        public ProblemSetAdapter(Context context, int layoutResId) {
            super(context, layoutResId);
            mContext = context;
            mLayoutResId = layoutResId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ProblemSet problem = getItem(position);
            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(mLayoutResId, parent, false);
            }
            TextView numberView = row.findViewById(R.id.problem_number);
            TextView titleView = row.findViewById(R.id.problem_title);
            if (problem != null) {
                numberView.setText(problem.number);
                titleView.setText(problem.title);
            }
            return row;
        }
    }
}
