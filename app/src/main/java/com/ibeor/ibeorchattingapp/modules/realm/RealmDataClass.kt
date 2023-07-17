package com.ibeor.ibeorchattingapp.modules.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class  RealmDataClass: RealmObject() {
    @PrimaryKey
    var user_uidd:String?=null
    var user_mobileNumberr:String?=null
    var user_completeStatuss:String?=null
    var user_mobileNumber_codee:String?=null
    var user_emaill:String?=null
    var user_namee:String?=null
    var user_genderr:String?=null
    var user_looking_genderr:String?=null
    var user_agee:String?=null
    var user_full_Agee:String?=null
    var user_latitutee:String?=null
    var user_longitutee:String?=null
    var selected_first_roott:String?=null
    var selected_second_roott:String?=null
//    var image_url_listt:ArrayList<String>?=null
    var image_url_listt : RealmList<String>?=null
}

