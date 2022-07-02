package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.domain.getEuropeCities
import geekbrains.android.myweatherkotlin.domain.getRussianCities

class RepositoryLocalImpl: RepositorySingle, RepositoryMulti {
    override fun getListWeather(location: CityLocation): List<Weather> {
        return when (location) {
            CityLocation.Russia -> {
                getRussianCities()
            }
            CityLocation.Europe -> {
                getEuropeCities()
            }
        }
    }

    override fun getWeather(latitude: Double, longitude: Double): Weather {
        return Weather()
    }
}