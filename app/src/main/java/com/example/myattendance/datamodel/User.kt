package com.example.myattendance.datamodel

data class User(
    var username: String = "",
    var fullname: String = "",

//    @Exclude
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "username" to username,
//            "fullname" to fullname
//            "password" to password
//        )
//    }
)
