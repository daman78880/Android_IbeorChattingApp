package com.ibeor.ibeorchattingapp.modules.chatting

import android.os.Parcelable
import com.ibeor.ibeorchattingapp.modules.userData.userDataForInfo.EnterUserDataInfoTwo
import kotlinx.android.parcel.Parcelize
@Parcelize
data class UserLikedCheckDetail(
    var liked_id:String?=null,
    var liked_user_name:String?=null,
    var liked_user_age:String?=null,
    var liked_user_img:String?=null,
    var user_msg_status:String?=null
): Parcelable

