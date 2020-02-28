package com.example.bluetoothmessaging

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class DevicesRecyclerViewAdapter(val context: Context) :
        RecyclerView.Adapter<DevicesRecyclerViewAdapter.VH>() {


    private var listener: ItemClickListener? = null
    private var devicesList = ArrayList<DeviceData>()

    fun setData(mDeviceList: List<DeviceData>){
        devicesList.addAll(mDeviceList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_single_item, parent, false)
        return VH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        if(devicesList[position].deviceName == "Paired Devices" || devicesList[position].deviceName == "Found Devices"){
            holder.label?.setTypeface(holder.label?.typeface, Typeface.BOLD)
            holder.label?.textSize = 15F
        }else{
            holder.label?.setTypeface(holder.label?.typeface, Typeface.NORMAL)
        }
        holder.label?.text = devicesList[position].deviceName ?: devicesList[position].deviceHardwareAddress
    }

    override fun getItemCount(): Int {
        return devicesList.size
    }


    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView){

        var label: TextView? = itemView.findViewById(R.id.largeLabel)

        init {
            itemView.setOnClickListener{
                devicesList[adapterPosition].let {
                    it.deviceHardwareAddress?.apply {
                        listener?.itemClicked(devicesList[adapterPosition])
                    }
                }

            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener){
        this.listener = listener
    }

    interface ItemClickListener{
        fun itemClicked(deviceData: DeviceData)
    }
}