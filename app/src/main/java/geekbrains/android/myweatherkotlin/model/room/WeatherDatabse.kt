package geekbrains.android.myweatherkotlin.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(WeatherEntity::class), version = 1)
abstract class WeatherDatabse: RoomDatabase() {
    abstract fun weatherDao(): WeatherDAO
}