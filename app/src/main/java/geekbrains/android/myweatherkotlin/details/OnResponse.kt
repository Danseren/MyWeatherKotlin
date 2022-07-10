package geekbrains.android.myweatherkotlin.details

import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO

interface OnResponse {
    fun onResponse(weather: WeatherDTO)
}