package com.ibeor.ibeorchattingapp.modules.userData

data class FeatchFireBaseRegisterData(
    var user_mobileNumber:String?=null,
    var user_completeStatus:String?=null,
    var user_mobileNumber_code:String?=null,
    var user_email:String?=null,
    var user_name:String?=null,
    var user_gender:String?=null,
    var user_looking_gender:String?=null,
    var user_age:String?=null,
    var user_full_Age:String?=null,
    var user_latitute:String?=null,
    var user_longitute:String?=null,
    var selected_first_root:String?=null,
    var selected_second_root:String?=null,
    var user_uid:String?=null,
    var image_url_list:ArrayList<String>?=null
//    var image_Path_list:ArrayList<String>?=null,
)

