package geekbrains.android.myweatherkotlin.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {

        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        Thread {
            var myConnection: HttpURLConnection? = null
            myConnection = uri.openConnection() as HttpURLConnection
            try {
                myConnection.readTimeout = 5000
                myConnection.addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO :: class.java)
                block(weatherDTO)
            } catch (e : Exception) {
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}