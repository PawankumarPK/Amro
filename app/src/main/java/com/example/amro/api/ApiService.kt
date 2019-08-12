package com.example.amro.api

import com.example.amro.api.models.FloorRoomModels.FloorListModel
import com.example.amro.api.models.FloorRoomModels.RoomListModel
import com.example.amro.api.models.StandardModels.DeviceStatsModel
import com.example.amro.api.models.StandardModels.StdStatusModel
import com.example.amro.api.models.StockModels.StateModel
import com.example.amro.api.models.StockModels.StockListModel
import com.example.amro.api.models.UserModels.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/amro-state")
    fun getState(): Call<StateModel>

    @GET("/amro-stock")
    fun getStockList(@Query("tripid") tripId:Int): Call<StockListModel>

    @GET("/floors-rooms")
    fun getFloorRoomList(): Call<FloorListModel>

    @GET("/floors")
    fun getFloorList(): Call<FloorListModel>

    @GET("/rooms")
    fun getRoomList(@Query("floorId") floorId:Int): Call<RoomListModel>

    @GET("/dispatch")
    fun dispatch(@Query("pin") pin:Int, @Query("tripid") tripId:Int): Call<UserModel>

    @GET("/delivery")
    fun delivery(@Query("pin") pin:Int, @Query("tripid") tripId:Int): Call<UserModel>

    @GET("/cancel-delivery")
    fun cancelDelivery(@Query("pin") pin:Int, @Query("tripid") tripId:Int): Call<UserModel>

    @GET("/set-goal")
    fun setGoal(@Query("goalid") goalId:Int, @Query("tripid") tripId:Int): Call<StdStatusModel>

    @GET("/newtrip")
    fun newTrip(): Call<StdStatusModel>

    @GET("/set-break")
    fun setBreak(@Query("stop") stop:Int, @Query("tripid") tripId:Int): Call<StdStatusModel>

    @GET("/device-stats")
    fun deviceStats(): Call<DeviceStatsModel>

}