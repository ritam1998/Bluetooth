package com.qi.bluetoothapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AllBluetoothDeviceAdapter(val deviceModelList : ArrayList<DeviceModel>,val onclickListner : OnDevicesClickListner) : RecyclerView.Adapter<AllBluetoothDeviceAdapter.AllDeviceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDeviceViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.alldevices_listview,parent,false)
        return AllDeviceViewHolder(view,onclickListner)
    }

    override fun onBindViewHolder(holder: AllDeviceViewHolder, position: Int) {

        holder.bindAllDevice(deviceModelList[position])
    }

    override fun getItemCount(): Int {
        return deviceModelList.size
    }

    class AllDeviceViewHolder(itview : View,val onclickListner: OnDevicesClickListner) : RecyclerView.ViewHolder(itview) {

        var deviceName = itview.findViewById(R.id.devicename) as TextView
        var deviceAddress = itview.findViewById(R.id.deviceaddress) as TextView

        var devicePairingButton = itview.findViewById(R.id.imageButton3) as ImageButton

        fun bindAllDevice(deviceModel: DeviceModel){

            deviceName.text = deviceModel.deviceName
            deviceAddress.text = deviceModel.deviceAddress

            devicePairingButton.setOnClickListener {
                onclickListner.onclickListner(deviceModel)
            }
        }
    }
}

interface OnDevicesClickListner {
    fun onclickListner(deviceModel: DeviceModel)
}

