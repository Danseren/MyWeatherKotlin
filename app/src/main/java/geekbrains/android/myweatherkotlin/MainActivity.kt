package geekbrains.android.myweatherkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbrains.android.myweatherkotlin.databinding.ActivityMainBinding
import geekbrains.android.myweatherkotlin.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Напоминание для себя
        //Ctrl + Alt + O
        //Ctrl + Alt + L
        //trl + Shift + U
        //val tvStart:TextView = findViewById<TextView>(R.id.tv_start) Исправил tv_start  на tvStart
        //tvStart.text = ("First student: $first_student\nSecond student (копия): $second_student") Исправил tvStart.setText() на tvStart.text = ()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }
    }

}