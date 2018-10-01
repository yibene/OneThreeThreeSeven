package com.el.uso.onethreethreeseven.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.el.uso.onethreethreeseven.BaseFragment;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.MultiDrawable;
import com.el.uso.onethreethreeseven.helper.Utils;
import com.el.uso.onethreethreeseven.log.L;
import com.el.uso.onethreethreeseven.model.ListData;
import com.el.uso.onethreethreeseven.model.StationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapFragment extends BaseFragment implements ClusterManager.OnClusterClickListener<BaseMarker>,
        ClusterManager.OnClusterItemClickListener<BaseMarker>,
        ClusterManager.OnClusterInfoWindowClickListener<BaseMarker>,
        ClusterManager.OnClusterItemInfoWindowClickListener<BaseMarker> {

    private static final String TAG = "MapFragment";
    private static final String MAP_TAG = "map_fragment";
    private GoogleMap mMap;
    private ClusterManager<BaseMarker> mClusterManager;
    private StationRenderer mStationRenderer;
    private CustomSupportMapFragment mMapFrag;
    private View mMapLoadingView;
    private ImageView mRefreshBtn;

    private Random mRandom = new Random(1984);

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        mRefreshBtn = v.findViewById(R.id.btn_refresh);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                performRefreshBtn();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapLoadingView.setVisibility(View.VISIBLE);
    }

    private final CustomSupportMapFragment.OnMapReadyListener mOnMapReadyListener = new CustomSupportMapFragment.OnMapReadyListener() {
        @Override
        public void onMapReady(GoogleMap map) {
            L.d(TAG, "onMapReady");
            setUpMap(map);
        }
    };

    private void setUpMap(GoogleMap map) {
        if (mMap != null) return;
        mMap = map;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setMapToolbarEnabled(false);

        mMapLoadingView.setVisibility(View.GONE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
        mClusterManager = new ClusterManager<>(mActivity, mMap);
        mStationRenderer = new StationRenderer();
        mClusterManager.setRenderer(mStationRenderer);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);

//        addItems();
        String out = Utils.readFromRaw(mActivity, R.raw.station);
        L.d(TAG, "out = " + out);
        StationData[] stationList = ListData.fromJson(Utils.readFromRaw(mActivity, R.raw.station)).getStationList();
        if (stationList != null) {
            for (StationData data : stationList) {
                mClusterManager.addItem(new StationMarker(data));
            }
        }
        mClusterManager.cluster();
//        try {
//            readItems();
//        } catch (JSONException e) {
//            L.e(TAG, "exception on readItems: " + e.toString());
//        }

    }

    private class StationRenderer extends DefaultClusterRenderer<BaseMarker> {
        private final IconGenerator mIconGenerator = new IconGenerator(mActivity.getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(mActivity.getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public StationRenderer() {
            super(mActivity.getApplicationContext(), mMap, mClusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(mActivity.getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(BaseMarker marker, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(marker.mMarkerIcon);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(marker.mName);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<BaseMarker> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (BaseMarker m : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(m.mMarkerIcon);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
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

    private void addItems() {
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mClusterManager.addItem(new StationMarker(position(), "Walter", R.drawable.walter));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mClusterManager.addItem(new StationMarker(position(), "Gran", R.drawable.gran));

        // http://www.flickr.com/photos/nypl/3111525394/
        mClusterManager.addItem(new StationMarker(position(), "Ruth", R.drawable.ruth));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mClusterManager.addItem(new StationMarker(position(), "Stefan", R.drawable.stefan));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mClusterManager.addItem(new StationMarker(position(), "Mechanic", R.drawable.mechanic));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mClusterManager.addItem(new StationMarker(position(), "Yeats", R.drawable.yeats));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mClusterManager.addItem(new StationMarker(position(), "John", R.drawable.john));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mClusterManager.addItem(new StationMarker(position(), "Trevor the Turtle", R.drawable.turtle));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        mClusterManager.addItem(new StationMarker(position(), "Teach", R.drawable.teacher));
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<BaseMarker> items = Utils.fromRawJson(inputStream);
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            for (BaseMarker item : items) {
                LatLng position = item.getPosition();
                double lat = position.latitude + offset;
                double lng = position.longitude + offset;
                BaseMarker offsetItem = new BaseMarker(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        }
    }

    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }

}
