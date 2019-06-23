package com.example.amro

import com.example.amro.api.RetrofitClient
import com.example.amro.api.StockModels.StateModel
import com.example.amro.api.TripDetails
import android.app.IntentService
import android.content.Intent
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response

class StateManagerService : IntentService("StateManagerService") {

    override fun onHandleIntent(intent: Intent?) {
        try {
            while(true) {

                val api = RetrofitClient.apiService
                val call = api.getState()

                call.enqueue(object : retrofit2.Callback<StateModel> {
                    override fun onFailure(call: Call<StateModel>?, t: Throwable?) {
                        Toast.makeText(applicationContext, "Service Fail : ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<StateModel>, response: Response<StateModel>) {
                        if (response.isSuccessful) {

                            Toast.makeText(applicationContext, "service success : ", Toast.LENGTH_SHORT).show()

                            val stateModel: StateModel =  response.body()
                            TripDetails.TripState = stateModel.tripStatus!!

                            val intent = Intent()
                            intent.action = "ai.jetbrain.RobotState"
                            intent.putExtra("state", response.body().tripStatus)
                            sendBroadcast(intent)
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