package org.hmispb.patientregistration.model

data class LoginResponse(
    val  UserName: String,
    val  hospitalCode : String,
    val  hospitalName: String,
    val  responseStatus: String
)