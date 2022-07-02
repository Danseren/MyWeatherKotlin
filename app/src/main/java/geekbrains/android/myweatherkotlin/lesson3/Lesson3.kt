package geekbrains.android.myweatherkotlin.lesson3

object Lesson3 {

    //Сделав изначально всё с val, потом подвисаешь на непонятной ошибке,
    // но потом поменяв на var понимаешь, что так же и сдругим программистом сработает, если он захочет  поменять что-нибудь
    //Надо приучаться так делать
    private var testJava: Test? = null
    private val testJava2: Test = Test()
    private var testKotlin: Test = Test()

//    init {
//        testKotlin = Test()
//    }

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
}