package geekbrains.android.myweatherkotlin.view.view.details

import geekbrains.android.myweatherkotlin.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}