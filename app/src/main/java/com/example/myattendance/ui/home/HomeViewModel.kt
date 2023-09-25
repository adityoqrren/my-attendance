package com.example.myattendance.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.myattendance.datamodel.CheckData
import com.example.myattendance.datamodel.Location
import com.example.myattendance.datamodel.Location_UIModel
import com.example.myattendance.datamodel.toLocationUIModel
import com.example.myattendance.helper.getTimeNowInUTCString
import com.example.myattendance.helper.mutation
import com.example.myattendance.helper.timeDateNow
import com.example.myattendance.helper.timeHourMinutesNow
import com.example.myattendance.helper.timeNowEpoch
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val databaseReference = Firebase.database.reference
    private val firebaseAuth = Firebase.auth

    private var _locations = MutableLiveData<List<Location_UIModel>>()
    val locations : LiveData<List<Location_UIModel>>  get() = _locations
    private  var _locationSelected : Location? = null
    val locationSelected get() = _locationSelected
    private var _checkState = MutableLiveData<CheckData?>()
    val checkState : MutableLiveData<CheckData?> get() = _checkState

    private var _timeNow = MutableLiveData<String>("00:00")
    val timeNow : LiveData<String> get() = _timeNow

    private var _dateNow = MutableLiveData<String>("8 Mei 2023")
    val dateNow : LiveData<String> get() = _dateNow

    init {
        getCheckState()
        updateDateTime()
    }

//    fun getLocFromFirebase(){
//        viewModelScope.launch {
//            val myLocsQuery = databaseReference.child("locations")
//            myLocsQuery.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val locationList = mutableListOf<Location_UIModel>()
//                    if(checkState.value==null) {
//                        for (postSnapshot in dataSnapshot.children) {
////                    val generated_id = postSnapshot.key
//                            val id = postSnapshot.child("id").value as Long
//                            val name = postSnapshot.child("name").value as String
//                            val address = postSnapshot.child("address").value as String
//                            val imageLink = postSnapshot.child("image_link").value as String
//                            val locationModel =
//                                Location_UIModel(id.toInt(), name, address, imageLink)
//                            Log.d("result locationModel", "${locationModel}")
//                            locationList.add(locationModel)
//                            Log.d("result locationList size", "${locationList.size}")
//                        }
//                    }
//                    locations.value = locationList
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.w("location firebase: ", "loadPost:onCancelled", error.toException())
//                }
//            })
//        }
//    }

    fun setSelectedLocation(locationSelected: Location){
        _locationSelected = locationSelected
        _locations.mutation {
            it.value?.map { loc ->
                if(loc.id == this.locationSelected?.id){
                    loc.is_choosen = true
                }else{
                    loc.is_choosen = false
                }
            }
        }
    }

    //indicator check true when check-in, false when check-out
    fun setLocationWhenStateChanged(indicatorCheck: Boolean){
        val myLocsQuery = databaseReference.child("locations")
        if(indicatorCheck){
            _locationSelected?.let {
                val listLocation = mutableListOf<Location_UIModel>(it.toLocationUIModel())
                _locations.value = listLocation
            }
        }else{
            myLocsQuery.get().addOnSuccessListener {
                val children = it.children
                val listLocation = mutableListOf<Location_UIModel>()
                for(loc in children){
                    val id_location = loc.child("id").value as Long
                    val name = loc.child("name").value as String
                    val address = loc.child("address").value as String
                    val imageLink = loc.child("image_link").value as String
                    val locationModel = Location_UIModel(id_location.toInt(), name, address, imageLink)
                    listLocation.add(locationModel)
                }
                _locations.value = listLocation
            }
            .addOnFailureListener {
                Log.d("HomeViewModel location error","${it.stackTrace}")
            }
        }
    }

    fun getCheckState(){
        viewModelScope.launch {
            val myCheckState = databaseReference.child("check_state")
            myCheckState.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("getCheckState","getCheckState triggered")
                    if(snapshot.exists()){
                        if(snapshot.child("id_user").exists()){
                            val id_user = snapshot.child("id_user").value as String
                            val checked = snapshot.child("checked").value as Long
                            val date_checked = snapshot.child("date_checked").value as String
                            val iduser_date = snapshot.child("iduser_date").value as String
                            //location
                            val location = snapshot.child("location")
                            val id_location = location.child("id").value as Long
                            val name_location = location.child("name").value as String
                            val address_location = location.child("address").value as String
                            val image_link_location = location.child("image_link").value as String
                            val locationObj = Location(id_location.toInt(),name_location, address_location, image_link_location)
                            val checkData = CheckData(id_user, locationObj , checked.toInt(), date_checked, iduser_date)
                            _checkState.value = checkData
                            _locationSelected = locationObj

                            //after we set checkState value, we must update locations to have only 1 location that active based on check_state in firebase db
                            setLocationWhenStateChanged(true)
                        }
                    }else{
                        _checkState.value = null
                        //call this to fetch all locations
                        setLocationWhenStateChanged(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("checkState firebase: ", "loadPost:onCancelled", error.toException())
                }
            })
        }
    }

    fun setCheckState(){
        val myCheckState = databaseReference.child("check_state")
        val myCheckData = databaseReference.child("check_data")
        val key = myCheckData.push()
        if(checkState.value!=null){
            //CHECK OUT
            //add data check out to check_data
            val checkDataCheckOut = checkState.value?.let {
                //CheckLogData(it.id_user, locationSelected!!, 1, getTimeNow())
                CheckData(it.id_user,it.location,1, getTimeNowInUTCString(),"${firebaseAuth.uid}_$timeNowEpoch")
            }
            key.setValue(checkDataCheckOut)
                .addOnCompleteListener {
                    myCheckState.removeValue()
                    Log.d("setCheckState check-out","success add check_data")
                }
                .addOnFailureListener {
                    Log.d("setCheckState check-out","failed add check_data")
                }
        }else{
            //CHECK IN
            //add data check-in to check_state then save to check_data
            val checkStateCheckIn = CheckData(firebaseAuth.uid as String, locationSelected as Location, 0, getTimeNowInUTCString(),"${firebaseAuth.uid}_$timeNowEpoch")
            myCheckState.setValue(checkStateCheckIn)
                .addOnCompleteListener {
                    Log.d("setCheckState check-in","success add check_state")
                    //add log-in data to check_data
                    key.setValue(checkStateCheckIn)
                }
                .addOnFailureListener {
                    Log.d("setCheckState check-in","failed add check_state")
                }
            //Log.d("setCheckState check_state","${checkState.value}")
        }
    }

    fun updateDateTime(){
        viewModelScope.launch {
            while (isActive){
                _timeNow.value = timeHourMinutesNow()
                _dateNow.value = timeDateNow()
                //Log.d("check HomeViewModel getTime()","update time ; ${timeHourMinutesNow()}")
                delay(1000)//update every 1 second
            }
        }
    }
}