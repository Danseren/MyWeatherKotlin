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
                checkIf()
                checWhen()
                cycles()
            }

        })
    }

    fun checkIf() {
        val masIf = 1..1000
        val numIf = 100
        if (numIf in masIf) {
            Log.d("My_Log", "True")
        } else {
            Log.d("My_Log", "False")
        }
    }

    fun checWhen() {
        val numWhen = 42
        val result = when (numWhen) {
            1 -> Log.d("My_Log", "Ты избранный!")
            3 -> Log.d("My_Log", "Один за всех и все за одного!")
            12 -> Log.d("My_Log", "Лед тронулся, господа присяжные заседатели, лед тронулся!")
            42 -> Log.d("My_Log", "Вот и ответ на Основной вопрос жизни, Вселенной и всего остального")
            451 -> Log.d("My_Log", "Похоже, что бумага притакой температуре по Фаренгейту не воспламеняется")
            1984 -> Log.d("My_Log", "Большой Брат следит за тобой!")
            else -> {
                Log.d("My_Log", "Увы")
            }
        }
    }

    fun cycles() {
        val colorMemories = listOf("Каждый", "Охотник", "Желает", "Знать", "Где", "Сидит", "Фазан")

        Log.d("My_Log", "*** Цикл For each ***")
        colorMemories.forEach() {
            Log.d("My_Log", "$it")
        }

        Log.d("My_Log", "*** Repeat ***")
        repeat(colorMemories.size) {
            Log.d("My_Log", colorMemories[it])
        }

        Log.d("My_Log", "*** For №1 ***")
        for (i in colorMemories.indices) {
            Log.d("My_Log", colorMemories[i])
        }

        Log.d("My_Log", "*** For №2 ***")
        for (i in colorMemories) {
            Log.d("My_Log", i)
        }

        Log.d("My_Log", "*** For №3 ***")
        for (i in 0..colorMemories.size-1) {
            Log.d("My_Log", colorMemories[i])
        }

        Log.d("My_Log", "*** For №4 ***")
        for (i in 0 until colorMemories.size) {
            Log.d("My_Log", colorMemories[i])
        }

        Log.d("My_Log", "*** For revers ***")
        for (i in colorMemories.size-1 downTo 0) {
            Log.d("My_Log", colorMemories[i])
        }

        Log.d("My_Log", "*** For with step ***")
        for (i in 0..colorMemories.size-1 step 2) {
            Log.d("My_Log", colorMemories[i])
        }

        Log.d("My_Log", "*** While ***")
        var count = colorMemories.size
        while (--count > 0) {
            Log.d("My_Log", colorMemories[count])
        }

        Log.d("My_Log", "*** Do While ***")
        do {
            Log.d("My_Log", colorMemories[count])
        } while (++count < 7)
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