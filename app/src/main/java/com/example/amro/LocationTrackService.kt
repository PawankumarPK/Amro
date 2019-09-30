package com.example.amro


import com.example.amro.api.LocationStats
import com.example.amro.api.RetrofitClient
import com.example.amro.api.models.Navigation.Face
import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response

class LocationTrackService : IntentService("FaceTrackService") {

    var locX = 0f
    var locY = 0f

    fun getFaceXY() {

        val api = RetrofitClient.apiService
        val call = api.faceXY()

        call.enqueue(object : retrofit2.Callback<Face> {
            override fun onFailure(call: Call<Face>?, t: Throwable?) {
                // Toast.makeText(applicationContext, "Service Fail : ", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    getFaceXY()
                }, 1000)
            }

            override fun onResponse(call: Call<Face>, response: Response<Face>) {
                if (response.isSuccessful) {
                 //   Toast.makeText(applicationContext, "Service Success : ", Toast.LENGTH_SHORT).show()
                    val face = response.body().face!!
                    val x = face.x
                    val y = face.y

                    LocationStats.x = x!!
                    LocationStats.y = y!!

                    if (locX != x || locY != y) {
                        val intent = Intent()
                        intent.action = "ai.jetbrain.LocationXY"
                        intent.putExtra("x", x)
                        intent.putExtra("y", y)
                        intent.putExtra("error", false)
                        sendBroadcast(intent)
                    }

                    Handler().postDelayed({
                        getFaceXY()
                    }, 1000)
                }
            }
        })
    }

    override fun onHandleIntent(p0: Intent?) {
        try {
            getFaceXY()
        } catch (e: InterruptedException) {
            Toast.makeText(applicationContext, "Thread Exception!!", Toast.LENGTH_SHORT).show()
            Thread.currentThread().interrupt()
            Handler().postDelayed({
                getFaceXY()
            }, 1000)
        }
    }
}


