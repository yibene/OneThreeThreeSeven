package com.el.uso.onethreethreeseven.map;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomSupportMapFragment extends SupportMapFragment {

    public interface OnMapReadyListener {
        void onMapReady(GoogleMap map);
    }

    private OnMapReadyListener mOnMapReadyListener;

    public void setOnMapReadyListener(OnMapReadyListener onMapReadyListener) {
        mOnMapReadyListener = onMapReadyListener;
    }

    public static CustomSupportMapFragment newInstance() {
        return new CustomSupportMapFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mOnMapReadyListener.onMapReady(googleMap);
            }
        });
    }

}