package com.example.myattendance.datamodel

//data class CheckData(
//    var id_user: String,
//    var id_location: Int,
//    var date_in: String,
//    var date_out: String = "",
//    var active: Int = 0
//)

data class CheckLogData(
    var id_user: String,
    var location: Location,
    var checked: Int = 0,
    var date_checked: String = "",
//    var timestamp: Long
)
