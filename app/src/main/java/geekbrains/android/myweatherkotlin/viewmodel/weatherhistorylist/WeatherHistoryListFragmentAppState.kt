package geekbrains.android.myweatherkotlin.viewmodel.weatherhistorylist

import geekbrains.android.myweatherkotlin.domain.Weather

sealed class WeatherHistoryListFragmentAppState {
    data class SuccessMulti(val weatherList: List<Weather>) : WeatherHistoryListFragmentAppState()
    data class Error(val error: Throwable) : WeatherHistoryListFragmentAppState()
    object Loading : WeatherHistoryListFragmentAppState()
}