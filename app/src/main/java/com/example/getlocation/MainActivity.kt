package com.example.getlocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var longitude:Double? = 0.0
    var latitude:Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object: LocationListener {
            override fun onLocationChanged(location: Location?) {
                longitude = location?.longitude
                latitude = location?.latitude

                longitudeView.text = longitude.toString()
                latitudeView.text = latitude.toString()


                val json = JSONObject()
                json.put("longitude",longitude)
                json.put("latitude",latitude)
                val message = json.toString()


                val ip = ipEditText.text.toString()
                val messageSender = MessageSender(this@MainActivity)
                messageSender.execute(ip,message)


            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }
        }
        getCoordinateBtn.setOnClickListener {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,0f,locationListener)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
             0-> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this,"Granted",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,"Not Granted",Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                Toast.makeText(this,"Already Granted",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
