package com.ibeor.ibeorchattingapp.modules.userData.uploadToFirebaseDataBase

data class UserFirebaseAboutData(
    var user_birthday:String?=null,
    var user_gender:String?=null,
    var user_about_me:String?=null,
    var user_current_work:String?=null,
    var user_school:String?=null,
    var user_city_live:String?=null,
    var user_home_town:String?=null,
    var user_looking_for_person_type:ArrayList<String>?=null,
    var user_pets:ArrayList<String>?=null,
    var user_children:String?=null,
    var user_astrological_sign:String?=null,
    var user_religion:String?=null,
    var user_education:String?=null,
    var user_language:ArrayList<String>?=null,
    var user_select_your_country:ArrayList<String>?=null,
    var user_height:String?=null,
    var user_body_type:String?=null,
    var user_exercise:String?=null,
    var user_drink:String?=null,
    var user_smoker:String?=null,
    var user_marijuana:String?=null,
    var user_interests:ArrayList<String>?=null
)
