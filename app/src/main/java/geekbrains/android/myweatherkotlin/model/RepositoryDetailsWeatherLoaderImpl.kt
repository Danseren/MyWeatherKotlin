package geekbrains.android.myweatherkotlin.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.YANDEX_API_KEY
import geekbrains.android.myweatherkotlin.utils.convertDtoToModel
import geekbrains.android.myweatherkotlin.utils.getLines
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RepositoryDetailsWeatherLoaderImpl : RepositoryLocationToWeather {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeather(lat: Double, lon: Double, callback: TopCallback) {
        Thread {
            val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            var myConnection: HttpURLConnection? = null
            myConnection = uri.openConnection() as HttpURLConnection
            myConnection.addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            try {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(convertDtoToModel(weatherDTO))
            } catch (e: IOException) {
                callback.onFailure(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}