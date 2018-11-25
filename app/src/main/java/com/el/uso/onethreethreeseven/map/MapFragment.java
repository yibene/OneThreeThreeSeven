package com.el.uso.onethreethreeseven.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.Constants;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.Config;
import com.el.uso.onethreethreeseven.helper.MultiDrawable;
import com.el.uso.onethreethreeseven.helper.TaskRunningManager;
import com.el.uso.onethreethreeseven.helper.Utils;
import com.el.uso.onethreethreeseven.log.L;
import com.el.uso.onethreethreeseven.model.ListData;
import com.el.uso.onethreethreeseven.model.ServiceCenterData;
import com.el.uso.onethreethreeseven.model.StationData;
import com.el.uso.onethreethreeseven.web.Result;
import com.el.uso.onethreethreeseven.web.model.ErrorData;
import com.el.uso.onethreethreeseven.web.model.GetStationListOutData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class MapFragment extends BaseFragment implements ClusterManager.OnClusterClickListener<BaseMarker>,
        ClusterManager.OnClusterItemClickListener<BaseMarker>,
        ClusterManager.OnClusterInfoWindowClickListener<BaseMarker>,
        ClusterManager.OnClusterItemInfoWindowClickListener<BaseMarker>,
        TaskRunningManager.OnTaskListener {

    private static final String TAG = "MapFragment";
    private static final String MAP_TAG = "map_fragment";
    private GoogleMap mMap;
    private ClusterManager<BaseMarker> mClusterManager;
    private StationRenderer mStationRenderer;
    private CustomSupportMapFragment mMapFrag;
    private MarkerIconManager mIconManager;
    private final UpdateMapMarkerCallback mUpdateMapMarkerCallback = new UpdateMapMarkerCallback();
    private ShowMarkerRunnable mShowMarkerRunnable;
    private List<StationData> mStationList;
    private List<ServiceCenterData> mServiceCenterList;
    private View mMapLoadingView;
    private ImageView mRefreshBtn;
    private final Handler mHandler = new Handler();

    private Random mRandom = new Random(1984);

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaskRunningManager.inst().registerListener(this);
        if (savedInstanceState == null) {
            mMapFrag = CustomSupportMapFragment.newInstance();
            mMapFrag.setOnMapReadyListener(mOnMapReadyListener);
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.map, mMapFrag, MAP_TAG);
            ft.commitAllowingStateLoss();
        } else {
            FragmentManager fm = getChildFragmentManager();
            mMapFrag = (CustomSupportMapFragment) fm.findFragmentByTag(MAP_TAG);
            mMapFrag.setOnMapReadyListener(mOnMapReadyListener);
        }
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        mMapLoadingView = v.findViewById(R.id.map_loading);
        mShowMarkerRunnable = new ShowMarkerRunnable(mUpdateMapMarkerCallback);
        mUpdateMapMarkerCallback.setFragment(this);
        mRefreshBtn = v.findViewById(R.id.btn_refresh);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                performRefreshBtn();
            }
        });
        mIconManager = new MarkerIconManager();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading(true);
    }

    @Override
    public void onDestroyView() {
        TaskRunningManager.inst().unregisterListener(this);
        super.onDestroyView();
    }

    private final CustomSupportMapFragment.OnMapReadyListener mOnMapReadyListener = new CustomSupportMapFragment.OnMapReadyListener() {
        @Override
        public void onMapReady(GoogleMap map) {
            L.d(TAG, "onMapReady");
            setUpMap(map);
        }
    };

    private void showLoading(boolean show) {
        if (show) {
            mMapLoadingView.setVisibility(View.VISIBLE);
        } else {
            mMapLoadingView.setVisibility(View.GONE);
        }
    }

    private void setUpMap(GoogleMap map) {
        if (mMap != null) return;
        mMap = map;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));

        mClusterManager = new ClusterManager<>(mActivity, mMap);
        mStationRenderer = new StationRenderer();
        mClusterManager.setRenderer(mStationRenderer);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

        loadStationData();
    }

    @Override
    public void onTaskResult(int key, Object result) {
        switch (key) {
            case Constants.TASK_RUN_GET_STATION_DATA:
                GetStationListOutData out = ((Result<GetStationListOutData>) result).result();
                mStationList = Arrays.asList(out.getStationList());
                mServiceCenterList = Arrays.asList(out.getServiceCenterList());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.0579223, 121.5568116), 13.5f), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        mHandler.removeCallbacks(mShowMarkerRunnable);
                        mHandler.post(mShowMarkerRunnable);
                    }

                    @Override
                    public void onCancel() {
                        showLoading(false);
                    }
                });
                break;
        }
    }

    private static class UpdateMapMarkerCallback {
        private WeakReference<MapFragment> mFragment;

        public void setFragment(MapFragment fragment) {
            MapFragment mf = (mFragment == null) ? null : mFragment.get();
            if (mf == null || mf != fragment) {
                mFragment = new WeakReference<>(fragment);
            }
        }
        public void onFinish() {
            L.d(TAG, "UpdateMapMarkerCallback onFinish");
            mFragment.get().showLoading(false);
        }
    }

    private class StationRenderer extends DefaultClusterRenderer<BaseMarker> {
        private final IconGenerator mIconGenerator = new IconGenerator(mActivity.getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(mActivity.getApplicationContext());
//        private final ImageView mImageView;
//        private final ImageView mClusterImageView;
//        private final int mDimension;

        public StationRenderer() {
            super(mActivity.getApplicationContext(), mMap, mClusterManager);
//            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
//            mClusterIconGenerator.setContentView(multiProfile);
//            mClusterImageView = multiProfile.findViewById(R.id.image);
//
//            mImageView = new ImageView(mActivity.getApplicationContext());
//            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
//            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
//            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
//            mImageView.setPadding(padding, padding, padding, padding);
//            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(BaseMarker marker, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            markerOptions.icon(mIconManager.createMapPinBitmapDescriptor(marker.getMarkerIcon(false)));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<BaseMarker> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
            for (BaseMarker marker : cluster.getItems()) {
                if (marker.getMarkerType() == BaseMarker.MarkerType.STATION) {
                    markerOptions.icon(mIconManager.createMapPinBitmapDescriptor(marker.getMarkerIcon(true)));
                    break;
                }
            }
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<BaseMarker> cluster) {
            // Always render clusters.
            boolean isStation = false;
            boolean isServiceCenter = false;
            for (BaseMarker marker : cluster.getItems()) {
                if (marker instanceof StationMarker) {
                    isStation = true;
                }
                if (marker instanceof ServiceCenterMarker) {
                    isServiceCenter = true;
                }
            }
            return (isStation ^ isServiceCenter) && cluster.getSize() > 1;
        }
    }

    private void updateMarkers() {
        for (StationData data : mStationList) {
            mClusterManager.addItem(new StationMarker(data));
        }
        for (ServiceCenterData data : mServiceCenterList) {
            mClusterManager.addItem(new ServiceCenterMarker(data));
        }
        mClusterManager.cluster();
    }

    @Override
    public boolean onClusterClick(Cluster<BaseMarker> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().mName;
        Toast.makeText(mActivity, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<BaseMarker> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public void onClusterItemInfoWindowClick(BaseMarker item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    @Override
    public boolean onClusterItemClick(BaseMarker item) {
        L.d(TAG, "onClusterItem click, item = " + item.mName);
        mStationRenderer.getMarker(item).hideInfoWindow();
        return false;
    }

    private static class ShowMarkerRunnable implements Runnable {
        private final WeakReference<UpdateMapMarkerCallback> mCallback;

        private ShowMarkerRunnable(UpdateMapMarkerCallback updateMapMarkerCallback) {
            mCallback = new WeakReference<>(updateMapMarkerCallback);
        }

        @Override
        public void run() {
            L.d(TAG, "ShowMarkerRunnable run+");
            long startTime = System.currentTimeMillis();
            UpdateMapMarkerCallback callback = mCallback.get();
            if (callback == null) return;
            MapFragment fragment = callback.mFragment.get();

            fragment.updateMarkers();
            L.d(TAG, "ShowMarkerRunnable run-: " + (System.currentTimeMillis() - startTime) + "ms");
            callback.onFinish();
        }
    }

    private void loadStationData() {
        L.w(TAG, "load station data");
        Callable<Object> loadStationCallable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Result<GetStationListOutData> result = new Result<>();
                StringBuilder sb = new StringBuilder();
                try {
                    InputStream json = Config.inst().getAppContext().getAssets().open("station.json");
                    BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));

                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }
                    in.close();
                } catch (FileNotFoundException fe) {
                    L.e(TAG, "asset file station.json not found");
                } catch (Exception e) {
                    L.e(TAG, "loadStationData Error: " + e + ", " + Utils.getStackTrace(e));
                }
                GetStationListOutData out = GetStationListOutData.fromJson(sb.toString());
                result.setResult(out);
                result.setCode(ErrorData.CODE_OKAY);
                return result;
            }
        };

        TaskRunningManager.inst().pushTask(Constants.TASK_RUN_GET_STATION_DATA, loadStationCallable, true);

    }

}
