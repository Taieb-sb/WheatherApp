package esme.fr.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String APP_ID = "38ff3a19b772f54791fd069454328815";
    String METEO_URL = "https://api.openweathermap.org/data/2.5/weather?";
            //"https://api.openweathermap.org/data/2.5/weather";

    final long TEMPS = 5000;
    final float DISTANCE = 1000;
    final int CODE_REQUIS = 101;

    String fournisseurLocalisation = LocationManager.GPS_PROVIDER;
    TextView NomVille, etatMeteo, Temperature;
    ImageView miconMeteo;
    RelativeLayout mRechercheVille;

    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etatMeteo = findViewById(R.id.conditionMeteo);
        Temperature = findViewById(R.id.temperature);
        miconMeteo = findViewById(R.id.iconMeteo);
        mRechercheVille = findViewById(R.id.rechercheVille);
        NomVille = findViewById(R.id.nomVille);

        NomVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, rechercheVille.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMeteoLocalisationActuel();
    }

    private void getMeteoLocalisationActuel() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                //params.put("lat", Latitude);
                //params.put("lon", Longitude);
                //params.put("appid", APP_ID);
                METEO_URL=METEO_URL+"lat="+String.valueOf(Latitude)+"&lon="+String.valueOf(Longitude)+"&appid="+APP_ID;
                //METEO_URL=METEO_URL+"lat=20&lon=30"+"&appid="+APP_ID;
                letsdosomenetworking(params);
                System.out.println("latitude : ");
                System.out.println(Latitude);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                //pas en mesure d'obtenir l'emplacement
            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},CODE_REQUIS);
            return;
        }
        mLocationManager.requestLocationUpdates(fournisseurLocalisation, TEMPS, DISTANCE, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CODE_REQUIS){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"localisation obtenu",Toast.LENGTH_SHORT).show();
                getMeteoLocalisationActuel();
            }
            else{
                //user n'a pas de permission
            }
        }
    }

    private void letsdosomenetworking(RequestParams params){
        System.out.println("Test"+METEO_URL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(METEO_URL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                Toast.makeText(MainActivity.this, "Donner obtenu", Toast.LENGTH_SHORT).show();
                DonneeMeteo dMeteo = DonneeMeteo.fromJson(response);
                updateUI(dMeteo);
                System.out.println(METEO_URL);

                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUI(DonneeMeteo meteo){
        Temperature.setText(meteo.getmTemperature());
        NomVille.setText(meteo.getmVille());
        etatMeteo.setText(meteo.getmTypeMeteo());
        int resourceID= getResources().getIdentifier(meteo.getmIcon(),"drawable",getPackageName());
        miconMeteo.setImageResource(resourceID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }

    }
}