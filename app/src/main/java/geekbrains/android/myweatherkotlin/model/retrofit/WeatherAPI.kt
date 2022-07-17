package geekbrains.android.myweatherkotlin.model.retrofit

import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.YANDEX_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v2/informers")
    fun getWeather(
        @Header(YANDEX_API_KEY) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}