package com.example.myattendance.datamodel

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class User(

    @PropertyName("username")
    var username: String? = null,
    @PropertyName("employee_data")
    var employeeData: EmployeeData? = null

//    @Exclude
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "username" to username,
//            "fullname" to fullname
//            "password" to password
//        )
//    }
){

}
