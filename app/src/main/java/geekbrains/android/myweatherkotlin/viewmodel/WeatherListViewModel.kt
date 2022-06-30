package geekbrains.android.myweatherkotlin.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.android.myweatherkotlin.model.Repository
import geekbrains.android.myweatherkotlin.model.RepositoryLocalImpl
import geekbrains.android.myweatherkotlin.model.RepositoryRemoteImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) : ViewModel() {

    lateinit var repository: Repository

    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
    }

    fun sentRequest() {

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
            liveData.postValue(AppState.Success(repository.getWeather(55.755826, 37.617299900000035)))
            //Не пойму почему в логах прописывается и Loading и Success
        } else {
            liveData.value = AppState.Loading
            Thread {
                sleep(2000L)
                liveData.postValue(AppState.Success(repository.getWeather(55.755826, 37.617299900000035)))
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