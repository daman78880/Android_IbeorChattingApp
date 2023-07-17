package com.ibeor.ibeorchattingapp.modules.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentHomeBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.chatting.chatNow.ChatMatchDataExtraDataClass
import com.ibeor.ibeorchattingapp.modules.home.stackViewAdapter.StackViewAdapter
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var binding: FragmentHomeBinding? = null
    private var userUid: String? = ""

    //  private var viewMode = HomeViewModel()
    private var allUidKeyList = ArrayList<String>()
    private var cardList = ArrayList<FeatchFireBaseRegisterData>()
    private var adapater: StackViewAdapter? = null
    var progI = 1
    private var locatinCode = 1024


    var client: FusedLocationProviderClient? = null
    var latitute: Double? = null
    var longitute: Double? = null

    var manager: CardStackLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        init()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.i("sajdfsdgdjkaf","complete listnerag bes")
//
//        val fragment=requireActivity().supportFragmentManager.findFragmentByTag(R.id.homeFragment2.toString())
//                if(fragment!=null) {
//                    fragment.onActivityResult(requestCode, resultCode, data)
//                    Log.i("sajdfsdgdjkaf","complete listnerag bes fragment")
//                    if(123 == requestCode){
//                        if(resultCode == RESULT_OK){
//                            requestPermssionn()
//                            Log.i("sajdfsdgdjkaf"," inside complete listner")
//
//                        }
//                    }
//                }
//    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
//        val states = LocationSettingsStates.fromIntent(data!!)
            when (requestCode) {
                123 -> when (resultCode) {
                    RESULT_OK -> {
                        requestPermssionn()
                        Log.i("sajdfsdgdjkaf", "auto done without change fragment call")
                        // All required changes were successfully made
                        Log.i("sajdfsdgdjkaf", "onactivity")
                    }
//                    Toast.makeText(requireContext(), states!!.isLocationPresent.toString() + "", Toast.LENGTH_SHORT).show()}
                        Activity.RESULT_CANCELED ->                         // The user was asked to change settings, but chose not to
                        Toast.makeText(requireContext(), "Canceled", Toast.LENGTH_SHORT).show()
//                        else -> {
//
//                        }
//                    }
                }
            }
        }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("successs==", "Onssuecc")

    }



    private fun requestGpsEnabled() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.i("safkjshf", "On success Listener")
            Log.i("comasdnjknf", "complete listner")
//            MyUtils.showProgress(requireContext())
            requestPermssionn()
        }

        task.addOnFailureListener { exception ->

            val statusCode = (exception as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    val rae = exception as ResolvableApiException

                    rae.startResolutionForResult(requireActivity(), 123)
                } catch (sie: IntentSender.SendIntentException) {
                    Log.e("GPS", "Unable to execute request.")
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                    "GPS",
                    "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                )
            }
        }
    }


    open fun requestPermssionn() {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
//                        client = LocationServices.getFusedLocationProviderClient(requireActivity())
//                        setLocationCallBack()
//                        Log.i("testingNowq", "logi $longitute , lati $latitute")

                        Log.i("sdfnsdkjfn","permission granted")
                        client?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
                            var location: Location? = task.result
                            if (location == null) {
                                Log.i("sdfnsdkjfn","getting new location")
                                requestNewLocationData()
                            } else {
                                latitute = location.latitude
                                longitute = location.longitude
                                Log.i("sdfnsdkjfn","setting location ")
                                getLastLocation()
                           }
                        }

                    } else {
                        Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        if (ContextCompat.checkSelfPermission(requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                        ) {
                            showSettingsDialog()
                        } else {
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
            })
            .withErrorListener {
                Log.i("sajdfsdgdjkaf", "error")
                Toast.makeText(requireContext(),
                    "Error occurred! ${it.name} ",
                    Toast.LENGTH_SHORT).show()
            }.onSameThread().check()
    }

    private fun showSettingsDialog() {
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

    private fun setLocationCallBack() {
        client?.lastLocation!!.addOnSuccessListener {
            val perOne = (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            val perTwo = ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (perOne && perTwo) {
                Log.i("saldfjaslf", "Location is ${it?.longitude}")
                latitute = it?.latitude
                longitute = it?.longitude
                if (latitute != null && longitute != null) {
                    MyUtils.putLongAsDouble(requireContext(),
                        MyUtils.userLotiKey,
                        latitute!!.toDouble())
                    MyUtils.putLongAsDouble(requireContext(),
                        MyUtils.userLongiKey,
                        longitute!!.toDouble())
                    val data2 = HashMap<String, Any>()
                    data2.put("user_latitute", latitute!!.toString())
                    data2.put("user_longitute", longitute!!.toString())
                    lifecycleScope.launchWhenResumed {
                        if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                            homeViewModel.uploadDataToFirebaseRegister2(requireContext(), data2)
                                .observe(requireActivity(),
                                    androidx.lifecycle.Observer {
//                                        MyUtils.stopProgress()
                                    })
                        }
                    }
                    lifecycleScope.launchWhenResumed {
                        if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                            homeViewModel.getUserIdList(requireContext())
                                .observe(requireActivity(), Observer {
                                    allUidKeyList = it as ArrayList<String>
                                    Log.i("asjdfbsajvbfa", "reapeat ")
//                gettingAllData()
                                    getLikeDislikeUserList()
                                })
                        }
                    }
                } else {
                    requestNewLocationData()
//                    client = LocationServices.getFusedLocationProviderClient(requireActivity())
//                    setLocationCallBack()
                }
            } else {
//                requestPermssionn()
                requestGpsEnabled()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        client?.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
//        if (isLocationEnabled()) {
            client?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                        latitute=location.latitude
                        longitute=location.longitude
                    Log.i("sdfnsdkjfn","setting location after call back inside lasst location setep")

                    Log.i("sdfsdhfb","Normal Lat ${location?.latitude.toString()} and Long ${location?.longitude.toString()}")
                    val perOne = (ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    val perTwo = ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    if (perOne && perTwo) {
                        Log.i("saldfjaslf", "Location is ${location?.longitude}")
                        latitute = location.latitude
                        longitute = location.longitude
                        if (latitute != null && longitute != null) {
                            Log.i("sdfnsdkjfn","all done user getting")
                            MyUtils.putLongAsDouble(requireContext(),
                                MyUtils.userLotiKey,
                                latitute!!.toDouble())
                            MyUtils.putLongAsDouble(requireContext(),
                                MyUtils.userLongiKey,
                                longitute!!.toDouble())
                            val data2 = HashMap<String, Any>()
                            data2.put("user_latitute", latitute!!.toString())
                            data2.put("user_longitute", longitute!!.toString())
                            lifecycleScope.launchWhenResumed {
                                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                    homeViewModel.uploadDataToFirebaseRegister2(requireContext(), data2)
                                        .observe(requireActivity(),
                                            androidx.lifecycle.Observer {
//                                        MyUtils.stopProgress()
                                            })
                                }
                            }
                            lifecycleScope.launchWhenResumed {
                                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                    homeViewModel.getUserIdList(requireContext())
                                        .observe(requireActivity(), Observer {
                                            allUidKeyList = it as ArrayList<String>
                                            Log.i("asjdfbsajvbfa", "reapeat ")
//                gettingAllData()
                                            getLikeDislikeUserList()
                                        })
                                }
                            }
                        } else {
                            requestNewLocationData()
//                    client = LocationServices.getFusedLocationProviderClient(requireActivity())
//                    setLocationCallBack()
                        }
                    } else {
//                requestPermssionn()
                        requestGpsEnabled()
                    }
                }
            }
//        }
//    else {
//            Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
//            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivity(intent)
//        }
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location? = locationResult.lastLocation
            latitute=mLastLocation?.latitude
            longitute=mLastLocation?.longitude
//            setLocationCallBack()
            Log.i("sdfnsdkjfn","setting location call back")
            getLastLocation()
            Log.i("sdfsdhfb","Call back Lat ${mLastLocation?.latitude.toString()} and Long ${mLastLocation?.longitude.toString()}")
            Toast.makeText(requireContext(),"Call back Lat ${mLastLocation?.latitude.toString()} and Long ${mLastLocation?.longitude.toString()}",Toast.LENGTH_SHORT).show()

        }
    }
    private fun startRipplee() {
        binding!!.apply {
            includeRippleDiscover.mainRippleTwo.startRippleAnimation()
            val img = MyUtils.getArray(requireContext(), MyUtils.userArrayImageLinkKey)
            Glide.with(requireContext()).load(img!![0]).into(includeRippleDiscover.imgTestingRipple)
            Glide.with(requireContext()).load(img[0]).into(toolBar.imUserImagetl)
            rippelDiscoverLayout.visibility = View.VISIBLE
            cardStackView.visibility = View.GONE
        }
    }

    private fun stopRipple() {
        binding!!.apply {
            includeRippleDiscover.mainRippleTwo.stopRippleAnimation()
            rippelDiscoverLayout.visibility = View.GONE
            cardStackView.visibility = View.VISIBLE
        }
    }

    private fun init() {
        homeViewModel = HomeViewModel()
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding!!.apply {
            toolBar.imUserImagetl.clipToOutline = true
            MyUtils.putBoolean(requireContext(), MyUtils.userLoginKey, true)
            userUid = MyUtils.getString(requireContext(), MyUtils.userUidKey)

            requestGpsEnabled()
            startRipplee()
            clickListener()
        }
    }

    private fun getLikeDislikeUserList() {
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                CoroutineScope(Dispatchers.IO).run {
                    homeViewModel.getAllLikeAndDisLikeUsers(requireContext())
                        .observe(requireActivity(), Observer {
                            val list = it as ArrayList<String>
//                    list.remove(userUid)
                            Log.i("asjdfbsajvbfa", "reapeat 2")
                            if (list.size >= 1) {
                                Log.i("asjdfbsajvbfa", "reapeat 2.1")
                                Log.i("likeDislikeList", " like or dislike list is ${list}}")
                                gettingAllData(list)
                            } else {
//                        startRipplee()
                                Log.i("asjdfbsajvbfa", "reapeat 2.2")
                                gettingAllData(list)
                                Log.i("likeDislikeList", "Empty like or dislike list is ${list}}")
                            }
                        })
                }
            }
        }
    }

    private fun gettingAllData(testList: ArrayList<String>) {
        binding!!.apply {
            lifecycleScope.launchWhenResumed {
                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                    CoroutineScope(Dispatchers.IO).run {
                        Log.i("asjdfbsajvbfa", "reapeat 2.9 ${testList.size}")
                        homeViewModel.fetchFirebaseData(requireContext(), testList)
                            .observe(requireActivity(), Observer {
                                cardList = it as ArrayList<FeatchFireBaseRegisterData>
                                Log.i("owenerDataAded", "return data is last ${cardList}")
                                Log.i("asjfnhjkasbfksvcfgan",
                                    "just getting data list ${cardList.size}")
                                for (i in 0 until cardList.size) {
                                    if (cardList[i].user_uid.equals(userUid)) {
                                        Log.i("owenerDataAded", "added owner list to user list}")
                                        userData = cardList[i]
                                        break
                                    }
                                }
                                Log.i("asjfnhjkasbfksvcfgan",
                                    "after removing data list ${cardList.size}")
                                if (userData.user_completeStatus?.equals("4")!!) {
                                    progressCircle(80)
                                } else if (userData.user_completeStatus!!.equals("3")) {
                                    progressCircle(60)
                                } else if (userData.user_completeStatus!!.equals("2")) {
                                    progressCircle(40)
                                } else if (userData.user_completeStatus!!.equals("1")) {
                                    progressCircle(20)
                                } else {
                                    progressCircle(10)
                                }
                                lifecycleScope.launchWhenResumed {
                                    if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                        homeViewModel.checkUserPresentOrNot(requireContext(),
                                            userUid!!,
                                            cardList)
                                            .observe(requireActivity(),
                                                Observer {
                                                    cardList =
                                                        it as ArrayList<FeatchFireBaseRegisterData> /* = java.util.ArrayList<com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData> */
                                                    Log.i("asjfnhjkasbfksvcfgan",
                                                        "inside presentor not function data list ${cardList.size}")
                                                    setData()
                                                    setAdapter(cardList)
                                                    if (cardList.size >= 1) {
                                                        stopRipple()
                                                    }
                                                    clickListener()
                                                })
                                    }
                                }
                            })
                    }
                }
            }
        }
    }

    private fun progressCircle(value: Int) {

        Handler().postDelayed(object : Runnable {
            override fun run() {
                if (progI <= value) {
                    binding!!.apply {
                        toolBar.txtProgressPercentageTemp.text = "$progI%"
                        toolBar.imUserProgressBar.progress = progI
                        progressCircle(value)
                        progI++
                    }
                }
            }

        }, 50)
    }

    override fun onResume() {
        super.onResume()
        initCard()
            if (MyUtils.checkStatus==true){
                Log.i("sajdfsdgdjkaf","auto call ")
                requestPermssionn()
            }
    }

    fun initCard() {
        binding!!.apply {
            includeRippleDiscover.imgTestingRipple.clipToOutline = true
        }
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {

            override fun onCardDragging(direction: Direction?, ratio: Float) {
                if (direction == Direction.Right || direction == Direction.Left) {
                    manager!!.setCanScrollVertical(true)
                } else if (direction == Direction.Top || direction == Direction.Bottom) {
                    manager!!.setCanScrollVertical(true)
                } else {
                    manager!!.setCanScrollVertical(false)
                }
            }

            override fun onCardSwiped(direction: Direction?) {
                val pos = manager?.topPosition

                if (direction == Direction.Left) {
                    val posInside = manager?.topPosition
                    if (posInside != null) {
                        lifecycleScope.launchWhenResumed {
                            if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                CoroutineScope(Dispatchers.IO).run {
                                    homeViewModel.addDataToLikeDataTwo(requireContext(),
                                        cardList[posInside - 1].user_uid!!,
                                        cardList[posInside - 1].user_name!!,
                                        cardList[posInside - 1].user_age!!,
                                        cardList[posInside - 1].image_url_list?.get(0)!!,
                                        "2").observe(requireActivity(),
                                        Observer {
                                            val flag = it as Boolean
                                            if (flag == true) {
                                                Log.i("dataAddded",
                                                    "Dis_liked data added successfully inside fragment")
                                            }
                                        })
                                }
                            }
                        }
                    }
                }

                if (direction == Direction.Right) {
                    val posInside = manager?.topPosition
                    if (posInside != null) {
                        lifecycleScope.launchWhenResumed {
                            CoroutineScope(Dispatchers.IO).run {
                                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                    homeViewModel.checkingUserAlreadyLikeYou(requireContext(),
                                        cardList[posInside - 1].user_uid!!)
                                        .observe(requireActivity(),
                                            Observer {
                                                val llist = it as String
                                                if (!llist.equals("null") && llist != null) {
                                                    var chatId = ""
                                                    if (userUid!! < cardList[posInside - 1].user_uid!!) {
                                                        Log.e("safdsafd99", "if chat id")
                                                        chatId =
                                                            "${userUid}_${cardList[posInside - 1].user_uid!!}"
                                                    } else {
                                                        Log.e("safdsafd99", "else chat id")
                                                        chatId =
                                                            "${cardList[posInside - 1].user_uid!!}_${userUid}"
                                                    }
                                                    val userOneMap = HashMap<String, Any>()
                                                    val userTwoMap = HashMap<String, Any>()
                                                    val img = MyUtils.getArray(requireContext(),
                                                        MyUtils.userArrayImageLinkKey)
                                                    Log.i("userimagekasfbdnksa", "user image link is ${img!!.get(0)}")
                                                    userOneMap["name"] = MyUtils.getString(
                                                        requireContext(),
                                                        MyUtils.userNameKey)!!
                                                    userOneMap["photo"] = img[0]
                                                    userOneMap["userView"] = "0"
                                                    userOneMap["typingStatus"] = "false"

//                                            userOneMap["last_msg"] =""
//                                            userOneMap["time"] =0L
                                                    userOneMap["unseen_msg_count"] = 0L

                                                    userOneMap["chatId"] = chatId
                                                    userTwoMap["name"] =
                                                        cardList[posInside - 1].user_name!!
                                                    userTwoMap["photo"] =
                                                        cardList[posInside - 1].image_url_list?.get(
                                                            0)!!
                                                    userTwoMap["userView"] = "0"
                                                    userTwoMap["typingStatus"] = "false"
                                                    userTwoMap["chatId"] = chatId

//                                            userTwoMap["last_msg"]= ""
                                                    userTwoMap["unseen_msg_count"] = 0L
//                                            userTwoMap["time"]= 0L
                                                    Log.i("asjkdfnsjkadfbn", "going to save member")
//                                            CoroutineScope(Dispatchers.IO).run {
//                                                saveMemberData(userUid!!, cardList[posInside - 1].user_uid!!, chatId)
//                                            }
//                                            Log.i("asjkdfnsjkadfbn","going to saveUserData member")
//                                            CoroutineScope(Dispatchers.IO).run {
//                                            }
//                                            saveUserData(chatId,userOneMap,userUid!!)
//                                            Log.i("asjkdfnsjkadfbn","going for other one saveUserData member")
//                                            CoroutineScope(Dispatchers.IO).run {
//                                                saveUserData(chatId,userTwoMap,cardList[posInside-1].user_uid!!)}
                                                    val arrayTesting =
                                                        ArrayList<ChatMatchDataExtraDataClass>()
                                                    arrayTesting.add(ChatMatchDataExtraDataClass(
                                                        chatId,
                                                        userOneMap,
                                                        userUid!!))
                                                    arrayTesting.add(ChatMatchDataExtraDataClass(
                                                        chatId,
                                                        userTwoMap,
                                                        cardList[posInside - 1].user_uid!!))
                                                    lifecycleScope.launchWhenResumed {
                                                        if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                                            homeViewModel.arrayAddUserChatExtraFeature(
                                                                arrayTesting)
                                                                .observe(requireActivity(),
                                                                    Observer {
                                                                        val check = it as Boolean
                                                                        if (check) {

//                                                                CoroutineScope(Dispatchers.IO).run {
                                                                            lifecycleScope.launchWhenResumed {
                                                                                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                                                                    saveMemberData(
                                                                                        userUid!!,
                                                                                        cardList[posInside - 1].user_uid!!,
                                                                                        chatId)
                                                                                }
                                                                            }

                                                                            Log.i("asdfbnasbdf",
                                                                                "Data added not turn to change like status")
                                                                            lifecycleScope.launchWhenResumed {
                                                                                if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                                                                    CoroutineScope(
                                                                                        Dispatchers.IO).run {
                                                                                        homeViewModel.addDataToLikeDataMatchTwo(
                                                                                            requireContext(),
                                                                                            cardList[posInside - 1].user_uid!!,
                                                                                            cardList[posInside - 1].user_name!!,
                                                                                            cardList[posInside - 1].user_age!!,
                                                                                            cardList[posInside - 1].image_url_list?.get(
                                                                                                0)!!)
                                                                                            .observe(
                                                                                                requireActivity(),
                                                                                                Observer {
                                                                                                    val flag =
                                                                                                        it as Boolean
                                                                                                    if (flag == true) {

                                                                                                        lifecycleScope.launchWhenResumed {
                                                                                                            if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                                                                                                CoroutineScope(
                                                                                                                    Dispatchers.IO).run {

                                                                                                                    homeViewModel.changUserStatus(
                                                                                                                        requireContext(),
                                                                                                                        cardList[posInside - 1].user_uid!!)
                                                                                                                        .observe(
                                                                                                                            requireActivity(),
                                                                                                                            Observer {
                                                                                                                                Log.i(
                                                                                                                                    "checkNowSataus",
                                                                                                                                    "update status of ${cardList[posInside - 1].user_uid!!}+${
                                                                                                                                        MyUtils.getString(
                                                                                                                                            requireContext(),
                                                                                                                                            MyUtils.userUidKey)
                                                                                                                                    } bro")
                                                                                                                            })
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        Log.i(
                                                                                                            "dataAddded",
                                                                                                            "Like data added successfully inside fragment")
                                                                                                    } else {

                                                                                                    }
                                                                                                })
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {

                                                                        }
                                                                    })
                                                        }
                                                    }

                                                } else {
                                                    Log.i("lkjsafnjksnf", "else part of liked user")
                                                    lifecycleScope.launchWhenResumed {
                                                        if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                                                            CoroutineScope(Dispatchers.IO).run {
                                                                homeViewModel.addDataToLikeDataTwo(
                                                                    requireContext(),
                                                                    cardList[posInside - 1].user_uid!!,
                                                                    cardList[posInside - 1].user_name!!,
                                                                    cardList[posInside - 1].user_age!!,
                                                                    cardList[posInside - 1].image_url_list?.get(
                                                                        0)!!,
                                                                    "1").observe(requireActivity(),
                                                                    Observer {
                                                                        val flag = it as Boolean
                                                                        if (flag == true) {
                                                                            Log.i("dataAddded",
                                                                                "Like data added successfully inside fragment")
                                                                        }
                                                                    })
                                                            }
                                                        }
                                                    }
                                                }
                                            })
                                }
                            }
                        }

                    }
                }
            }


            override fun onCardRewound() {
                Log.i("sbfjkasbdnsafkd", "On CardRewind directin ")
                manager!!.setCanScrollVertical(false)
            }

            override fun onCardCanceled() {
                Log.i("sbfjkasbdnsafkd", "On CardCanceled  ")
                manager!!.setCanScrollVertical(false)
            }

            override fun onCardAppeared(view: View?, position: Int) {
                Log.i("sbfjkasbdnsafkd", "On CardApped  position->${position} ")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.i("sbfjkasbdnsafkd", "On CardDisappeard position ->${position}")
            }
        })
        manager!!.setStackFrom(StackFrom.None)
        manager!!.setVisibleCount(3)
        manager!!.setTranslationInterval(8.0f)
        manager!!.setScaleInterval(0.95f)
        manager!!.setSwipeThreshold(0.3f)
        manager!!.setMaxDegree(20.0f)
        manager!!.setDirections(Direction.HORIZONTAL)
        manager!!.setCanScrollHorizontal(true)
        manager!!.setCanScrollVertical(false)
        manager!!.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager!!.setOverlayInterpolator(LinearInterpolator())
        binding!!.cardStackView.layoutManager = manager
    }

    private fun saveMemberData(userUid: String, userUid1: String, chatId: String) {
        var userList = ArrayList<String>()
        userList.add(userUid)
        userList.add(userUid1)
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                Log.i("asjkdfnsjkadfbn", "just outside save member")
                homeViewModel.addUserDataList(chatId, userList)

            }
        }
    }

    fun saveUserData(chatId: String, userOneMap: HashMap<String, String>, userUid: String) {
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.homeFragment2) {
                Log.i("asjkdfnsjkadfbn", "just outside saveUserData member")
                homeViewModel.addUserChatExtraFeature(requireContext(), chatId, userOneMap, userUid)
                    .observe(requireActivity(),
                        Observer {
                            val data = it as Boolean
                            if (data) {
                                changeLikeStatus += 1
                                if (changeLikeStatus == 2) {

                                }
                                Log.i("sjfhsakjfdn", "data added successfull")
                            } else {
                                Log.i("sjfhsakjfdn", "data not added")
                            }
                        })
            }
        }
    }

    fun setData() {
        Log.i("owenerDataAded", "=${userData} setting here")
        MyUtils.putString(requireContext(), MyUtils.userUidKey, userData.user_uid!!)
        MyUtils.putString(requireContext(),
            MyUtils.userMobileNumberKey,
            userData.user_mobileNumber!!)
        MyUtils.putString(requireContext(),
            MyUtils.userMobileNumberCodeKey,
            userData.user_mobileNumber_code!!)
        MyUtils.putString(requireContext(), MyUtils.userAgeKey, userData.user_age!!)
        MyUtils.putString(requireContext(), MyUtils.userFullAgeKey, userData.user_full_Age!!)
        MyUtils.putString(requireContext(), MyUtils.userEmailKey, userData.user_email!!)
        MyUtils.putString(requireContext(), MyUtils.userNameKey, userData.user_name!!)
        MyUtils.putString(requireContext(),
            MyUtils.userMobileNumberKey,
            userData.user_mobileNumber!!)
        MyUtils.putString(requireContext(),
            MyUtils.userMobileNumberCodeKey,
            userData.user_mobileNumber_code!!)
        MyUtils.putString(requireContext(), MyUtils.userGenderKey, userData.user_gender!!)
        MyUtils.putString(requireContext(),
            MyUtils.userLookingForKey,
            userData.user_looking_gender!!)
        MyUtils.putString(requireContext(),
            MyUtils.userStageGoing,
            userData.user_completeStatus.toString())
        MyUtils.putLongAsDouble(requireContext(),
            MyUtils.userLotiKey,
            userData.user_latitute?.toDouble()!!)
        MyUtils.putLongAsDouble(requireContext(),
            MyUtils.userLongiKey,
            userData.user_longitute?.toDouble()!!)
        MyUtils.putString(requireContext(),
            MyUtils.userSelectedCountryKeyOne,
            userData.selected_first_root!!)
        MyUtils.putString(requireContext(),
            MyUtils.userSelectedCountryKeyTwo,
            userData.selected_second_root!!)
        MyUtils.putArray(requireContext(), MyUtils.userArrayImageLinkKey, userData.image_url_list!!)
        val img = MyUtils.getArray(requireContext(), MyUtils.userArrayImageLinkKey)
        Log.i("userimagekasfbdnksa", "user image link is ${img!!.get(0)}")
        startRipplee()
    }

    fun heartLike() {
        binding!!.imgHearLike.visibility = View.VISIBLE
        Handler().postDelayed(object : Runnable {
            override fun run() {
                binding!!.imgHearLike.visibility = View.GONE
                swipeRight()
            }
        }, 300)
    }

    private fun swipeRight() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        binding!!.cardStackView.swipe()
    }

    private fun swipeRightRewind() {
        val setting = RewindAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()
        manager!!.setRewindAnimationSetting(setting)
        binding!!.cardStackView.rewind()
    }

    private fun swipeLeftRewind() {
        val setting = RewindAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()
        manager!!.setRewindAnimationSetting(setting)
        binding!!.cardStackView.rewind()
    }

    private fun swipeLeft() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        binding!!.cardStackView.swipe()
    }

    private fun superLike() {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Top)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager?.setSwipeAnimationSetting(setting)
        binding!!.cardStackView.swipe()
    }

    private fun clickListener() {
        binding!!.apply {
            includeRippleDiscover.btnTestingRipple.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment2_to_filterSettingFragment)
            }
            imgChat.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("img", userData.image_url_list?.get(0))
                findNavController().navigate(R.id.action_homeFragment2_to_chatFragment, bundle)
            }
            toolBar.imUserImagetl.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment2_to_profileFragment)
            }
            toolBar.imUserFiltertl.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment2_to_filterSettingFragment)
            }
            imgBoost.setOnClickListener {
                MyUtils.showBoostDialog(requireContext())
            }
            imgLike.setOnClickListener {
                heartLike()
            }
            imgRewindCard.setOnClickListener {
                swipeLeftRewind()
            }
            imgRightRewindCard.setOnClickListener {
                swipeRightRewind()
            }
            imgNoCard.setOnClickListener {
//                Toast.makeText(requireContext(), "Hii", Toast.LENGTH_SHORT).show()
                swipeLeft()
            }
            imgYesCard.setOnClickListener {
                heartLike()
            }
            imgSuperStar.setOnClickListener {
                superLike()
            }
            imgSwipeImage.setOnClickListener {
                Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//                MyUtils.saveRefreshStatus(requireContext(),true)
//                init()
                shuffelImage()
            }
        }
    }

    private fun setAdapter(list: ArrayList<FeatchFireBaseRegisterData>) {
        setListAdapter(list)

    }

    private fun setListAdapter(list: ArrayList<FeatchFireBaseRegisterData>) {
        binding!!.apply {
            Log.i("asjfnhjkasbfksvcfgan", "inside set adapter ${cardList.size}")
            adapater =
                StackViewAdapter(requireContext(), cardList, object : StackViewAdapter.Clickss {
                    override fun onInfoClick(id: Int, position: Int) {
                        val bundle = Bundle()
                        bundle.putString("idKey", cardList[position].user_uid)
                        bundle.putString("userFullAge", cardList[position].user_age)
                        bundle.putString("userName", cardList[position].user_name)
                        bundle.putStringArrayList("imageUrlList", cardList[position].image_url_list)
                        bundle.putDouble("userLatFirst",
                            cardList[position].user_latitute?.toDouble()!!)
                        bundle.putDouble("userLongFirst",
                            cardList[position].user_longitute?.toDouble()!!)
                        bundle.putString("firstRoot", cardList[position].selected_first_root)
                        bundle.putString("SecondRoot", cardList[position].selected_second_root)
                        findNavController().navigate(R.id.action_homeFragment2_to_userInfoFragment,
                            bundle)
                    }
                })
            cardStackView.adapter = adapater
//                adapater?.notifyDataSetChanged()
            cardStackView.itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
        }
    }

    private fun shuffelImage() {
        MyUtils.showProgress(requireContext())
        Handler(Looper.myLooper()!!).postDelayed(object : Runnable {
            override fun run() {
                cardList.shuffle()
                binding?.cardStackView?.adapter!!.notifyDataSetChanged()
                MyUtils.stopProgress()
            }
        }, 100)
    }

    companion object {
        private var changeLikeStatus = 0
        var userData = FeatchFireBaseRegisterData()
    }

}









