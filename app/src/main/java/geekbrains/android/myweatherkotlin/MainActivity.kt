package geekbrains.android.myweatherkotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import geekbrains.android.myweatherkotlin.databinding.ActivityMainBinding
import geekbrains.android.myweatherkotlin.utils.CHANNEL_HIGH_ID
import geekbrains.android.myweatherkotlin.utils.NOTIFICATION_ID1
import geekbrains.android.myweatherkotlin.view.view.contentprovider.ContentProviderFragment
import geekbrains.android.myweatherkotlin.view.view.maps.MapsFragment
import geekbrains.android.myweatherkotlin.view.view.room.WeatherHistoryListFragment
import geekbrains.android.myweatherkotlin.view.view.weatherlist.CitiesListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Напоминание для себя
        //Ctrl + Alt + O        Optimize imports
        //Ctrl + Alt + L        Reformat code
        //Ctrl + shift + U      upper/lower case
        //val tvStart:TextView = findViewById<TextView>(R.id.tv_start) Исправил tv_start  на tvStart
        //tvStart.text = ("First student: $first_student\nSecond student (копия): $second_student") Исправил tvStart.setText() на tvStart.text = ()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CitiesListFragment.newInstance())
                .commit()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("My_Log", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("My_Log", "Token: $token")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_history -> {

                val theOnlyOneHistoryFragment =
                    supportFragmentManager.findFragmentByTag("history tag")

                if (theOnlyOneHistoryFragment == null) {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(R.id.container, WeatherHistoryListFragment(), "history tag")
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
            R.id.menu_content_provider -> {

                val theOnlyOneProviderFragment =
                    supportFragmentManager.findFragmentByTag("provider tag")

                if (theOnlyOneProviderFragment == null) {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(R.id.container, ContentProviderFragment(), "provider tag")
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
            R.id.menu_google_maps -> {

                val theOnlyOneProviderFragment = supportFragmentManager.findFragmentByTag("map tag")

                if (theOnlyOneProviderFragment == null) {
                    supportFragmentManager.apply {
                        beginTransaction()
                            .replace(R.id.container, MapsFragment(), "map tag")
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}