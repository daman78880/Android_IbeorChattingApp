package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

class CountryDataModel : ArrayList<countryItem>()
data class countryItem(
    var name: String="",
    var code: String="",
    var phone_code: String="",
    var flag: String=""
)