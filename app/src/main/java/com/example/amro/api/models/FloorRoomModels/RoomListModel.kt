package com.example.amro.api.models.FloorRoomModels

import com.google.gson.annotations.SerializedName

/**
 * Created by PawanYadav
 */
class RoomListModel {

    @SerializedName("rooms")
    var rooms: ArrayList<RoomModel>? = null
}