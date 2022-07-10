package geekbrains.android.myweatherkotlin.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {

        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        var myConnection: HttpURLConnection? = null

        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.readTimeout = 5000
        myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
        Thread{
            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO :: class.java)
            block(weatherDTO)
        }.start()
    }
}