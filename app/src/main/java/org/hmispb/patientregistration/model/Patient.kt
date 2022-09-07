package org.hmispb.patientregistration.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey
    val crNo : String,
    val patFirstName : String,
    val patMiddleName : String,
    val patLastName : String,
    val isActualDob : Int = 0,
    val patAgeId : Int,
    val patAgeUnitId : String,
    val patGenderCodeId : String,
    val patGuardianName : String,
    val patHusbandName : String,
    val patMotherName : String,
    val patAddCountryCodeId : Int,
    val patAddStateCodeId : Int,
    val patAddMobileNo : Long,
    var isUploaded : Boolean = false
)
