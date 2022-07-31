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

        //pushNotification("push-Title", "push-Body")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("My_Log", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("My_Log", "Token: $token")
        })
    }

    private fun pushNotification(title: String, body: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val contentIntent = PendingIntent.getActivity(
            this,
            1,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
            setContentTitle(title)
            setContentText(body)
            setSmallIcon(R.drawable.ic_kotlin_logo)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(contentIntent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelHigh = NotificationChannel(
                CHANNEL_HIGH_ID,
                CHANNEL_HIGH_ID,
                NotificationManager.IMPORTANCE_HIGH
            )
            channelHigh.description = "Канал для важных push-уведомлений"
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID1, notification.build())
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