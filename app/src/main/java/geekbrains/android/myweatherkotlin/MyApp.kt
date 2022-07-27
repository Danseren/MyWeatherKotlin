package geekbrains.android.myweatherkotlin

import android.app.Application
import androidx.room.Room
import geekbrains.android.myweatherkotlin.model.room.WeatherDatabse
import geekbrains.android.myweatherkotlin.utils.ROOM_DB_NAME_WEATHER

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

    companion object {
        private var myApp: MyApp? = null
        private var weatherDatabase: WeatherDatabse? = null
        fun getMyApp() = myApp!!

        fun getWeatherDatabase(): WeatherDatabse {

            if (weatherDatabase == null) {
                    weatherDatabase = Room.databaseBuilder(
                        getMyApp(),
                        WeatherDatabse::class.java,
                        ROOM_DB_NAME_WEATHER
                    ).build()
                }
          return weatherDatabase!!
        }
    }
}