package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.domain.getEuropeCities
import geekbrains.android.myweatherkotlin.domain.getRussianCities

class RepositoryCitiesListImpl : RepositoryCitiesList {
    override fun getListCities(location: CityLocation): List<Weather> {
        return when (location) {
            CityLocation.Russia -> {
                getRussianCities()
            }
            CityLocation.Europe -> {
                getEuropeCities()
            }
        }
    }

}