package geekbrains.android.myweatherkotlin.viewmodel

import geekbrains.android.myweatherkotlin.domain.Weather

sealed class AppState {
    data class Success(val weatherData : Weather) : AppState()
    //data class SuccessList(val weatherData: WeatherList)
    data class Error(val error : Throwable) : AppState()
    object Loading : AppState()
}