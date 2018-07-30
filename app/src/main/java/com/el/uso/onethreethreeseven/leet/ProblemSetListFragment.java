package com.el.uso.onethreethreeseven.leet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.MainUIListener;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.log.L;

import java.util.List;

/**
 * Created by Cash on 23/01/2018.
 *
 */

public class ProblemSetListFragment extends Fragment {

    private static final String TAG = "ProblemSetListFragment";

    private Context mContext;
    private View mContentView;
    private ListView mProblemSetList;
    private ProblemSetAdapter mAdapter;
    private MainUIListener mCallback;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public static ProblemSetListFragment newInstance(AdapterView.OnItemClickListener listener) {
        ProblemSetListFragment fragment = new ProblemSetListFragment();
        fragment.setOnItemClickListener(listener);
        return fragment;
    }

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
        mAdapter = new ProblemSetAdapter(mContext, R.layout.problem_set_row);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        L.d(TAG, "onCreateView");
        mContentView = inflater.inflate(R.layout.problem_set_list, container, false);
        mProblemSetList = mContentView.findViewById(android.R.id.list);
        mProblemSetList.setOnItemClickListener(mOnItemClickListener);
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
        ((MainActivity) getActivity()).setNavigationSelected(R.id.navigation_problems);
        mCallback.queryProblemSetList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG, "onDestroyView");
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
                LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
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

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

}
