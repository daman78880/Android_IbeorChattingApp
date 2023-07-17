package com.ibeor.ibeorchattingapp.modules.editProfile

data class EditProfileUserDataGettingOne(
    var parentTitle:String?=null,
    var userNestedArrayList: ArrayList<EditProfileUserDataGettingTwo>? =null
)

data class EditProfileUserDataGettingTwo(
                        var imgLogo:Int?=null,
                        var txtTitleTemp:String?=null,
                        var txtSubTilte:String?=null,
)

