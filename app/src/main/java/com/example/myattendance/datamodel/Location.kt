package com.example.myattendance.datamodel

data class Location(
    val id: Int,
    val name: String,
    val address: String,
    val image_link: String
)

fun Location.toLocationUIModel() = Location_UIModel(
    id,
    name,
    address,
    image_link,
    true
)

