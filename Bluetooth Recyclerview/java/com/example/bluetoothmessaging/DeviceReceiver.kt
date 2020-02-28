package com.example.bluetoothmessaging

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar

class DeviceReceiver(val mDeviceList : ArrayList<DeviceData>,val devicesAdapter : DevicesRecyclerViewAdapter,val progressBar : ProgressBar) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action

        if (BluetoothDevice.ACTION_FOUND == action) {

            mDeviceList.clear()

            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            val deviceName = device.name
            val deviceHardwareAddress = device.address

            val deviceData = DeviceData(deviceName, deviceHardwareAddress)
            mDeviceList.add(deviceData)

            val setList = HashSet<DeviceData>(mDeviceList)
            setList.clear()

            mDeviceList.addAll(setList)
            devicesAdapter.setData(mDeviceList)
        }

        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            progressBar.visibility = View.INVISIBLE
            //headerLabel.text = getString(R.string.found)
        }
    }
}