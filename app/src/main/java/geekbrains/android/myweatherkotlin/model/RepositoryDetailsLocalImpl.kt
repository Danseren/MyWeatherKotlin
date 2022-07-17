package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.domain.getDefaultCity
import geekbrains.android.myweatherkotlin.domain.getEuropeCities
import geekbrains.android.myweatherkotlin.domain.getRussianCities
import geekbrains.android.myweatherkotlin.model.dto.Fact
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO

class RepositoryDetailsLocalImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double, callback: TopCallback) {
        val list = getEuropeCities().toMutableList()
        list.addAll(getRussianCities())
        val response = list.filter { it.city.latitude == lat && it.city.longitude == lon }
        callback.onResponse(convertModelToDto(response.first()))
    }

    private fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
        val fact: Fact = weatherDTO.fact
        return (Weather(getDefaultCity(), fact.temp, fact.feelsLike))
    }

    private fun convertModelToDto(weather: Weather): WeatherDTO {
        val fact: Fact = Fact(weather.feelsLike, weather.temperature)
        return WeatherDTO(fact)
    }
}