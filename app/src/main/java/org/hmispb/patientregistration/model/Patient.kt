package org.hmispb.patientregistration.model

data class Patient(
    val patFirstName : String,
    val patMiddleName : String,
    val patLastName : String,
    val isActualDob : Boolean = false,
    val patAgeId : Int,
    val patAgeUnitId : String,
    val patGuardianName : String,
    val patHusbandName : String,
    val patMotherName : String,
    val patAddCountryCodeId : Int,
    val patAddStateCodeId : Int,
    val patAddMobileNo : Int
)
