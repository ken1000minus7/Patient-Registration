package org.hmispb.patientregistration.model

data class LoginRequest(
    val userName: String,
    val password: String
)