package ai.jetbrain.sar.api

import ai.jetbrain.sar.api.FloorRoomModels.FloorListModel
import ai.jetbrain.sar.api.StockModels.StockListModel
import ai.jetbrain.sar.api.UserModels.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/amro-stock")
    fun getStockList(@Query("t") tripId:Int): Call<StockListModel>

    @GET("/floors-rooms")
    fun getFloorList(): Call<FloorListModel>

    @POST("/check-pin")
    @FormUrlEncoded
    fun checkPasscode(@Field("pin") pin:String): Call<UserModel>

}