package geekbrains.android.myweatherkotlin.details

import geekbrains.android.myweatherkotlin.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}