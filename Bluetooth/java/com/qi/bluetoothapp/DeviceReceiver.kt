package com.qi.bluetoothapp

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DeviceReceiver(val allBlueToothDeviceCallback: AllBlueToothDeviceCallback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {

        val action = intent.action
        var sameNameavoid : String? = null
        if(BluetoothDevice.ACTION_FOUND == action){

            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

            sameNameavoid = device?.name
            Log.e("All Devices"," "+(device?.name is String).toString())

            if(device?.name != null){
                allBlueToothDeviceCallback.alldeviceNameAddress(device.name.toString(),device.address.toString())
            }
        }
    }
}
interface AllBlueToothDeviceCallback {
    fun alldeviceNameAddress(name : String,address : String)
}
