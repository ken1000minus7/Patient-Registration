package org.hmispb.patientregistration.model

data class SavePatientRequest(
    //TODO take hospital code and seat ID from Login api
    val hospitalCode : String = "998",//for now
    val seatId : String = "10001",
    val inputDataJson : String,
    val modeForData : String = "REGISTER_NEW_PATIENT"
)