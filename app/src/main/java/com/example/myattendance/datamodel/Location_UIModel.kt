package com.example.myattendance.datamodel

data class Location_UIModel(
    val id: Int,
    val name: String,
    val address: String,
    val image_link: String,
    var is_choosen: Boolean = false
)

fun Location_UIModel.toLocation() = Location(
    id,
    name,
    address,
    image_link
)
