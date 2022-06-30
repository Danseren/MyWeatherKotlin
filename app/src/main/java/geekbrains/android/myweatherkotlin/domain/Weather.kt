package geekbrains.android.myweatherkotlin.domain

data class Weather (
    val city: City = getDefaultCity(),
    val temperature: Int = 23,
    val feelsLike: Int = 24
)

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)