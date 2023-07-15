package com.example.myattendance.datamodel

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName


@IgnoreExtraProperties
data class EmployeeData(
    @set:PropertyName("employee_id")
    @get:PropertyName("employee_id")
    var employeeID : String? = null,
    val address : String? = null,
    val email : String? = null,
    val fullname: String? = null,
    val position: String? = null
)
