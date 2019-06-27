package com.example.amro

import com.example.amro.api.DeviceStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.StockModels.StateModel
import android.app.IntentService
import android.content.Intent
import retrofit2.Call
import retrofit2.Response


class StateManagerService : IntentService("StateManagerService") {

    var state = 0

    override fun onHandleIntent(intent: Intent?) {
        try {
            while(true) {

                val api = RetrofitClient.apiService
                val call = api.getState()

                call.enqueue(object : retrofit2.Callback<StateModel> {
                    override fun onFailure(call: Call<StateModel>?, t: Throwable?) {
                        //Toast.makeText(applicationContext, "Service Fail : ", Toast.LENGTH_SHORT).show()

                        DeviceStats.ServerConnected = false
                    }

                    override fun onResponse(call: Call<StateModel>, response: Response<StateModel>) {
                        if (response.isSuccessful) {

                            DeviceStats.ServerConnected = true
                            //Toast.makeText(applicationContext, "Service success : ", Toast.LENGTH_SHORT).show()

                            val stateModel: StateModel =  response.body()
                            val newstate = stateModel.tripStatus!!
                            val tripId = stateModel.tripId!!
                            if(state!=newstate) {
                                val intent = Intent()
                                intent.setAction("ai.jetbrain.RobotState")
                                intent.putExtra("state", newstate)
                                intent.putExtra("tripid", tripId)
                                sendBroadcast(intent)
                            }
                        }
                    }
                })

                Thread.sleep(5000)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

}