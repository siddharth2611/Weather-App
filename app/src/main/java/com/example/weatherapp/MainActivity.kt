package com.example.weatherapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}"
    private var API = "fcd2c236893559071f53147e2c72132f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cname = findViewById<EditText>(R.id.cityname)
        val btn = findViewById<Button>(R.id.getweather)
        val humidityit = findViewById<TextView>(R.id.humidity)
        val tempMaxit = findViewById<TextView>(R.id.temp_max)
        val tempMinit = findViewById<TextView>(R.id.temp_min)
        val pressureit = findViewById<TextView>(R.id.pressure)
        val windDegit = findViewById<TextView>(R.id.wind)
        val weatherStatus = findViewById<TextView>(R.id.status)
        val sunsetit = findViewById<TextView>(R.id.sunset)
        val sunriseit = findViewById<TextView>(R.id.sunrise)
        val tempit = findViewById<TextView>(R.id.temp)

        btn.setOnClickListener {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)

            val retrofitData = retrofitBuilder.getWeatherByLocation(cname.toString().trim(), API)

            retrofitData.enqueue(object : Callback<WeatherData?> {
                override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>
                ) {
                    if (response.code() == 404) {
                        Toast.makeText(this@MainActivity, "Please Enter a valid City", Toast.LENGTH_LONG).show()
                        return
                    } else if (!response.isSuccessful) {
                        Toast.makeText(this@MainActivity, response.code().toString(), Toast.LENGTH_LONG).show()
                        return
                    }
                    val responseBody = response.body()
                    val mainProperties = responseBody?.main!!
                    val weatherProperties = responseBody.weather
                    val windProperties = responseBody.wind
                    val systemProperties = responseBody.sys

                    val temp: Double = mainProperties.temp
                    val temperature = (temp - 273.15).toInt()
                    tempit.text = temperature.toString()+"°C"

                    val humidity: Int = mainProperties.humidity
                    humidityit.text = humidity.toString() + "%"

                    val tempMax = mainProperties.temp_max
                    val temperatureMax = (tempMax - 273.15).toInt()
                    tempMaxit.text = temperatureMax.toString() + "°C"

                    val tempMin = mainProperties.temp_min
                    val temperatureMin = (tempMin - 273.15).toInt()
                    tempMinit.text = temperatureMin.toString() + "°C"

                    val pressure = mainProperties.pressure
                    pressureit.text = pressure.toString() + " Pa"

                    val windDeg = windProperties.deg
                    windDegit.text = windDeg.toString() + "°"

                    val collectDataInSB = StringBuilder()

                    weatherStatus.text = collectDataInSB

                    val sunset = systemProperties.sunset.toLong()
                    sunsetit.text =
                        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))

                    val sunrise = systemProperties.sunrise.toLong()
                    sunriseit.text =
                        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                }

                override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

}
