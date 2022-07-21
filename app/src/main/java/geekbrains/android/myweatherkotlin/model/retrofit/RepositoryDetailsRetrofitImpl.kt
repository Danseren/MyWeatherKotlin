package geekbrains.android.myweatherkotlin.model.retrofit

import com.google.gson.GsonBuilder
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.RepositoryLocationToWeather
import geekbrains.android.myweatherkotlin.model.TopCallback
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.bindDTOWithCity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryDetailsRetrofitImpl : RepositoryLocationToWeather {
    override fun getWeather(weather: Weather, callback: TopCallback) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl("https://api.weather.yandex.ru")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        //api.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).execute()
        api.getWeather(BuildConfig.WEATHER_API_KEY, weather.city.latitude, weather.city.longitude)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(bindDTOWithCity(response.body()!!, weather.city))
                    } else {
                        callback.onFailure(IOException("Ошибка для ленивых"))
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callback.onFailure(t as IOException)    //костыль
                }
            })
    }
}