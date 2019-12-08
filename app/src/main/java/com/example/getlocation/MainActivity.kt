package com.example.getlocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var longitude:String = ""
    var latitude:String = ""
    var mAzimuth:Double = 0.0
    var sendBoolean:Boolean = false
    lateinit var ip:String
    lateinit var port:String

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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
                longitude = location?.longitude.toString()
                latitude = location?.latitude.toString()

                longitudeView.text = longitude
                latitudeView.text = latitude
                if(sendBoolean) {
                    val json = JSONObject()
                    json.put("longitude",longitude)
                    json.put("latitude",latitude)
                    val message = json.toString()
                    MessageSender().execute(ip,port,message)
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }
        }

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)


        val rvListener = object : SensorEventListener {
            val orientation = FloatArray(3)
            val rMat = FloatArray(9)
            override fun onSensorChanged(event: SensorEvent?){
                if (event!!.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rMat, event.values)
                    mAzimuth = (Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0].toDouble()) + 360) % 360
                }
                directionView.text= mAzimuth.toString()
                if(sendBoolean) {
                    val json = JSONObject()
                    json.put("Azimuth",mAzimuth)
                    val message = json.toString()
                    MessageSender().execute(ip,port,message)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,0f,locationListener)
        sensorManager.registerListener(
            rvListener,
            rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST
        )


        sendDataBtn.setOnClickListener {
            ip = ipEditText.text.toString()
            port = portEditText.text.toString()
            if(ip.isEmpty() || port.isEmpty()){
                Toast.makeText(applicationContext,"Please fill in the blank",Toast.LENGTH_LONG).show()
            }else{
                sendMessage()
            }
        }

    }
    fun sendMessage() {
        sendBoolean = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
             0-> {
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
