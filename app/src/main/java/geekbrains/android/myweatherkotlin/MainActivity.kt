package geekbrains.android.myweatherkotlin

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import geekbrains.android.myweatherkotlin.databinding.ActivityMainBinding
import geekbrains.android.myweatherkotlin.view.view.contentprovider.ContentProviderFragment
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

        val rows = MyApp.getWeatherDatabase().weatherDao().getWeatherAll()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_history -> {

                val theOnlyOneHistoryFragment = supportFragmentManager.findFragmentByTag("history tag")

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

                val theOnlyOneProviderFragment = supportFragmentManager.findFragmentByTag("provider tag")

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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}