package com.ibeor.ibeorchattingapp.modules.userData.userDataForInfo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDataInfo(var name:String?=null,
                        var awayLocation:String?=null,
                        var imageLink:ArrayList<String>?=null): Parcelable

@Parcelize
data class EnterUserDataInfo(
                            var user_mobileNumber:String?=null,
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
                            var image_url_list:ArrayList<String>?=null,
                            var endterUserDataInfoTwo: EnterUserDataInfoTwo?=null
    ): Parcelable
@Parcelize
data class EnterUserDataInfoTwo(
    var user_birthday:String?=null,
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
    var user_languages:String?=null,
    var user_select_your_country:String?=null,
    var user_height:String?=null,
    var user_body_type:String?=null,
    var user_exercise:String?=null,
    var user_drink:String?=null,
    var user_smoker:String?=null,
    var user_marijuana:String?=null,
    var user_interests:ArrayList<String>?=null
): Parcelable

@Parcelize
data class FeatchFireBaseAllDataParcelable(
    var user_mobileNumber:String?=null,
    var user_completeStatus:String?=null,
    var user_mobileNumber_code:String?=null,
    var user_email:String?=null,
    var user_name:String?=null,
    var user_gender:String?=null,
    var user_looking_gender:String?=null,
    var user_age:String?=null,
    var user_full_Age:String?=null,
    var user_latitute:Double?=null,
    var user_longitute:Double?=null,
    var selected_first_root:String?=null,
    var selected_second_root:String?=null,
    var user_uid:String?=null,
    var image_url_list:ArrayList<String>?=null,
//    var image_Path_list:ArrayList<String>?=null,
): Parcelable

