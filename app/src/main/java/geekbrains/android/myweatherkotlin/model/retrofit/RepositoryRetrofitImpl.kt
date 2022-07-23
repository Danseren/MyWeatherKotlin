package geekbrains.android.myweatherkotlin.model.retrofit

import com.google.gson.GsonBuilder
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.model.RepositoryWeatherByCity
import geekbrains.android.myweatherkotlin.model.TopCallback
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.API_WEATHER_YANDEX
import geekbrains.android.myweatherkotlin.utils.bindDTOWithCity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepositoryRetrofitImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: TopCallback) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl(API_WEATHER_YANDEX)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        //api.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).execute()
        api.getWeather(BuildConfig.WEATHER_API_KEY, city.latitude, city.longitude)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(bindDTOWithCity(response.body()!!, city))
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