package com.example.vipps_option2

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val baseUrl = "https://restcountries.com/v2/name/"
    private val gson = Gson()
    private val apiData = MutableLiveData<CountryModel>()

    fun getCountryData(country: String) {
        val request = Request.Builder()
            .url(baseUrl+country)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                // Parsing JSON response
                try {
                    val apiResponse = gson.fromJson(response.body()?.string(), Array<CountryModel>::class.java).toList()[0]
                    // Notify on main thread
                    Handler(Looper.getMainLooper()).post {
                        notifyLiveData(apiResponse)
                    }
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        notifyLiveData(null)
                    }
                    e.printStackTrace()
                }
            }
        })
    }

    fun notifyLiveData(data: CountryModel?){
        apiData.value = data
    }

    fun countryApiLiveData() = apiData as LiveData<CountryModel>
}