package com.valenpatel.notes.model

data class UserInfo(val username:String, val userProfileImage:String){
    constructor():this("","")
}
