package geekbrains.android.myweatherkotlin.view.view.details

import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO

interface OnResponse {
    fun onResponse(weather: WeatherDTO)
}