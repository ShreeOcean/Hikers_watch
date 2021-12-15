package ocean.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Log.i("Location", location.toString());
                updateLoactionInfo(location);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastlocation != null){

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    public void updateLoactionInfo(Location location){
        //Log.i("Loction : -----", location.toString());
        TextView latTextView = findViewById(R.id.tvlatitude);
        TextView longTextView = findViewById(R.id.tvlongitude);
        TextView accTextView = findViewById(R.id.tvAccurecy);
        TextView altTextView = findViewById(R.id.tvAltitude);
        TextView addressTextView = findViewById(R.id.tvAddress);/*can not be access directly through location class object*/

        latTextView.setText("Latitude : " + Double.toString(location.getLatitude()));
        longTextView.setText("Longitude : " + Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy : " + Double.toString(location.getAccuracy()) );
        altTextView.setText("Altitude : " + Double.toString(location.getAltitude()));

        String address = "Could not find address :( ";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault()); /*Geocoder is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate*/

        try {
            List<Address> listaddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listaddress != null && listaddress.size() > 0){

                address = "Address : \n";

                if (listaddress.get(0).getThoroughfare() != null){
                    address += listaddress.get(0).getThoroughfare() + "\n";
                }
                if (listaddress.get(0).getLocality() != null){
                    address += listaddress.get(0).getLocality() + "\n";
                }
                if (listaddress.get(0).getPostalCode() != null){
                    address += listaddress.get(0).getPostalCode() + "\n";
                }
                if (listaddress.get(0).getAdminArea() != null){
                    address += listaddress.get(0).getAdminArea() + "\n";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        addressTextView.setText(address);
    }
}