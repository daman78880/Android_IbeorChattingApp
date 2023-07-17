package com.ibeor.ibeorchattingapp.modules.chatting.chatNow

data class ChatModel(var viewType:Int?=null,
                     var userId:String?=null,
                     var userIdUser:String?=null,
                     var message:String?=null,
                     var time:Long?=null,
                     var audio_Url:String?=null,
                     var imageUrl:String?=null,
                     var vedioUrl:String?=null,
                     var gif_sticker_emoji:String?=null,
                    var readStatus:Boolean=false,
                     var documentId:String=""
                     )