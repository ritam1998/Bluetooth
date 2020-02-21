package com.qi.bluetoothapp


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager.LayoutParams.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var viewBluetoothState: TextView? = null
    private var buttonTurnOn: Button? = null
    private var buttonTurnOff: Button? = null

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothImage: ImageView? = null

    private var blueToothPairing: Button? = null
    private var allPairedDeviceName: TextView? = null

    private var activeButton: Button? = null

    private var deviceReceiver: DeviceReceiver? = null

    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeActionBar()

        viewBluetoothState = findViewById(R.id.bluetoothstateview)
        bluetoothImage = findViewById(R.id.imageView2)

        buttonTurnOn = findViewById(R.id.button)
        buttonTurnOff = findViewById(R.id.button2)
        blueToothPairing = findViewById(R.id.button3)
        allPairedDeviceName = findViewById(R.id.textView2)

        activeButton = findViewById(R.id.button4)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(bluetoothAdapter == null){
            viewBluetoothState?.text = "Bluetooth is not available"
            viewBluetoothState?.setTextColor(resources.getColor(R.color.colorAccent))

        }else{
            viewBluetoothState?.text = "Bluetooth is available"
            viewBluetoothState?.setTextColor(resources.getColor(R.color.colorPrimary))
        }

        if(bluetoothAdapter?.isEnabled == true){
            bluetoothImage?.setImageResource(R.drawable.ic_bluetooth_searching_black_24dp)
        }else{
            bluetoothImage?.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp)
        }

        buttonTurnOn?.setOnClickListener {
            if(bluetoothAdapter?.isEnabled == false){

                Toast.makeText(this,"Turning on Bluetooth ...",Toast.LENGTH_LONG).show()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent,0)
            }else{

                Toast.makeText(this,"BlueTooth is already on",Toast.LENGTH_LONG).show()
            }
        }

        buttonTurnOff?.setOnClickListener {

            if(bluetoothAdapter?.isEnabled == true){

                bluetoothAdapter?.disable()
                Toast.makeText(this,"Turning BlueTooth Off ...",Toast.LENGTH_LONG).show()
                bluetoothImage?.setImageResource(R.drawable.ic_bluetooth_disabled_black_24dp)
            }else{

                Toast.makeText(this,"BlueTooth is already Off",Toast.LENGTH_LONG).show()
            }
        }

        blueToothPairing?.setOnClickListener {

            if(bluetoothAdapter?.isEnabled == true){

                val pairedDevices : Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

                allPairedDeviceName?.text = ""
                Toast.makeText(this,"Pairing Devices...",Toast.LENGTH_LONG).show()

                pairedDevices?.forEach { device->
                    allPairedDeviceName?.append("\n Device Name :"+device.name+" Address : "+device.address)
                }
            }else{
                Toast.makeText(this,"Turn on bluetooth to get paired devices ..",Toast.LENGTH_LONG).show()
            }
        }

        deviceReceiver = DeviceReceiver(object : AllBlueToothDeviceCallback{

            override fun alldeviceNameAddress(name: String, address: String) {
                allPairedDeviceName?.append("\n Name : "+name+" Address :"+address)
            }
        })


        activeButton?.setOnClickListener {

            startActivity(Intent(this,ScanActivity :: class.java))
        }

    }

//    private val receiver = object : BroadcastReceiver() {
//
//        override fun onReceive(context: Context?, intent: Intent) {
//            when (intent.action) {
//
//                BluetoothDevice.ACTION_FOUND -> intent.getParcelableExtra<BluetoothDevice>(
//                    BluetoothDevice.EXTRA_DEVICE
//                )?.let { device ->
//                    Log.e("Bluetooth", "${device.name} found with address ${device.address}")
//                }
//
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                    Log.e("Bluetooth", "Discovery started")
//                }
//
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    Log.e("Bluetooth", "Discovery finished")
//                }
//
//            }
//        }
//
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
//            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        }
//
//        registerReceiver(receiver, filter)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
//        } else {
//            BluetoothAdapter.getDefaultAdapter().startDiscovery()
//        }
//
//    }
//

    private fun changeActionBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            bluetoothImage?.setImageResource(R.drawable.ic_bluetooth_searching_black_24dp)
            Toast.makeText(this, "BlueTooth is on", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(deviceReceiver)
    }
}
