package com.ibeor.ibeorchattingapp.modules.chatting

data class MatchUserExtraData(
    var login_name:String?=null,
    var login_uid:String?=null,
    var login_image:String?=null,
    var login_user_view:String?=null,

//    var login_msg:String="",
    var msg:String="",
    var time:Long=0L,
    var lastType:Long=0,
//    var login_time:Long=0L,
    var login_unseenMsg_count:Long=0L,

    var user_Image:String?=null,
    var user_name:String?=null,
    var user_uid:String?=null,
    var user_view:String?=null,
    var chatId:String?=null,
    var messagePresent:Boolean=false,
    var login_Typing_Status:String="false",
    var user_Typing_Status:String="false",

//    var user_msg:String="",
//    var user_time:Long=0L,
    var user_unseenMsg_coung:Long=0L

)