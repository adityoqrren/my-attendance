package com.example.myattendance


import com.example.myattendance.datamodel.Location

interface OnSelectLocationCallback{
    fun onSelectLocation(location: Location)
}