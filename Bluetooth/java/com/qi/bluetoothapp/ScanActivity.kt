package com.qi.bluetoothapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.alldevices_listview.*
import java.io.IOException
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList


class ScanActivity : AppCompatActivity() {

    private var allDeviceList = ArrayList<DeviceModel>()
    private var recyclerView : RecyclerView?= null

    private var scanDeviceButton : Button?= null

    private var deviceReceiver : DeviceReceiver? = null
    private var bluetoothName : String? = null
    private var blueToothAddress : String? = null

   private var uuid : UUID? = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView?.layoutManager = LinearLayoutManager(this)


        //allDeviceList.add(DeviceModel("Ritam","shyamnagar"))

        val action = supportActionBar
        action?.setDisplayHomeAsUpEnabled(true)
        action?.setDisplayShowHomeEnabled(true)

        scanDeviceButton = findViewById(R.id.button5)

        allDeviceList.clear()

        scanDeviceButton?.setOnClickListener {

            deviceReceiver = DeviceReceiver(object : AllBlueToothDeviceCallback{

                override fun alldeviceNameAddress(name: String, address: String) {

                    Log.e("device name :"," $name")



                    allDeviceList.add(DeviceModel(deviceName = name,deviceAddress = address))

                    val deviceAdapter = AllBluetoothDeviceAdapter(allDeviceList,object : OnDevicesClickListner{

                        override fun onclickListner(deviceModel: DeviceModel) {
                            Toast.makeText(this@ScanActivity,"${deviceModel.deviceName}",Toast.LENGTH_LONG).show()
                            AcceptThread()
                        }

                    })

                    recyclerView?.adapter = deviceAdapter
                }
            })

            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

            registerReceiver(deviceReceiver,intentFilter)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            }else{
                BluetoothAdapter.getDefaultAdapter().startDiscovery()
            }
        }
    }

    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {

            Log.e("case 1", "Socket's accept() method is on $uuid")

            BluetoothAdapter.getDefaultAdapter()?.listenUsingInsecureRfcommWithServiceRecord(name,uuid)
        }

        override fun run() {

            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {

                    Log.e("case 1", "Socket's accept() method is ok")
                    mmServerSocket?.accept()

                } catch (e: IOException) {

                    Log.e("case 1", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    //manageMyConnectedSocket(it)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("case 2", "Could not close the connect socket", e)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            BluetoothAdapter.getDefaultAdapter().startDiscovery()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(deviceReceiver)
    }
}


