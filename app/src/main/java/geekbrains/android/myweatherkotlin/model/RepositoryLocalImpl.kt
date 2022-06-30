package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather

class RepositoryLocalImpl: Repository {
    override fun getListWeather(): List<Weather> {
        return listOf(Weather())
    }

    override fun getWeather(latitude: Double, longitude: Double): Weather {
        return Weather()
    }
}