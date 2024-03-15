package com.example.asaditasgourmet.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.asaditasgourmet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeneralFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static final String MY_PREFS_NAME = "MySession";
    public static final String soporte = "soporte";
    public static final String direccion = "direccion";
    public static final String negocio = "negocio";
    public static final String correo = "correo";
    public static final String telefono = "telefono";
    public static final String latitud = "latitud";
    public static final String longitud = "longitud";
    String sop, dir, neg, cor, tel;
    String lat, lon;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_general, container, false);
        mMapView = (MapView) root.findViewById(R.id.map);
        //   mMapView.setBuiltInZoomControls(true);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        mMapView.getMapAsync(this);

        SharedPreferences shared = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sop = (shared.getString(soporte, ""));
        dir = (shared.getString(direccion, ""));
        neg = (shared.getString(negocio, ""));
        cor = (shared.getString(correo, ""));
        tel = (shared.getString(telefono, ""));
        lat = (shared.getString(latitud, ""));
        lon = (shared.getString(longitud, ""));


        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
   // 40.714352, -74.0059731
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        gmap.addMarker(new MarkerOptions().position(ny)
                .position(ny).title(neg)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo))
                .draggable(false).visible(true));
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
