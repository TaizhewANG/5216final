package au.edu.canberra.mtfinalassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityTwo extends AppCompatActivity implements OnMapReadyCallback {
    private boolean norMode = true;
    private GoogleMap mMap;
    LatLng google = new LatLng(37.3981617,-122.1220645);
    LatLng ibm = new LatLng(41.1308344,-73.7315235);
    private String[] placeMarkersFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        setTitle("Google and IBM Headquarters");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.ActivityEight:

                return true;
                default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in USA and move the camera
        final LatLng usa = new LatLng(40.788077, -98.627419);
        final LatLng google = new LatLng(37.453772, -122.004079);
        final LatLng IBM = new LatLng(36.289269, -78.578078);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usa, 2));


        final Marker ucMarker1 = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_google_round))
                .position(google)
                .flat(true)

                .title("Google Headquarter")
                .snippet("Mountain View,California ,USA")

        );
        final Marker ucMarker2 = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ibm_round))
                .position(IBM)
                .flat(true)
                .title("IBM Headquarter")
                .snippet("Yorktown Height,New York,USA")
        );
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.infowindow_with_image,null);
                TextView title = (TextView) infoWindow.findViewById(R.id.title);
                TextView snippet = (TextView) infoWindow.findViewById(R.id.snippet);
                ImageView image = (ImageView) infoWindow.findViewById(R.id.info_image);

                if (marker.getId().equals(ucMarker1.getId())) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                    image.setImageDrawable(getResources()
                            .getDrawable(R.drawable.google, getTheme()));
                }
                if (marker.getId().equals(ucMarker2.getId())) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                    image.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ibm, getTheme()));
                }

                return infoWindow;

            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getId().equals(ucMarker1.getId())) {
                    Intent intent = new Intent(getApplicationContext(), ActivityThree.class);
                    intent.putExtra("Title","Google Firebase ML Clould Service");
                    intent.putExtra("id","google");
                    startActivity(intent);
                }
                if (marker.getId().equals(ucMarker2.getId())) {
                    Intent intent = new Intent(getApplicationContext(), ActivityThree.class);
                    intent.putExtra("Title","IBM Watson ML Clould Service");
                    intent.putExtra("id","ibm");
                    startActivity(intent);
                }

            }
        });
    }
    }
