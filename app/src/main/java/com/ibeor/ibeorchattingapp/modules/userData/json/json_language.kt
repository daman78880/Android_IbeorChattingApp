package com.ibeor.ibeorchattingapp.modules.userData.json

class json_language : ArrayList<json_languageItem>()
data class json_languageItem(
    val code: String,
    val name: String
)