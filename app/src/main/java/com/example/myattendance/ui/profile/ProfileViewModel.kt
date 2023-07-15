package com.example.myattendance.ui.profile

import android.renderscript.Sampler.Value
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myattendance.datamodel.EmployeeData
import com.example.myattendance.datamodel.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val databaseReference = Firebase.database.reference
    private val firebaseAuth = Firebase.auth
    private lateinit var profileValueListener: ValueEventListener

    private val _profileData = MutableLiveData<User>()
    val profileData: LiveData<User> = _profileData

    init {
        getProfileData()
    }

    fun getProfileData(){
        profileValueListener = databaseReference.child("users/${firebaseAuth.uid}").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    //cara biasa
//                    val username = snapshot.child("username").value as String
//                    val userEmployeeData = snapshot.child("employeeData")
//                    val fullname = userEmployeeData.child("fullname").value as String
//                    val address = userEmployeeData.child("address").value as String
//                    val email = userEmployeeData.child("email").value as String
//                    val employeeID = userEmployeeData.child("employee_id").value as String
//
//                    val employeeDataObj = EmployeeData(employeeID, address, email, fullname)
//                    val userDataObj = User(username,employeeDataObj)
//                    _profileData.value = userDataObj

//                    //cara langsung object
                    val userData = snapshot.getValue(User::class.java)
                    Log.d("userData in profile viewmodel",userData.toString())
                    _profileData.value = userData!!
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("get data profile error","$error")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        databaseReference.removeEventListener(profileValueListener)
    }
}