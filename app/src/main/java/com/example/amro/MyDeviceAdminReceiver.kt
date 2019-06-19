package ai.jetbrain.sar

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context

/**
 * Created by ajayvishnu on 04/10/18.
 */
class MyDeviceAdminReceiver : DeviceAdminReceiver() {
    companion object {
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, MyDeviceAdminReceiver::class.java)
        }
    }
}