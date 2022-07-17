package geekbrains.android.myweatherkotlin.viewmodel.details

import geekbrains.android.myweatherkotlin.domain.Weather

sealed class DetailsFragmentAppState {
    data class Success(val weatherData : Weather) : DetailsFragmentAppState()
    data class Error(val error : Throwable) : DetailsFragmentAppState()
    object Loading : DetailsFragmentAppState()
}