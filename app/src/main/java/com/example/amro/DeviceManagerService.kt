package com.example.amro

import com.example.amro.api.DeviceStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.StandardModels.DeviceStatsModel
import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response

class DeviceManagerService : IntentService("DeviceManagerService") {

    fun getDeviceStats() {

        val api = RetrofitClient.apiService
        val call = api.deviceStats()

        call.enqueue(object : retrofit2.Callback<DeviceStatsModel> {
            override fun onFailure(call: Call<DeviceStatsModel>?, t: Throwable?) {
                Handler().postDelayed({
                    getDeviceStats()
                }, 5000)
            }

            override fun onResponse(call: Call<DeviceStatsModel>, response: Response<DeviceStatsModel>) {
                if (response.isSuccessful) {
                    val deviceStatsModel: DeviceStatsModel =  response.body()

                    DeviceStats.ServerConnected = true
                    DeviceStats.Battery = deviceStatsModel.Battery!!

                    Handler().postDelayed({
                        getDeviceStats()
                    }, 5000)

                }
            }
        })
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            getDeviceStats()
        } catch (e: InterruptedException) {
            Toast.makeText(applicationContext, "Thread Exception!!", Toast.LENGTH_SHORT).show()
            //Thread.currentThread().interrupt()
            Handler().postDelayed({
                getDeviceStats()
            }, 5000)
        }
    }

}