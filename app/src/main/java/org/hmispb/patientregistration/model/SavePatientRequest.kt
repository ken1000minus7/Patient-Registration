package org.hmispb.patientregistration.model

data class SavePatientRequest(
    val hospitalCode : Int,
    val seatId : Int,
    val inputDataJson : String,
    val modeForData : String = "REGISTER_NEW_PATIENT"
)