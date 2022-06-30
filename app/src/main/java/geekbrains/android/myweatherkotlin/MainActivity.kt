package geekbrains.android.myweatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import geekbrains.android.myweatherkotlin.databinding.ActivityMainBinding
import geekbrains.android.myweatherkotlin.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.btnGo.text = "Is it a success?"

        //Напоминание для себя
        //val tvStart:TextView = findViewById<TextView>(R.id.tv_start) Исправил tv_start  на tvStart
        //tvStart.text = ("First student: $first_student\nSecond student (копия): $second_student") Исправил tvStart.setText() на tvStart.text = ()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance())
                .commit()
        }
    }

}
/*
Воспользуйтесь ViewBinding в своём проекте.
Добавьте в приложение рандомизатор, который будет возвращать разный результат загрузки данных: успех или ошибку. Обработайте ошибки загрузки.
 */