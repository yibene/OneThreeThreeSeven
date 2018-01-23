package com.el.uso.onethreethreeseven.leet;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.ConstantValues;
import com.el.uso.onethreethreeseven.R;

/**
 * Created by Cash on 23/01/2018.
 *
 */

public class ProblemSetListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static String TAG = ProblemSetListFragment.class.getSimpleName();

    private Context mContext;
    private View mContentView;
    private ListView mProblemSetList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = mContext.getSharedPreferences(ConstantValues.PREFS_NAME, Context.MODE_PRIVATE);
        mProblemSetList.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.problem_set_list, container, false);
        mProblemSetList = mContentView.findViewById(android.R.id.list);
        return mContentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
