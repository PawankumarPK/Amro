package ai.jetbrain.sar.api.FloorRoomModels

import com.google.gson.annotations.SerializedName

class RoomModel {

    @SerializedName("roomName")
    var roomName: String? = null

    @SerializedName("roomId")
    var roomId: Int? = null

}
