package com.example.myattendance.helper

import androidx.lifecycle.MutableLiveData
import com.example.myattendance.datamodel.Location_UIModel
import com.example.myattendance.helper.dateFormat.dateFormatOnlyDateMonthString
import com.example.myattendance.helper.dateFormat.dateFormatOnlyTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object dateFormat {
    val dateFormatOnlyTime = "HH:mm"
    val dateFormatOnlyDate = "dd-MM-yyyy"
    val dateFormatOnlyDateMonthString = "dd MMMM yyyy"
}

val locationDummy = Location_UIModel(-1,"","","")

val timeNowEpoch = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

fun <T> MutableLiveData<T>.mutation(actions: (MutableLiveData<T>) -> Unit) {
    actions(this)
    this.value = this.value
}

//get time now in form string UTC
fun getTimeNowInUTCString() : String{
    val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatDate.timeZone = TimeZone.getTimeZone("UTC")
    val formattedDate = formatDate.format(Date())
    return formattedDate
}

//form string ISO to time and date
fun getTimeAndDate(dateISO: String): List<String>{
    val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatDate.timeZone = TimeZone.getTimeZone("UTC")
    //from iso (string) back to date (awalys converted to default system)
    //parse is from string to date
    val date = formatDate.parse(dateISO)

    //setting format for date like 08-09-2014
    val formatDateLocal = SimpleDateFormat(dateFormat.dateFormatOnlyDate,Locale("ID"))
    //change to another timezone while formatting
    formatDateLocal.timeZone = TimeZone.getDefault()
    //format date to string (by custom format)
    //format is from date to string
    val formattedDate = formatDateLocal.format(date as Date)

    //setting format for time like 09:00
    val formatDateLocal2 = SimpleDateFormat(dateFormat.dateFormatOnlyTime,Locale("ID"))
    //change to another timezone while formatting
    formatDateLocal2.timeZone = TimeZone.getDefault()
    //format date to string (by custom format)
    //format is from date to string
    val formattedDate2 = formatDateLocal2.format(date as Date)

    return listOf(formattedDate, formattedDate2)
}

//from date ISO in UTC to date in local timezone and local style
fun getTimeAMPM(dateISO: String) : String{
    val zonedDateTime = ZonedDateTime.parse(dateISO, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Asia/Jakarta")))
    val jakartaFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a")
    val jakartaDateTime = zonedDateTime.format(jakartaFormatter)
    return jakartaDateTime
}

//from localdate to get hour minutes (HH:mm)
fun timeHourMinutesNow() : String = LocalTime.now().format(DateTimeFormatter.ofPattern(dateFormatOnlyTime))

// from localdate to get date time (xx mm yyyy)
fun timeDateNow() : String = LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormatOnlyDateMonthString))

