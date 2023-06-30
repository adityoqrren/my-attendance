package com.example.myattendance.ui.history

import android.util.Log
import androidx.lifecycle.*
import com.example.myattendance.datamodel.CheckData
import com.example.myattendance.datamodel.Location
import com.example.myattendance.helper.Filter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class HistoryViewModel : ViewModel() {

    private val _filterSelected = MutableLiveData<Filter>()
    val filterSelected: LiveData<Filter> get() = _filterSelected

    private val databaseReference = Firebase.database.reference
    private val firebaseAuth = Firebase.auth

    private lateinit var query: Query
    private var valueEventListener: ValueEventListener? = null

    private val _logCheckHistory1 = MutableLiveData<List<CheckData>>()
    val logCheckHistory1: LiveData<List<CheckData>> get() = _logCheckHistory1

    init {
        setFilter(Filter.DAY)
    }

    fun setFilter(filter: Filter) {
        Log.d("log data", "setFilter() is called ${filter.name}")
        _filterSelected.value = filter
        getLogCheckHistory1()
    }

    fun getLogCheckHistory1(){
        val startDate = when(_filterSelected.value){
            Filter.DAY -> {
                LocalDateTime.now().minusDays(1)
            }
            Filter.WEEK -> {
                LocalDateTime.now().minusWeeks(1)
            }
            Filter.MONTH -> {
                LocalDateTime.now().minusMonths(1)
            }
            else -> {
                LocalDateTime.now().minusYears(1)
            }
        }

        val endDate = LocalDateTime.now()
        val zoneId: ZoneId = ZoneId.of("Asia/Jakarta")

        Log.d(
            "log start date",
            "${firebaseAuth.uid}_${startDate.toInstant(ZoneOffset.UTC).toEpochMilli()}"
        )
        Log.d(
            "log end date",
            "${firebaseAuth.uid}_${endDate.toInstant(ZoneOffset.UTC).toEpochMilli()}"
        )
        Log.d(
            "log start date utc",
            "${startDate.toInstant(ZoneOffset.UTC)}"
        )
        Log.d(
            "log end date utc",
            "${endDate.toInstant(ZoneOffset.UTC)}"
        )

        valueEventListener?.let {
            Log.d("valueEventListener","valueEventListener removed")
            query.removeEventListener(it)
            valueEventListener = null
        }

        query = databaseReference.child("check_data")
            .orderByChild("iduser_date")
            .startAt("${firebaseAuth.uid}_${startDate.toInstant(ZoneOffset.UTC).toEpochMilli()}")
            .endAt("${firebaseAuth.uid}_${endDate.toInstant(ZoneOffset.UTC).toEpochMilli()}")

        valueEventListener = query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val checkHistoryList = mutableListOf<CheckData>()
                Log.d("log query", "LOG QUERY SUCCESS $snapshot")

                if(snapshot.exists() && snapshot.hasChildren()){
                    val checkHistoryData = snapshot.children

                    checkHistoryData.forEachIndexed { index, it ->
                        val id_user = it.child("id_user").value as String
                        val checked = it.child("checked").value as Long
                        val date_checked = it.child("date_checked").value as String
                        val iduser_date = it.child("iduser_date").value as String
                        //location
                        val location = it.child("location")
                        val id_location = location.child("id").value as Long
                        val name_location = location.child("name").value as String
                        val address_location = location.child("address").value as String
                        val image_link_location = location.child("image_link").value as String
                        val locationObj = Location(id_location.toInt(),name_location, address_location, image_link_location)
                        val checkData = CheckData(id_user, locationObj , checked.toInt(), date_checked, iduser_date)
                        checkHistoryList.add(checkData)
                    }

                    Log.d("log history list", "outer $checkHistoryList")

                }else{
                    checkHistoryList.clear()
                }

                Log.d("log history list","must set value of livedata")
                _logCheckHistory1.value = checkHistoryList

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("log eror", "$error")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        valueEventListener?.let {
            query.removeEventListener(it)
            valueEventListener = null
        }
    }


}