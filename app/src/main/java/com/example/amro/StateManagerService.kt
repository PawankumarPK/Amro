package com.example.amro

import com.example.amro.api.DeviceStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.StockModels.StateModel
import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response

class StateManagerService : IntentService("StateManagerService") {

    var state = 0

    fun checkState() {

        val api = RetrofitClient.apiService
        val call = api.getState()

        call.enqueue(object : retrofit2.Callback<StateModel> {
            override fun onFailure(call: Call<StateModel>?, t: Throwable?) {
                //Toast.makeText(applicationContext, "Service Fail : ", Toast.LENGTH_SHORT).show()
                DeviceStats.ServerConnected = false

                val intent = Intent()
                intent.action = "ai.jetbrain.RobotState"
                intent.putExtra("error", true)
                sendBroadcast(intent)

                //Thread.sleep(5000)
                Handler().postDelayed({
                    checkState()
                }, 1000)
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
                        intent.action = "ai.jetbrain.RobotState"
                        intent.putExtra("state", newstate)
                        intent.putExtra("tripid", tripId)
                        intent.putExtra("error", false)
                        sendBroadcast(intent)
                    }
                    Handler().postDelayed({
                        checkState()
                    }, 1000)
                }
            }
        })
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            checkState()
        } catch (e: InterruptedException) {
            Toast.makeText(applicationContext, "Thread Exception!!", Toast.LENGTH_SHORT).show()
            //Thread.currentThread().interrupt()
            Handler().postDelayed({
                checkState()
            }, 1000)
        }
    }

}