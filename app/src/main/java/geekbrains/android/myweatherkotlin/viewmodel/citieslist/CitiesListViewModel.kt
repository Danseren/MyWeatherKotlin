package geekbrains.android.myweatherkotlin.viewmodel.citieslist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.android.myweatherkotlin.model.CityLocation
import geekbrains.android.myweatherkotlin.model.RepositoryCitiesList
import geekbrains.android.myweatherkotlin.model.RepositoryCitiesListImpl
import java.lang.Thread.sleep
import kotlin.random.Random

class CitiesListViewModel(private val liveData: MutableLiveData<CityListFragmentAppState> = MutableLiveData<CityListFragmentAppState>()) :
    ViewModel() {

    lateinit var repositoryCitiesList: RepositoryCitiesList
    //lateinit var repositorySingle: RepositorySingle

    fun getLiveData(): MutableLiveData<CityListFragmentAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {

        repositoryCitiesList = RepositoryCitiesListImpl()
    }

    fun getWeatherListForRussia() {
        sentRequest(CityLocation.Russia)
    }

    fun getWeatherListForEurope() {
        sentRequest(CityLocation.Europe)
    }

    fun sentRequest(location: CityLocation) {

        liveData.value = CityListFragmentAppState.Loading
        val rand = (0..3).random(Random(System.nanoTime()))
        Log.d("My_Log", rand.toString())
        if (rand == 3) {
            try {
                liveData.postValue(CityListFragmentAppState.Error(IllegalStateException("Что-то пошлло не так")))
            } catch (e: IllegalStateException) {
                Log.d("My_Log", "OK?!")
            }
        } else if (rand == 2) {
            liveData.postValue(
                CityListFragmentAppState.SuccessMulti(
                    repositoryCitiesList.getListCities(
                        location
                    )
                )
            )
            //Не пойму почему в логах прописывается и Loading и Success
        } else {
            liveData.value = CityListFragmentAppState.Loading
            Thread {
                sleep(2000L)
                liveData.postValue(
                    CityListFragmentAppState.SuccessMulti(
                        repositoryCitiesList.getListCities(
                            location
                        )
                    )
                )
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