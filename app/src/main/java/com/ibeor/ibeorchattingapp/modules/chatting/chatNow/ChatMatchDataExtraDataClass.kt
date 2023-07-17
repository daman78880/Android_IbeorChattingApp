package com.ibeor.ibeorchattingapp.modules.chatting.chatNow

data class ChatMatchDataExtraDataClass (
            var chatId:String?=null,
            var userOneMap:HashMap<String,Any>?=null,
            var userUid:String?=null
        )
data class ArrayOfChatMatchDataExtraDataClass(
 var ChatData:ArrayList<ChatMatchDataExtraDataClass>?=null
)