package geekbrains.android.myweatherkotlin.view.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DetailsServiceIntent : IntentService("") {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {

        intent?.let {
            it.getParcelableExtra<City>(BUNDLE_CITY_KEY)?.let {

                try {
                    val uri =
                        URL("https://api.weather.yandex.ru/v2/informers?lat=${it.latitude}&lon=${it.longitude}")
                    Thread {
                        var myConnection: HttpURLConnection? = null
                        myConnection = uri.openConnection() as HttpURLConnection
                        try {
                            myConnection.readTimeout = 5000
                            myConnection.addRequestProperty(
                                YANDEX_API_KEY,
                                BuildConfig.WEATHER_API_KEY
                            )
                            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                            val weatherDTO =
                                Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                                putExtra(BUNDLE_WEATHER_DTO_KEY, weatherDTO)
                                action = WAVE
                            })
                        } catch (e: MalformedURLException) {
                            Log.d("My_Log", "Поймали MalformedURLException")
                            //Никакой особой обработки кроме как вывести в логи или Toast-ы я не придумал
                            //По хорошему, наверное, стоит куда-то записывать какая ошибка когда и где произошла и куда-то эту информацию передавать
                        } catch (e: IOException) {
                            Log.d("My_Log", "Поймали IOException")
                        } catch (e: JsonSyntaxException) {
                            Log.d("My_Log", "Поймали JsonSyntaxException")
                        } finally {
                            myConnection.disconnect()
                        }
                    }.start()
                } catch (e: MalformedURLException) {
                    Log.d("My_Log", "Поймали MalformedURLException")
                }
            }
        }
    }
}