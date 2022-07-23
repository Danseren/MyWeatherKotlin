package geekbrains.android.myweatherkotlin.utils

import android.os.Build
import androidx.annotation.RequiresApi
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.dto.Fact
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import java.io.BufferedReader
import java.util.stream.Collectors

class Utils {
}

@RequiresApi(Build.VERSION_CODES.N)
fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}

fun bindDTOWithCity(weatherDTO: WeatherDTO, city: City): Weather {
    val fact: Fact = weatherDTO.fact
    return (Weather(city, fact.temp, fact.feelsLike))
}

fun convertModelToDto(weather: Weather): WeatherDTO {
    val fact: Fact = Fact(weather.feelsLike, weather.temperature)
    return WeatherDTO(fact)
}