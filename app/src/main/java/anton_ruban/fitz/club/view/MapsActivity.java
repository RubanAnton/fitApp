package anton_ruban.fitz.club.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import anton_ruban.fitz.R;
import anton_ruban.fitz.main.adapter.ClubCardAdapter;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.network.ServerApi;
import anton_ruban.fitz.network.ServerUtils;
import anton_ruban.fitz.network.res.SubscriptionRequestMain;
import anton_ruban.fitz.utils.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener {


    private List<SubscriptionRequestMain> resList;
    private GoogleMap mMap;
    private Location lastLocation;
    private ServerApi serverApi;
    private PreferenceManager preferenceManager;
    private boolean state;
    String[] array = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        resList = new ArrayList<>();
        state = getIntent().getBooleanExtra("mapStateFind", false);
        serverApi = ServerUtils.serverApi();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!state) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.longClickMap)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }
        geocoder = new Geocoder(this,Locale.getDefault());
    }


    public int requestPerm() {
        if (ContextCompat.checkSelfPermission(this, array[0])
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return 1;
        } else {
            ActivityCompat.requestPermissions(this, array, 1000);
            return 2;
        }
    }
    @Override
    public void onLocationChanged(final Location location) {

        Log.i("called", "onLocationChanged");

        //when the location changes, update the map by zooming to the location
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
        mMap.moveCamera(center);
        lastLocation = location;
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);

        if (state){
            serverApi.getClubList(preferenceManager.getUserToken(),preferenceManager.getUserId()).enqueue(new Callback<ArrayList<SubscriptionRequestMain>>() {
                @Override
                public void onResponse(Call<ArrayList<SubscriptionRequestMain>> call, Response<ArrayList<SubscriptionRequestMain>> response) {
                    if(response.isSuccessful()){
                        resList.clear();
                        resList = response.body();
                        mMap.clear();
                        for (SubscriptionRequestMain res : resList) {
                            mMap.addMarker(new MarkerOptions()
                                    .position( new LatLng(res.getClubRes().lat, res.getClubRes().lng)))
                                    .setTitle(res.getClubRes().nameClub + " " + res.getClubRes().addres);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SubscriptionRequestMain>> call, Throwable t) {

                    Toast.makeText(MapsActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (permissions.length == 1 &&
                    permissions[0] == array[0] &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        requestPerm();
        mMap.getMyLocation();
        mMap.setOnMapLongClickListener(this);
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        if (!state) {
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                String message = getString(R.string.selectedAddr) + " " + address;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MapsActivity.this, CreateClubActivity.class);
                                intent.putExtra("longitude", latLng.longitude);
                                intent.putExtra("latitude", latLng.latitude);
                                intent.putExtra("addr", address);
                                setResult(1007, intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    void buildWay( Double lat,  Double lon){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat.toString()+"," +lon.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage( "com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public List<SubscriptionRequestMain> isNear(List<SubscriptionRequestMain> list2){
        List<SubscriptionRequestMain> list = new ArrayList<>();
        for (SubscriptionRequestMain res: list2) {
            Location targetLocation = new Location("");
            targetLocation.setLatitude(res.getClubRes().lat);
            targetLocation.setLongitude(res.getClubRes().lng);
          float dist =  lastLocation.distanceTo(targetLocation);
          if (dist < 3000) {
              list.add(res);
          }
        }
        return list;
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.chooseAction))
                .setPositiveButton(R.string.buildWay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       buildWay(marker.getPosition().latitude, marker.getPosition().longitude);
                    }
                });
        builder.show();
    }
}
