package com.example.ergasia1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import com.example.ergasia1.Event;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    Button button;
    double previousSpeed = 0;
    FirebaseDatabase database;
    TextView textView2;
    static final int REQ_LOC_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
        button.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOC_CODE);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
                textView2.setVisibility(View.VISIBLE); // to kokkino keimeno pou deixnei oti katagrafetai h taxythta
            }
        });

        database = FirebaseDatabase.getInstance("https://ergasia1-e2bb6-default-rtdb.europe-west1.firebasedatabase.app");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOC_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
            textView2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double speed = location.getSpeed()*3.6;

        DatabaseReference reference;
        // o periorismos "speed > 15" einai gia na ksexwrisoume an einai anthrwpos pou trexei
        // kai to "previouSpeed > 0" einai wste ama einai akinhto to amaksi na mhn katagrafei ws epitaxynsh
        if (previousSpeed > 0 && speed > 15 ){
            double difference = speed - previousSpeed;

            if (difference < -20 ){ // an einai mikrotero tou 0 einai frenarisma
                Date time = new Date(location.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                String strDate = dateFormat.format(time);

                // Ftiaxnw ena object ths klasis Event pou eftiaksa kai meta apothikevw to object sthn firebase
                Event slowup = new Event(false, difference, previousSpeed, strDate, location.getLatitude(), location.getLongitude());
                reference = database.getReference("eventList");
                reference.push().setValue(slowup);

                // Toast gia na deiksoume oti katagrafike ena event frenarismatos
                Toast.makeText(this, "Katagrafhke Frenarisma", Toast.LENGTH_SHORT).show();
            } else if (difference > 20) { // an einai megalytero tou 0 einai epitaxynsh
                Date time = new Date(location.getTime());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                String strDate = dateFormat.format(time);

                // Ftiaxnw ena object ths klasis Event pou eftiaksa kai meta apothikevw to object sthn firebase
                Event speedup = new Event(true, difference, previousSpeed, strDate, location.getLatitude(), location.getLongitude());
                reference = database.getReference("eventList");
                reference.push().setValue(speedup);

                // Toast gia na deiksoume oti katagrafike ena event epitaxynshs
                Toast.makeText(this, "Katagrafhke Epitaxynsh", Toast.LENGTH_SHORT).show();
            }
        }

        previousSpeed = speed; // sto telos thewtw thn twrinh taxythta ish me thn prohgoymneh
    }

    public void stopRecording(View view){
        locationManager.removeUpdates(MainActivity.this);
        textView2.setVisibility(View.INVISIBLE);
    }

    public void showMap(View view){
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}