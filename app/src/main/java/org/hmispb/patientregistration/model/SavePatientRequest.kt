package org.hmispb.patientregistration.model

data class SavePatientRequest(
    val hospitalCode : String,
    val seatId : String,
    val inputDataJson : String,
    val modeForData : String = "REGISTER_NEW_PATIENT"
)