package geekbrains.android.myweatherkotlin.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City,
    var temperature: Int = 23,
    var feelsLike: Int = 24,
    var icon: String = "ovc_-ra"
) : Parcelable

@Parcelize
data class City(
    var name: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable

fun getEuropeCities(): List<Weather> {
    return listOf(
        Weather(City("Лондон", 51.5085300, -0.1257400), 1, 2),
        Weather(City("Париж", 48.8534100, 2.3488000), 5, 6),
        Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8),
        Weather(City("Рим", 41.9027835, 12.496365500000024), 9, 10),
        Weather(City("Киев", 50.4501, 30.523400000000038), 17, 18),
        Weather(City("Вена", 48.1300, 16.2224), 14, 16),
        Weather(City("Хельсинки", 60.1024, 24.5655), 14, 16),
        Weather(City("Варшава", 52.13, 21.02), 16, 18),
        Weather(City("Будапешт", 47.31, 19.05), 19, 16),
        Weather(City("Лиссабон", 38.43, 9.10), 23, 26),
    )
}

fun getRussianCities(): List<Weather> {
    return listOf(
        Weather(City("Москва", 55.755826, 37.617299900000035), 1, 2),
        Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), 3, 3),
        Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), 5, 6),
        Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 7, 8),
        Weather(City("Нижний Новгород", 56.2965039, 43.936059), 9, 10),
        Weather(City("Казань", 55.8304307, 49.06608060000008), 11, 12),
        Weather(City("Челябинск", 55.1644419, 61.4368432), 13, 14),
        Weather(City("Омск", 54.9884804, 73.32423610000001), 15, 16),
        Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), 17, 18),
        Weather(City("Уфа", 54.7387621, 55.972055400000045), 19, 20)
    )
}

//fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)