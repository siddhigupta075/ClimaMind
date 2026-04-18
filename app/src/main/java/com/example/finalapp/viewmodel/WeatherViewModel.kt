package com.example.finalapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.finalapp.data.local.UserPreferencesManager
import com.example.finalapp.data.remote.model.ForecastItem
import com.example.finalapp.data.remote.model.WeatherResponse
import com.example.finalapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> = _weather
    private var prefsManager: UserPreferencesManager? = null

    private val _userType = MutableLiveData<String>()
    val userType: LiveData<String> = _userType

    private val _forecast = MutableLiveData<List<ForecastItem>>()
    val forecast: LiveData<List<ForecastItem>> = _forecast
    fun initPrefs(context: Context) {
        prefsManager = UserPreferencesManager(context)

        viewModelScope.launch {
            prefsManager?.userPreferencesFlow?.collect {
                _userType.postValue(it.userType)
                _city.postValue(it.city)
            }
        }
    }
    private val _city = MutableLiveData<String>("Lucknow")
    val city: LiveData<String> = _city

    fun setCity(newCity: String) {
        _city.value = newCity
    }

    fun saveUserType(type: String) {
        viewModelScope.launch {
            prefsManager?.saveUserType(type)
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val result = repository.getWeather(city)
                _weather.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchForecast(city: String) {
        viewModelScope.launch {
            try {
                val result = repository.getForecast(city)
                _forecast.postValue(result.list.take(8)) // next ~24 hrs
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun saveCity(city: String) {
        viewModelScope.launch {
            prefsManager?.saveCity(city)
        }
    }
}