package geekbrains.android.myweatherkotlin.lesson3

import android.util.Log
import android.view.ViewGroup

object Lesson3 : TestableKotlin {

    //Сделав изначально всё с val, потом подвисаешь на непонятной ошибке,
    // но потом поменяв на var понимаешь, что так же и сдругим программистом сработает, если он захочет  поменять что-нибудь
    //Надо приучаться так делать
    private var testJava: Test? = null
    private val testJava2: Test = Test()
    private var testKotlin: Test = Test()

//    init {
//        testKotlin = Test()
//    }

//    fun startLessonInterface() {
//        val callbackJava = TestableJava {  }
//    }

    fun startLessonGeneric() {
        val startList: List<*>
        startList = listOf<String>("Каждый ", "выбирает ", "для ", "себя")
        val phraseList = listOf<String>("Каждый ", "выбирает ", "для ", "себя")
        val numList = listOf<Int>(42, 3, 20000, 451, 1984, 100)
        val tesList = listOf<Test>(Test(), Test(), Test())

        someGeneric("")
        someGeneric(Test())
        someGeneric(1.0)

        var uniLink: Producer<Any>

        val producerTest = Producer<Test>()
        val producerString = Producer<String>()

        uniLink = producerTest
        uniLink = producerString
        uniLink.producer()
    }

    class Producer<out T> {
        private val list: List<T> = listOf<T>()
        fun producer(): List<T> {
            return list
        }
    }

    class Consume<in T> {
        fun consume(input: T) {

        }
    }

    private fun <MyT> someGeneric(input: MyT) {
        Log.d("My_Log", input.toString())
    }

    fun <V : ViewGroup> someGenericView(input: V) {
        Log.d("My_Log", input.toString())
    }

    fun startLessonColections() {
        fooAdv()
        val phraseArray = arrayOf("Каждый ", "выбирает ", "для ", "себя")
        var phraseList = listOf("Каждый ", "выбирает ", "для ", "себя")
        val numList = listOf(42, 3, 20000, 451, 1984, 100)
        val phraseListMutable = mutableListOf("Каждый ", "выбирает ", "для ", "себя")
        val phraseMap = mapOf(1 to "one", 2 to "two", 3 to "three")

        //phraseList = phraseList.toMutableList()
        phraseListMutable.add("")
        phraseListMutable.removeAt(2)

        val newFilteredPhraseList = phraseList.filter { it.length > 4 }
        val newList = phraseList.map { "$it" }
        val newFilteredNumList = numList.sorted()
        val first = numList.first()
        val last = numList.last()

        val location = Pair(first, last)
        location.first
        location.second
        val locationAnotherOne = first to last
        locationAnotherOne.first
        locationAnotherOne.second
    }

    fun startLesson() {

        //val strangeThing: Test = null!! Это, конечно, забавно получается

        testJava = null
        if (testJava != null) {
            testJava = null
            val response: String? = testJava?.serverResponse()
            val responseShoot: String = testJava!!.serverResponse() //Делаем глупость
            val responseKotlin: String = response ?: "none" //Оператор "Элвис"
        }

        val strNull: String? = testJava2.fooRun()
        val strNotNull: String = testJava2.fooRun()

        val testNullable: Test? = null
        if (testJava != null) {
            testJava = null
            testKotlin = testJava!!
        }
        testKotlin = testJava ?: Test()

        testKotlin = if (testJava != null) {
            testJava = null
            testJava!! //nullpointer
        } else {
            Test()
        }

        if (testNullable != null) { //всегда false
            testKotlin = testNullable
            testKotlin.fooRun() //nullpointer
        }
    }

    fun setupTestJava(testJava: Test?) {
        if (testJava != null) {
            testKotlin = testJava
        }
    }

    override fun foo() {
        TODO("Not yet implemented")
    }

    override var field: String
        get() = TODO("Not yet implemented")
        set(value) {}
}