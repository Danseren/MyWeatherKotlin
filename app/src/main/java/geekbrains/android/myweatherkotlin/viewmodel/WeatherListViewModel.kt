package geekbrains.android.myweatherkotlin.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.android.myweatherkotlin.model.*
import java.lang.Thread.sleep
import kotlin.random.Random

class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) : ViewModel() {

    lateinit var repositoryMulti: RepositoryMulti
    lateinit var repositorySingle: RepositorySingle

    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repositorySingle = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
        repositoryMulti = RepositoryLocalImpl()
    }

    fun getWeatherListForRussia() {
        sentRequest(CityLocation.Russia)
    }

    fun getWeatherListForEurope() {
        sentRequest(CityLocation.Europe)
    }

    fun sentRequest(location: CityLocation) {

        liveData.value = AppState.Loading
        val rand = (0..3).random(Random(System.nanoTime()))
        Log.d("My_Log", rand.toString())
        if (rand == 3) {
                try {
                    liveData.postValue(AppState.Error(throw IllegalStateException("Что-то пошлло не так")))
                } catch (e : IllegalStateException) {
                    Log.d("My_Log", "OK?!")
                }
        } else if (rand == 2){
            liveData.postValue(AppState.SuccessMulti(repositoryMulti.getListWeather(location)))
            //Не пойму почему в логах прописывается и Loading и Success
        } else {
            liveData.value = AppState.Loading
            Thread {
                sleep(2000L)
                liveData.postValue(AppState.SuccessMulti(repositoryMulti.getListWeather(location)))
            }.start()
        }
    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() {
        super.onCleared()
    }
}