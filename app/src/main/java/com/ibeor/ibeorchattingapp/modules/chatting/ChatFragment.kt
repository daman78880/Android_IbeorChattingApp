package com.ibeor.ibeorchattingapp.modules.chatting

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentChatBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.chatting.chatNow.ChatModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.security.auth.login.LoginException
import kotlin.collections.ArrayList
import kotlin.math.log


class ChatFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private var binding:FragmentChatBinding?=null
    private var userImage:String?=null
    private var bundle:Bundle?=null
    private var whoLikedMeList:ArrayList<UserLikedCheckDetail>?=null
    private var matchUserList:ArrayList<UserLikedCheckDetail>?=null
    private var loginUserUid:String=""
    lateinit var firebaseb :FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding=FragmentChatBinding.inflate(layoutInflater)
        firebaseb = FirebaseFirestore.getInstance()
        viewModel=  HomeViewModel()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        binding!!.apply {
            MyUtils.showProgress(requireContext())
            loginUserUid = MyUtils.getString(requireContext(), MyUtils.userUidKey).toString()
            userImage = arguments?.getString("img")
            imgHomeChat.clipToOutline=true

            if (userImage != null) {
                Glide.with(requireContext()).load(userImage).into(imgHomeChat)
                Glide.with(requireContext()).load(userImage).into(checkLikeImgChat)
            }
            clickListner()

            val allUserList = ArrayList<String>()
            var likeIndex = 0
            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.chatFragment){
            firebaseb.collection("Like").addSnapshotListener { value, error ->
                if (value?.isEmpty!!) {
                    MyUtils.stopProgress()
                } else {
                    Log.i("asdfbsaf", "Not empty user Like")
                    val len = value.documents.size
                    Log.i("asdfbsaf", "len size is ${len}")
                    for (i in 0 until len) {
                        if (value.documents[i].id != loginUserUid) {
                            likeIndex += 1
                            allUserList.add(value.documents[i].id)
                            Log.i("asdfbsaf", "adasas ${allUserList}")
                            if (likeIndex == len) {
                                Log.i("asdfbsaf", "inside if who like")
                                lifecycleScope.launchWhenResumed {
                                    if (findNavController().currentDestination!!.id == R.id.chatFragment) {
                                        viewModel.checkWhoLikedMe(requireContext(), allUserList)
                                            .observe(requireActivity(), Observer {
                                                whoLikedMeList = it as ArrayList<UserLikedCheckDetail>
                                                Log.i("asdfbsaf",
                                                    "return  who like ${whoLikedMeList}")

                                                if (whoLikedMeList!!.size == 0) {
                                                    MyUtils.stopProgress()
//                                                    gettingAllLikedUsers()
                                                    }

                                                if (whoLikedMeList!!.size >0){
                                                    binding!!.countLikeImgChat.text=whoLikedMeList!!.size.toString()
                                                }
                                                else{
                                                    binding!!.countLikeImgChat.text="0"
                                                }
                                                setBundleData(whoLikedMeList!!)

                                            })
                                    }
                                }
                            }
                        } else {
                            likeIndex += 1
                            if (likeIndex == len) {
                                lifecycleScope.launchWhenResumed {

                                    if (findNavController().currentDestination!!.id == R.id.chatFragment) {
                                        Log.i("asdfbsaf", "inside else who like")
                                        viewModel.checkWhoLikedMe(requireContext(), allUserList)
                                            .observe(requireActivity(), Observer {
                                                whoLikedMeList =
                                                    it as ArrayList<UserLikedCheckDetail>
                                                Log.i("asdfbsaf", "return else who like")
//                                                if (whoLikedMeList!!.size == 0) {
//                                                    Log.i("asdfbsjafbd",
//                                                        "return else who like size 0")
//                                                    MyUtils.stopProgress()
////                                                    gettingAllLikedUsers()
//                                                } else {
//                                                    Log.i("asdfbsjafbd",
//                                                        "return else who like size 1")
////                                                    gettingAllLikedUsers()
//                                                }
                                                if (whoLikedMeList!!.size == 0) {
                                                    MyUtils.stopProgress()
//                                                    gettingAllLikedUsers()
                                                }

                                                if (whoLikedMeList!!.size >0){
                                                    binding!!.countLikeImgChat.text=whoLikedMeList!!.size.toString()
                                                }
                                                else{
                                                    binding!!.countLikeImgChat.text="0"
                                                }
                                                setBundleData(whoLikedMeList!!)
                                            })
                                    }
                                }
//
                            }
                        }
                    }
                }
            }
        }}



//            viewModel.allLikedUsers(requireContext()).observe(requireActivity(),
//                Observer {
//                    val lis=it as ArrayList<String>
//                        viewModel.checkWhoLikedMe(requireContext(),lis).observe(requireActivity(),
//                            Observer {
//                                whoLikedMeList=it as ArrayList<UserLikedCheckDetail>
//                                if(whoLikedMeList!!.size == 0){
//                                    MyUtils.stopProgress()
//                                }
//                                gettingAllLikedUsers()
//                            })
//
//                })
            gettingAllLikedUsers()
//            typingStatusChecking()
        }
    }

    private fun setBundleData(list:ArrayList<UserLikedCheckDetail>){
        bundle=Bundle()
        bundle?.putParcelableArrayList("userList",list)
    }
    private fun clickListner(){
        binding!!.apply {

            checkLikeImgChat.setOnClickListener {
            lifecycleScope.launchWhenResumed {
            findNavController().navigate(R.id.action_chatFragment_to_likeMeFragment,bundle)
            }
            }
            countLikeImgChat.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_chatFragment_to_likeMeFragment,bundle)
                }
            }
            imgLikeChat.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                findNavController().navigate(R.id.action_chatFragment_to_likeMeFragment,bundle)
                }
            }
            imgShuffelChat.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                findNavController().popBackStack()
            }
            }
            imgHomeChat.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    findNavController().navigate(R.id.action_chatFragment_to_profileFragment)
                }
            }
        }
    }


     fun gettingAllLikedUsers() {
         CoroutineScope(Dispatchers.Main).run {
             var index = 0
             val tempResponse = ArrayList<MatchUserExtraData>()
//         CoroutineScope.launch {  }
             lifecycleScope.launchWhenResumed {
                 if (findNavController().currentDestination!!.id == R.id.chatFragment) {
             CoroutineScope(Dispatchers.IO).run {
                     firebaseb.collection("Chat").whereArrayContains("memberData", loginUserUid)
                         .addSnapshotListener { value, error ->
                             if (value?.isEmpty!!) {
                                 MyUtils.stopProgress()
                             } else {
                                 val len = value.documents.size
                                 tempResponse.clear()
                                 Log.i("asjfxfgfddhfsdsafjdbn", " value ${value.documents.size}")
                                 Log.i("asjfxfgfddhfsdsafjdbnn", " value ${value.documents} ")
                                 for (i in 0 until len) {
                                     val dataList = MatchUserExtraData()
                                     Log.i("asjfhsafjdbn",
                                         "document ${value.documents.get(i).data}")
//                          val userMsg=value.documents.get(i).data!!.get("last_Msg") as ChatModel
                                     var last_msg_model: ChatModel? = null
                                     if (value.documents.get(i).data!!.get("last_Msg") != null) {
                                         val t = value.documents.get(i).data!!.get("last_Msg") as HashMap<String, Any>
                                         dataList.messagePresent = true
                                         dataList.msg = t.get("message").toString()
                                         dataList.time = t.get("time") as Long
                                         dataList.lastType = t.get("viewType") as Long
//                              dataList.login_msg=t.get("message").toString()
                                         Log.i("asbsnjabdvdfs", "t is ${t}")
                                     } else {
                                         dataList.messagePresent = false
                                     }

                                     val usersList =
                                         value.documents.get(i).data!!.get("memberData") as ArrayList<String>
                                     Log.i("asdfbsjafbd", "userlist ${usersList}")
                                     var otherUserIdd = ""
                                     var loginUserIdd = ""
                                     if (usersList[0].equals(loginUserUid)) {
                                         otherUserIdd = usersList[1]
                                         loginUserIdd = usersList[0]
                                     } else {
                                         otherUserIdd = usersList[0]
                                         loginUserIdd = usersList[1]
                                     }
                                     val userDetail =
                                         value.documents.get(i).data!![otherUserIdd] as HashMap<String, String>
                                     dataList.user_uid = otherUserIdd
                                     dataList.user_name = userDetail.get("name")
                                     dataList.user_Image = userDetail.get("photo")
                                     dataList.user_view = userDetail.get("userView")
                                     dataList.chatId = userDetail.get("chatId")
                                     dataList.user_Typing_Status =
                                         userDetail.get("typingStatus") ?: "false"
//                          dataList.msg=userDetail.get("last_msg") ?:""// changeifneed
//                          dataList.user_time=userDetail.get("time") as Long ?:0// changeifneed
                                     dataList.user_unseenMsg_coung =
                                         userDetail.get("unseen_msg_count") as Long
                                             ?: 0// changeifneed
                                     val loginUserDetail =
                                         value.documents.get(i).data!!.get(loginUserIdd) as HashMap<String, String>
                                     dataList.login_uid = loginUserIdd
                                     dataList.login_name = loginUserDetail.get("name")
                                     dataList.login_image = loginUserDetail.get("photo")
                                     dataList.login_user_view = loginUserDetail.get("userView")
                                     dataList.chatId = loginUserDetail.get("chatId")
                                     dataList.login_Typing_Status =
                                         loginUserDetail.get("typingStatus") ?: "false"
                                     dataList.login_unseenMsg_count =
                                         loginUserDetail.get("unseen_msg_count") as Long
                                             ?: 0// changeifneed
                                     index += 1
                                     tempResponse.add(dataList)
                                     if (index == len) {
                                         Log.i("asdfbsandf",
                                             "setting adapter inside if  msg len ${len} and index  ${index} ")
                                         index = 0
                                         runnExtraFeature = true
                                         setLikeAdapter(tempResponse)
                                     }

                                 }
                             }
                         }
                 }
             }
         }

         if(runnExtraFeature == true){
             if(tempResponse.size > 1){
                 for(i in 0 until tempResponse.size){
                     firebaseb.collection("Chat").document(tempResponse[i].chatId!!).addSnapshotListener { value, error ->
                         if(value!!.exists()){
//                             val chatingStatus=value.data.get()
                             Log.i("sjfndfs","value ${value.data!!.get(tempResponse[i].user_Typing_Status)}")


                         }else{

                         }
                     }
                 }
             }
         }
     }}
    private fun setLikeAdapter(tempResponse: ArrayList<MatchUserExtraData>) {
        binding!!.apply {

        val likeUserList=ArrayList<MatchUserExtraData>()
        val matchUserList=ArrayList<MatchUserExtraData>()
        for(i in 0 until tempResponse.size){
            if(tempResponse[i].messagePresent == false){
                likeUserList.add(tempResponse[i])
            }
            else{
                matchUserList.add(tempResponse[i])
            }
        }

        lifecycleScope.launchWhenResumed {
            likeUserList.sortBy {
                it.time
            }
            Log.i("asjdfbsdf","match horizontal list ${likeUserList.size}")
                                    if(findNavController().currentDestination!!.id == R.id.chatFragment){
                                        val adapterr=MatchUserAdatper(requireContext(),likeUserList,object:MatchUserAdatper.Clisks{
                                            override fun onImageClick(position: Int, id: Int) {
                                                val bundle=Bundle()
                                                bundle.putString("chatId",likeUserList[position].chatId)
                                                bundle.putString("login_user_view",likeUserList[position].login_user_view)
                                                bundle.putString("uid", likeUserList[position].user_uid)
                                                bundle.putString("name", likeUserList[position].user_name)
                                                bundle.putString("img", likeUserList[position].user_Image)
                                                bundle.putString("typingStatus", likeUserList[position].user_Typing_Status)
                                                findNavController().navigate(R.id.action_chatFragment_to_chatNowFragment,bundle)
                                            }

                                            override fun onTextClick(position: Int, id: Int) {
                                            }

                                        })
                                        rvLikesUserChat.adapter=adapterr
                                    }}

        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.chatFragment){
                Log.i("sdfbjsnfbssfb","setting adapter")
                matchUserList.sortBy {
                    it.time
                }
                Log.i("asjdfbsdf","match vertical list ${matchUserList.size}")
                val adapterVertical=MatchUserVerticalAdatper(requireContext(),matchUserList,object :MatchUserVerticalAdatper.CClicks{
                    override fun onViewClick(position: Int) {
                        val bundle=Bundle()
                        bundle.putString("uid", matchUserList[position].user_uid)
                        bundle.putString("name", matchUserList[position].user_name)
                        bundle.putString("img", matchUserList[position].user_Image)
                        bundle.putString("chatId",tempResponse[position].chatId)
                        bundle.putString("login_user_view",tempResponse[position].login_user_view)
                        bundle.putString("typingStatus", tempResponse[position].user_Typing_Status)

                        findNavController().navigate(R.id.action_chatFragment_to_chatNowFragment,bundle)
                    }
                })
                rvChatingShowChat.layoutManager=LinearLayoutManager(requireContext())
                rvChatingShowChat.adapter=adapterVertical
                if(matchUserList.size > 0){
                    tempSendMsgLayout.visibility=View.GONE
                }

                if(matchUserList.size == 0 || likeUserList.size == 0){
                    MyUtils.stopProgress()
                }
                MyUtils.stopProgress()
            }}


    }
    }

    companion object{
var runnExtraFeature=false
    }
}

