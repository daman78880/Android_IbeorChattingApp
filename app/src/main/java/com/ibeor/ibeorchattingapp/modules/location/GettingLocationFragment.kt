package com.ibeor.ibeorchattingapp.modules.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentGettingLocationBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.HashMap


class GettingLocationFragment : Fragment() {
    private var fragmentGettingLocationBinding:FragmentGettingLocationBinding?=null
    private  var locationManager: LocationManager?=null
    var client: FusedLocationProviderClient? = null
    var latitute:Double?=null
    var longitute:Double?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_getting_location, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentGettingLocationBinding.bind(view)
        fragmentGettingLocationBinding=binding
        init()
    }
    private fun init(){
//        requestGpsEnabled()
        fragmentGettingLocationBinding!!.apply {
            imBackBtnLocation.setOnClickListener {
                findNavController().popBackStack()
            }
            btnEnableLocation.setOnClickListener {
                requestGpsEnabled()
            }
            btnLocatinNotNow.setOnClickListener {
                requestGpsEnabled()
            }
        }
    }
    private fun requestGpsEnabled(){
//        val locationRequest = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }//\
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        val locationRequest = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            MyUtils.showProgress(requireContext())
            requestPermssionn()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
//                    requestGpsEnabled()
                    exception.startResolutionForResult(requireActivity(), 100)
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    Log.i("checkStatus","error is -> ${sendEx.message}")
                }
            }
        }
        if(task.isSuccessful){
            MyUtils.showProgress(requireContext())
            requestPermssionn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in requireActivity().supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
            requestPermssionn()
            Log.i("sajdfsjkaf","auto done")
        }
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            requestPermssionn()
            Log.i("sajdfsjkaf","auto done simple")

        }
    }
    private fun requestPermssionn(){
//        MyUtils.showProgress(requireContext())
        Dexter.withActivity(activity).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object :MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        client = LocationServices.getFusedLocationProviderClient(requireActivity())
                        Log.i("testingNowq", "logi $longitute , lati $latitute")
                        setLocationCallBack()

                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED){

                            showSettingsDialog()
                        }
                        else{
//                          client=LocationServices.getFusedLocationProviderClient(requireActivity())
                            requestGpsEnabled()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                   p1!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(requireContext(),
                    "Error occurred! ",
                    Toast.LENGTH_SHORT).show();
            }.onSameThread().check()
            }
    private fun showSettingsDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        builder.show()
    }
    private fun setLocationCallBack(){
//        client?.lastLocation!!.addOnSuccessListener {
            val perOne=(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            val perTwo=ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if(perOne && perTwo){
                client?.lastLocation!!.addOnSuccessListener {
                latitute = it?.latitude
                longitute = it?.longitude
                if(latitute != null && longitute != null) {
                    MyUtils.putLongAsDouble(requireContext(), MyUtils.userLotiKey, latitute!!.toDouble())
                    MyUtils.putLongAsDouble(requireContext(), MyUtils.userLongiKey, longitute!!.toDouble())
                    val data2= HashMap<String,Any>()
                    data2.put("user_latitute",latitute!!.toString())
                    data2.put("user_longitute",longitute!!.toString())
                    data2.put("user_completeStatus","4")
                    lifecycleScope.launchWhenResumed {
                        if(findNavController().currentDestination!!.id == R.id.gettingLocationFragment) {
                            HomeViewModel().uploadDataToFirebaseRegister2(requireContext(), data2)
                                .observe(requireActivity(),
                                    androidx.lifecycle.Observer {
                                        val flag = it as Boolean
                                        if (flag) {
                                            if (flag == true) {
                                                Log.i("asfdnbasf", "sucess ")
                                                lifecycleScope.launchWhenResumed {
                                                    if(findNavController().currentDestination!!.id == R.id.gettingLocationFragment) {
                                                HomeViewModel().saveSettingData(requireContext(),
                                                    null).observe(requireActivity(),
                                                    Observer {
                                                        val check = it as Boolean
                                                        lifecycleScope.launchWhenResumed {

                                                            if (check) {
                                                                MyUtils.putInt(requireContext(),
                                                                    MyUtils.userFilterStartAge,
                                                                    18)
                                                                MyUtils.putInt(requireContext(),
                                                                    MyUtils.userFilterEndAge,
                                                                    50)
                                                                MyUtils.putBoolean(requireContext(),
                                                                    MyUtils.userFilterAgeFlag,
                                                                    true)
                                                                MyUtils.putBoolean(requireContext(),
                                                                    MyUtils.userFilterDistanceFlag,
                                                                    true)
                                                                MyUtils.putInt(requireContext(),
                                                                    MyUtils.userFilterDistance,
                                                                    100)
                                                                if (findNavController().currentDestination?.id == R.id.gettingLocationFragment) {
                                                                    MyUtils.stopProgress()
                                                                    findNavController().navigate(R.id.action_gettingLocationFragment_to_welcomeSignInIbeorFragment)
                                                                }
                                                            }
                                                        }
                                                    })}}
                                            }
                                        }
                                    })
                        }}
                }
                else{
                    client = LocationServices.getFusedLocationProviderClient(requireActivity())
                    setLocationCallBack()
                }
            }


        }
            else{
                requestGpsEnabled()
            }
    }

}

