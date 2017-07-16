package com.pushkaranand.googlemaps;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.places.*;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "Place";
    SearchView svFrom, svTo;
    LatLng from, to;
    Marker from_marker = null, to_marker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setCountry("IN")
//                .build();
//
//        autocompleteFragment.setFilter(typeFilter);


        PlaceAutocompleteFragment autocompleteFragmentFrom = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);

        autocompleteFragmentFrom.setHint("From");

        autocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                from = place.getLatLng();
                Log.d(TAG, "onPlaceSelected: From " + from);
                if(from_marker != null){
                    from_marker.remove();
                }
                from_marker = mMap.addMarker(new MarkerOptions().position(from).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(from, 10)); // to zoom in to the map of radius = 10km.

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment autocompleteFragmentTo = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_to);

        autocompleteFragmentTo.setHint("To");

        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                to = place.getLatLng();
                Log.d(TAG, "onPlaceSelected: To " + to);
                if(to_marker != null){
                    to_marker.remove();
                }
                to_marker = mMap.addMarker((new MarkerOptions().position(to).title(place.getName().toString())));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(to));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to, 10)); // to zoom in to the map of radius = 10km.

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Delhi and move the camera
        LatLng delhi = new LatLng(28.69, 77.14);
        mMap.addMarker(new MarkerOptions().position(delhi).title("Marker in New Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi, 10)); // to zoom in to the map of radius = 10km.

        if(from != null && to != null) {

            mMap.addPolyline(new PolylineOptions()
                    .add(from)
                    .add(to)
            );
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
