package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.developers.imagezipper.ImageZipper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentAddImageBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AddImageFragment : Fragment() {
    private var binding: FragmentAddImageBinding? = null
    private var list = ArrayList<String>()
    var adapter : AddImageAdapter?=null
    private var RESULT_LOAD_IMAGE=100
    private var galleryFile:File?=null
    private var clickPostion:Int?=null
    private var cameraImagePickUri:Uri?=null
    private var PICTURE_RESULT=112
    private var imageUriSecond:Uri?=null
    private var imageUrl:String ?=null
    private var userImageList = ArrayList<String>()
    private var imageUploadCount:Int=0
    private var tempImageUploadCount:Int=0
    var auth: FirebaseAuth?=null
    private var storageReference: StorageReference?=null
    var     currentUser : FirebaseUser?=null
    private var replacePosition:Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddImageBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onClickListner()
    }
    private fun init(){
        binding!!.apply {
            addList()
            auth = Firebase.auth
            currentUser = auth!!.currentUser
            storageReference = FirebaseStorage.getInstance().reference
            adapter = AddImageAdapter(requireContext(), list,0,object : AddImageAdapter.Clicks{
                override fun onAddImage(position: Int, viewId: Int) {
                    when(viewId){
                        R.id.imgAddImgDD ->{
                            showSheetDialog(requireContext(),position)
                        }
                        R.id.imgUserImgDD ->{
                            replacePosition=position
                            showSheetDialog(requireContext(),position)
                        }
                    }
                }
                override fun onDelteImage(position: Int, viewId: Int) {
                    when(viewId){
                        R.id.imgDeleteImgDD ->{
                            setDeleteDialog(position)
                        }
                    }
                }
            })
//            rvAddPhoto.adapter = adapter
            includeAddImageLayout.rvAddPhotoComon.adapter=adapter
        }
    }
    private fun onClickListner(){
        binding!!.apply {
            imBackBtnAddImg.setOnClickListener {
                findNavController().popBackStack()
            }
            btnPhotoSelected.setOnClickListener {
                clickPerform()
            }
        }
    }
private fun clickPerform(){
    var check=1
    for(i in 0..1){
        if(list[i] != "") {
            check +=1
        }
    }
    if(check >= 3){
        for(i in 0.until(list.size)){
            if (list[i] != ""){
                imageUploadCount+=1
            }
        }
        Log.i("testingNowq","imagecount $imageUploadCount and list $list")
            uploadImageOnFirebaseStorage()

    }
    else{
        Toast.makeText(requireContext(), "Please select at least two images", Toast.LENGTH_SHORT).show()
    }
}
    private fun requestPermssionn(position:Int,permission:String?,per:ArrayList<String>?,value:Int){
        if(permission!=null) {
            Dexter.withActivity(activity).withPermissions(permission)
        }
        else{
            Dexter.withActivity(activity).withPermissions(per)
        }
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        clickPostion=position
                        if(value == 0){
                        getImageFormGallery()
                        }
                        else if(value == 1){
                            getImageFromCameraSecondMethod()
                        }
                        else{
                            Log.i("checkNowq","else inside request permission")
                        }
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        Log.i("checkNowq","inside denid")
                        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            showSettingsDialog()
                        }
                        else{
                            clickPostion=position
                            if(value == 0){
                                getImageFormGallery()
                            }
                            else{
                                getImageFromCameraSecondMethod()
                            }
                        }
                        if(value == 0 ){

                        }
                        else{

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
    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Camera Permissions Required")
        builder.setMessage("You have forcefully denied some of the required permissions for this action. Please open setting, go to permissions and allow them.")
        builder.setPositiveButton("SETTINGS") { dialog: DialogInterface, which: Int ->
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
    private fun setDeleteDialog(a:Int){
        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.delete_image_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val deleteTxt:AppCompatTextView=dialog.findViewById(R.id.delelteBtnDeleteImage)
        val cancelTxt:AppCompatTextView=dialog.findViewById(R.id.cancelBtnDeleteImage)
        deleteTxt.setOnClickListener {
            removeImage(a)
            dialog.dismiss()
        }
        cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun removeImage(position:Int){
        list.removeAt(position)
        list.add("")
        adapter!!.updateList(list)
//        binding!!.rvAddPhoto.adapter=adapter
        binding!!.includeAddImageLayout.rvAddPhotoComon.adapter=adapter
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
    private fun addList() {
        for (i in 0..5) {
            list.add("")
        }
    }
    private fun showSheetDialog(context: Context,position: Int){
        val dialog=BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.image_pic_dialog, null)
        dialog.setContentView(view)
        val galleryImage:AppCompatImageView=view.findViewById(R.id.dialogGalleryPick)
        val cameraImage:AppCompatImageView=view.findViewById(R.id.dialogCameraPick)
        galleryImage.setOnClickListener {
            val per=Manifest.permission.READ_EXTERNAL_STORAGE
            requestPermssionn(position,per,null,0)
            dialog.dismiss()
        }
        cameraImage.setOnClickListener {
            Log.i("checkNowq","ON camera click")
            val per=Manifest.permission.CAMERA
            val per2=Manifest.permission.WRITE_EXTERNAL_STORAGE
            val per3=ArrayList<String>()
            per3.add(per)
            per3.add(per2)
            requestPermssionn(position,null,per3,1)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getImageFormGallery(){
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK &&  data != null ) {
            val selectedImage: Uri = data.data!!
            val path:String?=getFilePath(selectedImage)
            if(path!= null){
            galleryFile=imageCompressor(File(path.toString()))
            addDataToList(galleryFile!!.path)
            }
            else{
                Toast.makeText(requireContext(), "Uploading failed due to image path not found!!", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK ) {
            try {
                val thumbnail = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUriSecond)
//                imageUrl = getRealPathFromURI(imageUriSecond)
//                addDataToList(imageUrl!!)
                val f=getFilePath(imageUriSecond!!)
                val fi=imageCompressor(File(f.toString()))
//                imageUrl = getRealPathFromURI(imageUriSecond)
                addDataToList(fi.path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        }
    private fun addDataToList(path:String){
        if (replacePosition != null){
            list[replacePosition!!] = path
            adapter!!.updateList(list)
            binding!!.includeAddImageLayout.rvAddPhotoComon.adapter = adapter
            clickPostion = null
            replacePosition=null
        }
        else {
            for (i in 0.until(list.size)) {
                if (list[i] == "") {
                    list[i] = path
                    adapter!!.updateList(list)
                    binding!!.includeAddImageLayout.rvAddPhotoComon.adapter = adapter
                    clickPostion = null
                    break
                }
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
    private fun getImageFromCameraSecondMethod(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUriSecond = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriSecond)
        startActivityForResult(intent, PICTURE_RESULT)
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = activity?.managedQuery(contentUri, proj, null, null, null)!!
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
    private fun uploadImageOnFirebaseStorage(){
        for(i in  0.until(imageUploadCount)){

            Log.i("testingNowq","inside loop count $i")
        val progressdialog = ProgressDialog(requireContext())
        progressdialog.setTitle("Uploading..")
        progressdialog.show()
            val uidd=Firebase.auth.uid
        Log.i("testingNowq","inside upload count ")
            val name=MyUtils.getString(requireContext(),MyUtils.userNameKey)?:uidd
             storageReference?.child("images")!!.child(uidd.toString()).child(name+"_"+UUID.randomUUID().toString()).putFile(Uri.fromFile( File(list[i])))
            .addOnSuccessListener {
                tempImageUploadCount+=1
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val uploadedUrlp = it.toString()
                    userImageList.add(uploadedUrlp)
                    if(imageUploadCount == tempImageUploadCount) {
                        Log.i("testingNowq", " user image list -> $userImageList")
                        lifecycleScope.launchWhenResumed {
                        progressdialog.dismiss()
                            Log.i("123456789","array put sucess $userImageList")
                         MyUtils.putArray(requireContext(),MyUtils.userArrayImageLinkKey,userImageList)

                            val data2=HashMap<String,Any>()
                            data2.put("image_url_list", userImageList.toList() )
                            data2.put("user_completeStatus","3")
                            HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                                androidx.lifecycle.Observer {
                                    val flag=it as Boolean
                                    lifecycleScope.launchWhenResumed {
                                        if (flag) {
                                            if (findNavController().currentDestination?.id == R.id.addImageFragment) {
                                                findNavController().navigate(R.id.action_addImageFragment_to_gettingLocationFragment2)
                                            }
                                        }
                                    }
                                })

                    }
                        }
                }
//                Toast.makeText(requireContext(), "Uploaded successful", Toast.LENGTH_SHORT).show()
                progressdialog.dismiss()
            }.addOnFailureListener {
                progressdialog.dismiss()
                Log.i("testingNow","error ${it.message}")
                Toast.makeText(requireContext(), "Failed ${it.message}", Toast.LENGTH_SHORT).show()
            }.addOnProgressListener { snapshot ->
                val process = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount)
                progressdialog.setMessage("Uploaded ${process.toInt()}% ")
            }
        }
    }
    fun imageCompressor(file: File):File {
        val imageCompress= ImageZipper(requireContext())
            .setQuality(100)
            .setMaxWidth(700)
            .setMaxHeight(700)
            .compressToFile(file)
        return imageCompress
    }
}