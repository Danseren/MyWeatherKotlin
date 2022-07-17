package geekbrains.android.myweatherkotlin.viewmodel.details

import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO

sealed class DetailsFragmentAppState {
    data class Success(val weatherData: WeatherDTO) : DetailsFragmentAppState()
    data class Error(val error: Throwable) : DetailsFragmentAppState()
    object Loading : DetailsFragmentAppState()
}