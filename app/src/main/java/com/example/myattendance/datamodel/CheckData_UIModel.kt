package com.example.myattendance.datamodel

data class CheckData_UIModel(
    var id_user: String,
    var id_location: Int,
    var checked: Int = 0,
    var date_checked: String = "",
    var iduser_date: String,
    var location: Location? = null
)
