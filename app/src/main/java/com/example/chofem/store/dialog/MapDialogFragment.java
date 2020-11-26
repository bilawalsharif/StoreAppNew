package com.example.chofem.store.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.chofem.store.R;
import com.example.chofem.store.responses.LoginResponse;
import com.example.chofem.store.utils.CustomProgressDialog;
import com.example.chofem.store.utils.SharedPref;
import com.example.chofem.store.volley.URLs;
import com.example.chofem.store.volley.VolleySingleton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback{
    //private View pic;
    View view;
    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Button btnSaveLocation;
    private  Dialog builder;
    private LatLng latLong;
    private EditText etdescription;
    private Context mContext;

    private PlaceAutocompleteFragment placeAutoComplete;
    public MapDialogFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog_map, new LinearLayout(getActivity()), false);

        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }


        // Build dialog
        widgets();
       // SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete);
/*
        placeAutoComplete = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete);

       placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.d("Maps", "Place selected: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });*/

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return builder;
    }

    private void widgets() {
        try {
            builder = new Dialog(getActivity());
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            builder.setContentView(view);
            etdescription=view.findViewById(R.id.etdescription);
            btnSaveLocation=view.findViewById(R.id.btnSaveLocation);
            builder.setCancelable(true);
            btnSaveLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (latLong!=null)
                    {
                        addLocation(v);
                    }
                    else {
                        builder.dismiss();
                        builder.cancel();
                    }
                }
            });
        }catch (Exception ex)
        {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
          @Override
          public void onMapClick(LatLng latLng) {
              mMap.clear();
              latLong=latLng;

              MarkerOptions marker = new MarkerOptions();
              getAddress(latLong.latitude,latLng.longitude);
              marker.position(new LatLng(latLng.latitude, latLng.longitude));
              marker.title("Current  position");
              mMap.addMarker(marker);
              mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,  latLng.longitude), 11));
          }
      });
    }
    public void addLocation(final View v)
    {
        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(getActivity(), "Updating your Location ...");
        progressDialog.show();
        LoginResponse loginResponse = SharedPref.getInstance(getActivity()).getData();


        @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("lat", String.valueOf(latLong.latitude));
        jsonParams.put("lng",  String.valueOf(latLong.longitude));
        jsonParams.put("description",etdescription.getText().toString());
        jsonParams.put("email", loginResponse.getEMAIL());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URLs.URL_ADD_LOCATION, new JSONObject(jsonParams),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            builder.dismiss();
                            builder.cancel();
                            progressDialog.dismiss();
                            if (response.getString("MESSAGE").equals("LOCATION ADDED SUCCESSFULLY"))
                            {
                               // Snackbar.make(v,"Location Updated Success",Snackbar.LENGTH_SHORT).show();
                                Toast.makeText(mContext, "Location Updated Success", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                            else {
                                progressDialog.dismiss();
                               // Snackbar.make(v,"Server error",Snackbar.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //   mResponseListener.requestCompleted(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            // mResponseListener.requestEndedWithError(error);
                        }
                    }
                });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    public void getAddress(double latitude,double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); //
            etdescription.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
