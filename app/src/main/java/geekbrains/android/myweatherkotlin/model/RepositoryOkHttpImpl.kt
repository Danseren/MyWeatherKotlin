package geekbrains.android.myweatherkotlin.model

import android.util.Log
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.WEATHER_URL_YANDEX
import geekbrains.android.myweatherkotlin.utils.YANDEX_API_KEY
import geekbrains.android.myweatherkotlin.utils.bindDTOWithCity
import okhttp3.*
import java.io.IOException

class RepositoryOkHttpImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: TopCallback) {

        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$WEATHER_URL_YANDEX${city.latitude}&lon=${city.longitude}")
        val request: Request = builder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("My_Log", "В момент запроса что-то пошло не так")
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code in 200..299 && response.body != null) {
                    response.body?.let {
                        val responseString = it.string()
                        val weatherDTO = Gson().fromJson(responseString, WeatherDTO::class.java)
                        callback.onResponse(bindDTOWithCity(weatherDTO, city))
                    }
                } else {
                    Log.d("My_Log", "Что-то пошло не так...")
                    callback.onFailure(IOException("Ошибка для ленивых"))
                }
            }
        })
    }
}