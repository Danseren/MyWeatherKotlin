package geekbrains.android.myweatherkotlin.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.WEATHER_URL_YANDEX
import geekbrains.android.myweatherkotlin.utils.YANDEX_API_KEY
import geekbrains.android.myweatherkotlin.utils.bindDTOWithCity
import geekbrains.android.myweatherkotlin.utils.getLines
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RepositoryWeatherLoaderImpl : RepositoryWeatherByCity {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeather(city: City, callback: TopCallback) {
        Thread {
            val uri = URL("$WEATHER_URL_YANDEX${city.latitude}&lon=${city.longitude}")
            var myConnection: HttpURLConnection? = null
            myConnection = uri.openConnection() as HttpURLConnection
            myConnection.addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            try {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(bindDTOWithCity(weatherDTO, city))
            } catch (e: IOException) {
                callback.onFailure(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}