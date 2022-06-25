package geekbrains.android.myweatherkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv_start:TextView = findViewById<TextView>(R.id.tv_start)

        findViewById<Button>(R.id.btn_go).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.d("My_Log", "It's alive!")
                val first_student = OneOfUs("Andrei", 1984)
                val second_student = first_student.copy()
                //tv_start.setText("First student: " + first_student.toString() + "\nSecond student (копия): " + second_student.toString()) Интересно студия предлагает вот это все заменить. Непривычно
                tv_start.setText("First student: $first_student\nSecond student (копия): $second_student")

            }

        })
    }


}

/*
Урок 1
    Создать новый проект в Android Studio без поддержки Kotlin.
    Сконфигурировать Kotlin в новом проекте (лучше вручную).
    Перевести MainActivity на Kotlin.
    Добавить кнопку в разметку и повесить на неё clickListener в Activity.
Потренироваться в создании классов и функций, описанных в уроке, и убедиться, что всё работает. Например, создать тестовое приложение:
    Сформировать data class с двумя свойствами и вывести их на экран приложения.
    Создать объект класса из предыдущего пункта, в этом бъекте вызвать copy и вывести значения скопированного класса на экран (Создать Object. В Object вызвать copy и вывести значения скопированного класса на экран).
Вывести значения из разных циклов в консоль, используя примеры из методических материалов.
Изучить API погоды «Яндекса», посмотреть примеры и зарегистрироваться в качестве разработчика, получить свой ключ разработчика.
Изучить API КиноПоиска и зарегистрироваться в качестве разработчика, подключиться к API.
Определиться с экранами и инструментарием своего будущего приложения с фильмами на основе возможностей API.
Переведите проект с заметками на Kotlin. Курс «Базовый уровень».
 */