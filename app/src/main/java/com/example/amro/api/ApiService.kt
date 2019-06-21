package com.example.amro.api

import com.example.amro.api.FloorRoomModels.FloorListModel
import com.example.amro.api.StockModels.StockListModel
import com.example.amro.api.UserModels.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/amro-stock")
    fun getStockList(@Query("t") tripId:Int): Call<StockListModel>

    @GET("/floors-rooms")
    fun getFloorList(): Call<FloorListModel>

    @POST("/delivery")
    @FormUrlEncoded
    fun checkPasscode(@Field("pin") pin:String): Call<UserModel>

}