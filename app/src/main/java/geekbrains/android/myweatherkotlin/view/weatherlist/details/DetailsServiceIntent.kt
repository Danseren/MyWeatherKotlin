package geekbrains.android.myweatherkotlin.view.weatherlist.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsServiceIntent : IntentService("") {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {

        intent?.let {
            it.getParcelableExtra<City>(BUNDLE_CITY_KEY)?.let {

                val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${it.latitude}&lon=${it.longitude}")

                Thread {
                    var myConnection: HttpURLConnection? = null
                    myConnection = uri.openConnection() as HttpURLConnection
                    try {
                        myConnection.readTimeout = 5000
                        myConnection.addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                        val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                        val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO :: class.java)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                            putExtra(BUNDLE_WEATHER_DTO_KEY, weatherDTO)
                            action = WAVE
                        })
                    } catch (e : Exception) {
                    } finally {
                        myConnection.disconnect()
                    }
                }.start()
            }
        }
    }
}