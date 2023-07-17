package com.ibeor.ibeorchattingapp.modules.chatting.chatNow

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.developers.imagezipper.ImageZipper
import com.devlomi.record_view.OnRecordClickListener
import com.devlomi.record_view.OnRecordListener
import com.giphy.sdk.core.GPHCore
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.themes.GPHTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentChatNowBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ChatNowFragment : Fragment() {
    private var binding:FragmentChatNowBinding?=null
    private var userName:String?=null
    private var userImg:String?=null
    private var userUid:String?=null
    private var currentUid:String?=null
    private var chatId:String?=null

    // for audio recoding
    private var mediaRecoder:MediaRecorder? = null
    private var recordFile: File? = null

    // for image and vedio send
    private var RESULT_LOAD_IMAGE=100
    private var PICTURE_RESULT=112
    private var imageUriSecond:Uri?=null
    private val REQUEST_CODE_VEDIO=878
    private val GALLERY=118
    private var chatIdd=""
    private var loginViewStatus=""
    lateinit var firebaseb :FirebaseFirestore
    private var typingCheck=false
    private lateinit var homeViewModel:HomeViewModel
    private var tempMsg=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentChatNowBinding.inflate(layoutInflater)
        Giphy.configure(requireContext(), "FKfvf2pRp0pcvQf9mX8lDFMhpyVERc42")
        firebaseb = FirebaseFirestore.getInstance()
        homeViewModel=HomeViewModel()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }
    private fun recodVoiceInit(){
        binding!!.apply {
            recordButton.setRecordView(recordView)
//            recordButton.setRecordView(recordViewTwo)
            recordButton.isListenForRecord=true
            recordView.cancelBounds=5.0f
            recordView.setSmallMicColor(Color.parseColor("#c2185b"))
            recordView.setLessThanSecondAllowed(false)
            recordView.setSlideToCancelText("Slide To Cancel")
            recordView.setSoundEnabled(false)
            recordView.setSlideToCancelTextColor(Color.parseColor("#ff0000"))
            recordView.setSlideToCancelArrowColor(Color.parseColor("#ff0000"))
            recordView.setCounterTimeColor(Color.parseColor("#ff0000"))
            recordView.isShimmerEffectEnabled = true
            recordView.timeLimit = 10000;//30 sec
//        recordView.setTrashIconColor(Color.parseColor("#fff000"));
            recordView.setTrashIconColor(R.color.black)
            recordView.isRecordButtonGrowingAnimationEnabled = true
            recordButton.setSendIconResource(R.drawable.ic_launcher_background)
            recordView.setOnRecordListener(object : OnRecordListener {
                override fun onStart() {
                    Log.i("sakfdbskadf","onn start")
                    val per= arrayListOf<String>(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                    val check=recodePermission(per)
                    if(check == true) {
                        stopRecodingMedia()
                        hideEditText()
                        recordFile =
                            File(requireContext().filesDir, UUID.randomUUID().toString() + ".m4a")
                        if (!recordFile?.exists()!!) {
                            recordFile?.createNewFile()
                        }
                        try {
                            mediaRecoder = MediaRecorder()
                            mediaRecoder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                            mediaRecoder?.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
                            mediaRecoder?.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                            mediaRecoder?.setOutputFile(recordFile!!.path)
                            mediaRecoder?.prepare()
                            mediaRecoder?.start()
                        } catch (e: IOException) {
                            Log.e("TAG", "prepare() failed")
                        }
                    }
                }

                override fun onCancel() {
                    mediaRecoder?.stop()
                    mediaRecoder?.release()
//                mediaRecoder=null
                    stopRecodingMedia()

                }

                override fun onFinish(recordTime: Long, limitReached: Boolean) {
                    showEditText()
                    mediaRecoder?.stop()
                    if (recordFile?.path != null){
                    val uri=Uri.fromFile(File(recordFile!!.path))
                    val uriReal=getFilePath(uri)
                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                        homeViewModel.changeChatShowMsgValue(requireContext(),
                            userUid!!,
                            "2",
                            chatIdd).observe(requireActivity(),
                            Observer {
                                val t = it as Boolean
                                if (t) {

                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                                        homeViewModel.saveAudioToFireBase(requireContext(), uri)
                                            .observe(requireActivity(),
                                                Observer {
                                                    val link = it as String
                                                    Log.i("sajkjfbjkaf", "value is ${link}")
                                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                                                        uploadData(2,
                                                        chatId!!,
                                                        System.currentTimeMillis(),
                                                        null,
                                                        currentUid!!,
                                                        link,
                                                        null,
                                                        null,
                                                        null)
//                                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                            changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                            changeChatLastMsg(currentUid!!, "Audio Recoding", chatId!!)}}
                                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                                            changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                                }}
                                    })
                                        }}
                                }
                            })

                    }}
                    Log.i("recoding","recoding path is  ${uriReal} and uri ${uri}")
                }}



                override fun onLessThanSecond() {
                    stopRecodingMedia()
                    showEditText()
                }
            })
            recordButton.setOnRecordClickListener(OnRecordClickListener {
                Toast.makeText(requireContext(), "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            })


            recordView.setOnBasketAnimationEndListener {
                showEditText()
            }
        }
    }


    fun getFilePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }

    private fun stopRecodingMedia(){
        if(mediaRecoder != null && recordFile != null){
            recordFile?.delete()
            mediaRecoder?.release()
            mediaRecoder=null
        }
    }
    private fun recodePermission(per:ArrayList<String>):Boolean{
        var value=false
//        Dexter.withActivity(activity).withPermissions(Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
        Dexter.withActivity(activity).withPermissions(per)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                       value = true
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            showSettingsDialog()
                        }
                        else{
                            recodePermission(per)
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
        return value
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
    private fun hideEditText(){
        binding!!.apply {

            etMsgSendChat.visibility=View.GONE
            btnMsgSendChat.visibility=View.GONE
            imgGif.visibility=View.GONE
            recordView.visibility=View.VISIBLE
//            recordLock.visibility=View.VISIBLE
        }
    }
    private fun showEditText(){
        binding!!.apply {
            etMsgSendChat.visibility=View.VISIBLE
            btnMsgSendChat.visibility=View.VISIBLE
            imgGif.visibility=View.VISIBLE
            recordView.visibility=View.GONE
//            recordLock.visibility=View.GONE
        }
    }
    private fun init(){
        binding!!.apply {
        currentUid=MyUtils.getString(requireContext(),MyUtils.userUidKey)
        loginViewStatus= arguments?.getString("login_user_view").toString()
        chatIdd= arguments?.getString("chatId").toString()
        userUid=arguments?.getString("uid")
        userName=arguments?.getString("name")
        userImg=arguments?.getString("img")
            tempImgChattingNow.clipToOutline=true
            txtSetUserDetailImg.clipToOutline=true
            Log.i("asdfbsjdafb","chatIdd ${chatIdd} ")
            Log.i("askdf","name ${userName}, ${userUid} , ${userImg}")
            Glide.with(requireContext()).load(userImg!!).into(txtSetUserDetailImg)
            txtSetUserDetailName.text=userName
            if(currentUid!! < userUid!!){
                chatId="${currentUid}_${userUid}"
            }else{
                chatId="${userUid}_${currentUid}"
            }
            recodVoiceInit()

//            var firebaseb = FirebaseFirestore.getInstance()
            val arrayListModel=ArrayList<ChatModel>()
            imgGif.setOnClickListener {
                showGifDialog(requireContext())
            }
            demoChatStartLayoutChatingNow.setOnClickListener {
                 hideKeyboard(requireActivity())
            }
            rvChattingShowNow.setOnClickListener {
                 hideKeyboard(requireActivity())
            }
            userShowLayoutChatingNow.setOnClickListener {
                 hideKeyboard(requireActivity())
            }

            etMsgSendChat.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.e("asdfjsf","On before text change")

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.e("asdfjsf", "On change listener")
                    if (typingCheck == false) {
                        typingCheck= true
                        changeChatTyingStatus(currentUid!!, "true", chatId!!)
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                    Log.e("asdfjsf", "before running after change listener")
                    if (typingCheck == true) {
                        Handler().postDelayed(object : Runnable {
                            override fun run() {
                                typingCheck = false
                                Log.e("asdfjsf", "Running after change listener")
                                changeChatTyingStatus(currentUid!!, "false", chatId!!)
                            }

                        }, 2000)
                    }
                }

            })

            lifecycleScope.launchWhenResumed {
                if (findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                    CoroutineScope(Dispatchers.IO).run {
                    firebaseb.collection("Chat").document(chatId!!).collection("Message")
                        .addSnapshotListener { value, error ->
                            if (!value?.isEmpty!!) {
                                val len = value.documents.size
                                arrayListModel.clear()
                                for (i in 0 until len) {
                                    Log.i("asmnsfsfsfsf","id of message is ${value.documents[i].id}")
                                    val tempModel = ChatModel()
                                    tempModel.documentId=value.documents[i].id
                                    tempModel.message = value.documents.get(i).data?.get("message").toString()
                                    val num = value.documents.get(i).data?.get("viewType")
                                    val numL = num as Long
                                    val numIn = numL.toInt()
                                    tempModel.viewType = numIn
                                    tempModel.time = value.documents.get(i).data?.get("time") as Long
                                    tempModel.userId =
                                        value.documents.get(i).data?.get("userId").toString()
                                    tempModel.userIdUser =
                                        value.documents.get(i).data?.get("userIdUser").toString()
                                    tempModel.audio_Url=value.documents.get(i).data?.get("audio_Url").toString()
                                    tempModel.imageUrl=value.documents.get(i).data?.get("imageUrl").toString()
                                    tempModel.vedioUrl=value.documents.get(i).data?.get("vedioUrl").toString()
                                    tempModel.gif_sticker_emoji=value.documents.get(i).data?.get("gif_sticker_emoji").toString()
                                    tempModel.readStatus=value.documents.get(i).data?.get("readStatus") as Boolean
                                    arrayListModel.add(tempModel)
                                }
                                demoChatStartLayoutChatingNow.visibility = View.GONE
                                userShowLayoutChatingNow.visibility = View.VISIBLE
                                arrayListModel.sortBy {
                                    it.time
                                }
                                lifecycleScope.launchWhenResumed {
                                    if (findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                                        Glide.with(requireContext()).load(userImg!!)
                                            .into(txtSetUserDetailImg)
                                    }
                                }

                                txtSetUserDetailName.text = userName
                                lifecycleScope.launchWhenResumed {
                                    if (findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                            emptyUnseenCount(userUid!!, chatId!!)}
                                        }
                                        val adapterr =
                                            ChattingAdatperr(requireContext(), arrayListModel,object :ChattingAdatperr.Clicked{
                                                override fun onClick(type: Int, data: String) {
                                                    val bundle=Bundle()
                                                    bundle.putInt("type",type)
                                                    bundle.putString("id",data)
                                                    findNavController().navigate(R.id.action_chatNowFragment_to_previewChatFragment,bundle)
                                                }

                                                override fun onChangeReadStatus(chatId:String,id: String) {
                                                        Log.i("failedDueTo","inside interface id ${chatId} and insideid ${id}")
                                                    CoroutineScope(Dispatchers.IO).run {
                                                        firebaseb.collection("Chat")
                                                            .document(chatId).collection("Message")
                                                            .document(id).update("readStatus", true)
                                                            .addOnSuccessListener {
                                                                Log.i("failedDueTo",
                                                                    "UpdateData SuccessFull.")
                                                            }.addOnFailureListener {
                                                            Log.i("failedDueTo",
                                                                "UpdateDataFailed due to ${it.message}")
                                                        }
                                                    }
                                                }

                                            })
                                        rvChattingShowNow.layoutManager =
                                            LinearLayoutManager(requireContext())
                                        rvChattingShowNow.scrollToPosition(arrayListModel.size - 1)
                                        rvChattingShowNow.adapter = adapterr
                                    }
                                }
                            }
                        }
                    demoUserNameChatingNow.text = "You matched with ${userName}"
                    Log.i("sakjdfbsadf", "value of name is ${userName}")
                    Glide.with(requireContext()).load(userImg).into(tempImgChattingNow)
                    tempTimeAgoChattingNow.text = "0 minutes ago"

                    homeViewModel.getClickStatus(requireContext(),currentUid!!,chatIdd)
                        .observe(requireActivity(),
                            Observer {
                                val value = it as String
                                Log.i("sabfsakfb","return value is ${value}")
                                if (value.equals("0")) {
                                    homeViewModel.changeChatShowMsgValue(requireContext(),
                                        currentUid!!,
                                        "1",chatIdd).observe(requireActivity(),
                                        Observer {
                                            val d = it as Boolean
                                            if (d) {
                                                Log.i("sabfsakfb","changed")
                                                Log.i("asfdjasf", "success")
                                            }
                                        })
                                }
                            })
//                    clickListener()

                }
            }}
        }
        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
            emptyUnseenCount(userUid!!, chatId!!)}
        }
        clickListener()

    }

    private fun clickListener(){
        binding!!.apply {
            imBackBtnChat.setOnClickListener {
                findNavController().popBackStack()
            }
            btnMsgSendChat.setOnClickListener {
                Log.i("asjfdbsadfb","rahul")
                if(etMsgSendChat.text?.isNotEmpty()!!){
                    tempMsg=etMsgSendChat.text.toString()
                    etMsgSendChat.text?.clear()
//                    msgArray.add(etMsgSendChat.text.toString())
                    Log.i("asjfdbsadfb","parameter is ${userUid} and 2 and ${chatId}")
                    homeViewModel.changeChatShowMsgValue(requireContext(),currentUid!!,"2",chatIdd!!).observe(requireActivity(),
                        Observer {
                            val t=it as Boolean
                            Log.i("sabfsakfb","change status return value is ${t}")
                            if(t){
                                if(!etMsgSendChat.text!!.equals("")) {
//                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                        changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                        changeChatLastMsg(currentUid!!, etMsgSendChat.text.toString(), chatId!!)}}
                                     lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                        changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                    uploadData(1, chatId!!, System.currentTimeMillis(), tempMsg,currentUid!!,null,null,null,null)}}
//                                    uploadData(1, chatId!!, System.currentTimeMillis(), etMsgSendChat.text.toString(),currentUid!!,null,null,null,null)}}
//                                    uploadData(1, chatId!!, System.currentTimeMillis(), msgArray.get(0),currentUid!!,null,null,null,null)}}
//                                    etMsgSendChat.text!!.clear()


                                }
                                Log.i("asjfbsnabf","value update 3 please check")
                            }
                        })
                }
            }
        }
    }

    private fun uploadData(viewType:Int,chatId:String,currentTime:Long,Msg:String?,currentUserId:String,audioUrl:String?,imageUrl:String?,vedioUrl:String?,gifId:String?){
        
        if(viewType == 1){
            CoroutineScope(Dispatchers.Main).run {
                val model = ChatModel(viewType,
                    chatId,
                    currentUserId,
                    Msg,
                    currentTime,
                    audioUrl,
                    imageUrl,
                    vedioUrl,
                    gifId)
                addMsgField(chatId, model)
                homeViewModel.saveChat(viewType, model).observe(requireActivity(), Observer {
                    val t = it as Boolean
                    if (t) {
//                        msgArray.removeAt(0)
                        Log.i("data", "Data result okhe")
                    } else {
                        Log.i("data", "Data result failed")
                    }
                })
            }
        }
        else if (viewType == 2){
            val model=ChatModel(viewType,chatId,currentUserId,Msg,currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            val model2=ChatModel(viewType,chatId,currentUserId,"Audio recoding",currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            addMsgField(chatId,model2)
            homeViewModel.saveChat(viewType,model).observe(requireActivity(), Observer {
                val t=it as Boolean
                if(t){
                    Log.i("data","Data result okhe")
                }
                else{
                    Log.i("data","Data result failed")
                }
            })
        }
        else if(viewType == 3){
            val model=ChatModel(viewType,chatId,currentUserId, Msg,currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            val model2=ChatModel(viewType,chatId,currentUserId,"Image",currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            addMsgField(chatId,model2)
            homeViewModel.saveChat(viewType,model).observe(requireActivity(), Observer {
                val t=it as Boolean
                if(t){
                    Log.i("data","Data result okhe")
                }
                else{
                    Log.i("data","Data result failed")
                }
            })
        }
        else if(viewType == 4){
            val model2=ChatModel(viewType,chatId,currentUserId,"Vedio",currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            addMsgField(chatId,model2)
            val model=ChatModel(viewType,chatId,currentUserId, Msg,currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            homeViewModel.saveChat(viewType,model).observe(requireActivity(), Observer {
                val t=it as Boolean
                if(t){
                    Log.i("data","Data result okhe")
                }
                else{
                    Log.i("data","Data result failed")
                }
            })
        }
        else if(viewType == 5){
            val model2=ChatModel(viewType,chatId,currentUserId,"Emoji",currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            addMsgField(chatId,model2)
            val model=ChatModel(viewType,chatId,currentUserId, Msg,currentTime,audioUrl,imageUrl,vedioUrl,gifId)
            homeViewModel.saveChat(viewType,model).observe(requireActivity(), Observer {
                val t=it as Boolean
                if(t){
                    Log.i("data","Data result okhe")
                }
                else{
                    Log.i("data","Data result failed")
                }
            })
        }
    }
    private fun showGifDialog(context: Context) {
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.select_data_send_layout, null)
        dialog.setContentView(view)
        val galleryImage: AppCompatImageView = view.findViewById(R.id.dialogGalleryImagePick)
        val cameraImage: AppCompatImageView = view.findViewById(R.id.dialogCameraImagePick)
        val galleryVedio: AppCompatImageView = view.findViewById(R.id.dialogGalleryVedioPick)
        val cameraVedio: AppCompatImageView = view.findViewById(R.id.dialogCameraVedioPick)
        val gifSend: AppCompatImageView = view.findViewById(R.id.dialogGifPick)

        galleryImage.setOnClickListener {
            val per = arrayListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
            val check = recodePermission(per)
            if (check == true) {
                getImageFormGallery()
            }
            dialog.dismiss()
        }
        cameraImage.setOnClickListener {
            val per = arrayListOf<String>(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val check = recodePermission(per)
            if (check == true) {
                getImageFromCameraSecondMethod()
            }
            dialog.dismiss()
        }
        galleryVedio.setOnClickListener {
            val per = arrayListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
            val check = recodePermission(per)
            if (check == true) {
//                pickVedioFormGallery()
                chooseVideoFromGallary()
            }
            dialog.dismiss()
        }
        cameraVedio.setOnClickListener {
            val per = arrayListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
            val check = recodePermission(per)
            if (check == true) {
                openCameraToCaptureVideo()
            }
            dialog.dismiss()
        }
        gifSend.setOnClickListener {
            val settings = GPHSettings(theme = GPHTheme.Custom)
            settings.theme = GPHTheme.Dark
            settings.mediaTypeConfig = arrayOf(GPHContentType.gif,
                GPHContentType.sticker,
                GPHContentType.text,
                GPHContentType.emoji)
            var giphyDialog = GiphyDialogFragment.newInstance(settings)
            giphyDialog.show(requireActivity().supportFragmentManager, "giphy_dialog")
            giphyDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
                override fun didSearchTerm(term: String) {
                    Log.i("slfkjaljasf", "did search term ${term}")
                }

                override fun onDismissed(selectedContentType: GPHContentType) {
                    Log.i("slfkjaljasf", "content type ${selectedContentType}")
                }

                override fun onGifSelected(
                    media: Media,
                    searchTerm: String?,
                    selectedContentType: GPHContentType,
                ) {
                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                        homeViewModel.changeChatShowMsgValue(requireContext(),
                            userUid!!,
                            "2",
                            chatIdd).observe(requireActivity(),
                            Observer {
                                val t = it as Boolean
                                if (t) {
                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                    uploadData(5,
                                        chatId!!,
                                        System.currentTimeMillis(),
                                        null,
                                        currentUid!!,
                                        null,
                                        null,
                                        null,
                                        media.id)
//                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                            changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                            changeChatLastMsg(currentUid!!, "Stiker", chatId!!)}}
                                        lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                            changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                    Log.i("slfkjaljasf",
                                        "Search Tearm ${searchTerm} and media $${media.images.original!!.gifUrl} and selectedContent ${selectedContentType}")
                                    dialog.dismiss()
                                }}
                                }
                            })
                    }}
                }
            }
        }
            dialog.show()
    }

    fun getImageFormGallery(){
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK &&  data != null ) {
            val selectedImage: Uri = data.data!!
            val path:String?=getFilePath(selectedImage)
            if(path!= null){
                val galleryFile=abc(File(path.toString()))
                lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                    homeViewModel.changeChatShowMsgValue(requireContext(), userUid!!, "2", chatIdd)
                        .observe(requireActivity(),
                            Observer {
                                val t = it as Boolean
                                if (t) {
                                    homeViewModel.saveImageToFireBase(requireContext(),
                                       "images",
                                        galleryFile.path).observe(
                                        requireActivity(), Observer {
                                            val link = it as String
                                            Log.i("linasnfdjknsa", "pic link is ${link}")
                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {
                                                uploadData(3,
                                                chatId!!,
                                                System.currentTimeMillis(),
                                                null,
                                                currentUid!!,
                                                null,
                                                link,
                                                null,
                                                null)
//
                                        } }
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsg(currentUid!!, "Image", chatId!!)}}
                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                                changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                        }
                                    )
                                }
                            })
                }}
            }
            else{
                Toast.makeText(requireContext(), "Uploading failed due to image path not found!!", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            try {
                val f = getFilePath(imageUriSecond!!)
                val fi = abc(File(f.toString()))
                lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                    homeViewModel.changeChatShowMsgValue(requireContext(), userUid!!, "2", chatIdd)
                        .observe(requireActivity(),
                            Observer {
                                val t = it as Boolean
                                if (t) {
                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){

                                    homeViewModel.saveImageToFireBase(requireContext(),
                                        "images",
                                        fi.path).observe(
                                        requireActivity(), Observer {
                                            val link = it as String
                                            Log.i("linasnfdjknsa", "pic link is ${link}")
                                            uploadData(3,
                                                chatId!!,
                                                System.currentTimeMillis(),
                                                null,
                                                currentUid!!,
                                                null,
                                                link,
                                                null,
                                                null)
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsg(currentUid!!, "Image", chatId!!)}}
                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                                changeUpdateUnseenCount(currentUid!!, chatId!!)}}

                                        })
                                }}
                                }
                            })
                }}

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_VEDIO && data != null) {
            if (data.data != null) {
                val contentURI: Uri? = data.data!!
                val path="myVedio/"
                val f=getFilePath(contentURI!!)
                Log.i("asfjkbnasjkfdn","path of vedio is ${f}")
                lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                    homeViewModel.changeChatShowMsgValue(requireContext(), userUid!!, "2", chatIdd)
                        .observe(requireActivity(),
                            Observer {
                                val t = it as Boolean
                                if (t) {
                                    lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){

                                    homeViewModel.saveImageToFireBase(requireContext(),
                                        "vedios",
                                        f.toString()).observe(
                                        requireActivity(), Observer {
                                            val link = it as String
                                            Log.i("linasnfdjknsa", "pic link is ${link}")
                                            uploadData(4,
                                                chatId!!,
                                                System.currentTimeMillis(),
                                                null,
                                                currentUid!!,
                                                null,
                                                null,
                                                link,
                                                null)

//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsg(currentUid!!, "Vedio", chatId!!)}}
                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                                changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                        }
                                    )
                                }}
                                }
                            })
                }}
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY && data != null) {
                val contentURI = data!!.data
                val selectedVideoPath = getPath(contentURI)
                Log.d("path", "${selectedVideoPath}")
            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                homeViewModel.changeChatShowMsgValue(requireContext(), userUid!!, "2", chatIdd)
                    .observe(requireActivity(),
                        Observer {
                            val t = it as Boolean
                            if (t) {
                                lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment) {

                                    homeViewModel.saveImageToFireBase(requireContext(),
                                        "vedios",
                                        selectedVideoPath.toString()).observe(
                                        requireActivity(), Observer {
                                            val link = it as String
                                            Log.i("linasnfdjknsa", "pic link is ${link}")
                                            uploadData(4,
                                                chatId!!,
                                                System.currentTimeMillis(),
                                                null,
                                                currentUid!!,
                                                null,
                                                null,
                                                link,
                                                null)
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsgTime(currentUid!!, System.currentTimeMillis(), chatId!!)}}
//                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
//                                                changeChatLastMsg(currentUid!!, "Vedio", chatId!!)}}
                                            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatNowFragment){
                                                changeUpdateUnseenCount(currentUid!!, chatId!!)}}
                                        }
                                    )
                                }}
                            }
                        })
            }}

        }
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO && data != null) {
//                val selectedImageUri: Uri = data.data!!
//            val g=parsePath(selectedImageUri)
//            val f=getFilePath(selectedImageUri)
//            Log.i("asjdfbsabf","gallery vedio path is ${selectedImageUri} with default path ${g} with f ${f}")
//            homeViewModel.saveImageToFireBase(requireContext(),"vedios",selectedImageUri.toString()).observe(
//                requireActivity(), Observer {
//                    val link=it as String
//                    Log.i("linasnfdjknsa","pic link is ${link}")
//                    uploadData(4, chatId!!, System.currentTimeMillis(), null,currentUid!!,null,null,link)
//                }
//            )
//        }
    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        Log.i("asjdfbsabf","inside get function value is ${uri}")
        val cursor: Cursor = requireActivity().contentResolver.query(uri!!, projection, null, null, null)!!
        return if (cursor != null) {
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        }
        else null
    }
    fun abc(file: File):File {
        val imageCompress= ImageZipper(requireContext())
            .setQuality(100)
            .setMaxWidth(700)
            .setMaxHeight(700)
            .compressToFile(file)
        return imageCompress
    }
    private fun getImageFromCameraSecondMethod(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUriSecond = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriSecond)
        startActivityForResult(intent, PICTURE_RESULT)
    }
    private fun openCameraToCaptureVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1)
        startActivityForResult(intent, REQUEST_CODE_VEDIO)
    }
    fun chooseVideoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }
    fun hideKeyboard(activity: Activity) {
        val im = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        im?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
    fun changeChatTyingStatus(userUid:String,value:String,chatId: String){
    lifecycleScope.launchWhenResumed {
        if (findNavController().currentDestination!!.id == R.id.chatNowFragment){
            CoroutineScope(Dispatchers.IO).run {
        firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.typingStatus" to value)).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }}}}

    fun changeUpdateUnseenCount(userUid:String,chatId:String)
{
    lifecycleScope.launchWhenResumed {
        if (findNavController().currentDestination!!.id == R.id.chatNowFragment){
            CoroutineScope(Dispatchers.IO).run {
    firebaseb.collection("Chat").document(chatId).get().addOnSuccessListener {
        if(it.exists()){
            var countt=it.data?.get(userUid)  as HashMap<String, String>
            var count=countt.get("unseen_msg_count")  as Long
            Log.i("safbsaf","return value is ${count}")
            if(count > 0){
                count+=1
                Log.i("safbsaf","inside condition value is ${count} ")
                firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.unseen_msg_count" to count)).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }else{
                firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.unseen_msg_count" to 1)).addOnSuccessListener {

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
        else{
            firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.unseen_msg_count" to 1)).addOnSuccessListener {

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
            }}}
}
    fun emptyUnseenCount(userUid:String,chatId:String){
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.chatNowFragment){
                CoroutineScope(Dispatchers.IO).run {
        firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.unseen_msg_count" to 0)).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }}}}
    }
    fun addMsgField(chatId:String , userList:ChatModel): LiveData<Boolean> {
        val responseData= MutableLiveData<Boolean>()
        var userHashMap=HashMap<String,Any>()
        userHashMap.put("last_Msg",userList)
        firebaseb.collection("Chat").document(chatId).set(userHashMap, SetOptions.merge()).addOnSuccessListener {
            responseData.value=true
            Log.i("asjkdfnsjkadfbn"," save member")
        }.addOnFailureListener {
            // responseData.value=false
        }
        return responseData
    }
}
